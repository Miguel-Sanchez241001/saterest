package pe.com.bn.wsrestsate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bn.wsrestsate.model.MovimientoDTO;
import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.model.sate.MovimientoRequest;
import pe.com.bn.wsrestsate.service.MovimentoService;
import pe.com.bn.wsrestsate.transvesal.ResponseCode;
import pe.com.bn.wsrestsate.transvesal.components.ParametrosComp;
import pe.com.bn.wsrestsate.transvesal.components.SftpPool;
import pe.com.bn.wsrestsate.transvesal.exception.ApplicationException;
import pe.com.bn.wsrestsate.transvesal.exception.NoMovementsFoundException;
import pe.com.bn.wsrestsate.transvesal.util.MovimientoUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements MovimentoService {
	private static final Logger logger = LogManager.getLogger(MovimientoServiceImpl.class);

	@Autowired
	private SftpPool sftpPool;

	@Autowired
	private ParametrosComp parametros;

	@Autowired
	private MovimientoUtil movimientoUtil;

	@Override
	public List<Movimiento> crearMovimientosPrueba(String numTarjeta, String fechaInicio, String fechaFin) {
		List<Movimiento> movimientos = new ArrayList<>();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date fechaInicioDate = dateFormat.parse(fechaInicio);
			Date fechaFinDate = dateFormat.parse(fechaFin);

			for (int i = 1; i <= 5; i++) {
				Movimiento movimiento = new Movimiento();

				movimiento.setTipoRegistro("1"); // Tipo de registro fijo para la prueba
				movimiento.setCodigoOrganizacion("64");
				movimiento.setCodigoAgencia("001");
				movimiento.setCodigoCliente("C000" + i);
				movimiento.setNumeroCuenta("1234567890123456789");
				movimiento.setCodigoMoneda("USD");
				movimiento.setFechaTransaccion(new Date(fechaInicioDate.getTime() + i * 86400000L)); // Incrementa un
																										// día por cada
																										// iteración
				movimiento.setClaseTransaccion("10");
				movimiento.setCodigoTransaccion("200");
				movimiento.setNumeroTarjeta(numTarjeta);
				movimiento.setImporteTransaccion("100.00");
				movimiento.setMonedaTransaccion("USD");
				movimiento.setOficinaTransaccion("001");
				movimiento.setTipoCambio("3.50");
				movimiento.setImporteCambio("350.00");
				movimiento.setDescripcionTransaccion("Compra de prueba " + i);
				movimiento.setTipoTransaccion("D");
				movimiento.setModuloLogica("MOD");
				movimiento.setCodigoComercio("0000000001");
				movimiento.setCuentaCargo("12345678901234");
				movimiento.setOrigenTransaccion("ORIGEN" + i);
				movimiento.setCodigoAutorizacion("AUTH" + i);
				movimiento.setFechaProceso(new Date());
				movimiento.setCodigoPlan("P001");
				movimiento.setCodigoTransaccionOrigen("ITF");
				movimiento.setReservado("");
				movimiento.setImporteOriginalSinITF("95.00");
				movimiento.setFinalArchivo("0");

				movimientos.add(movimiento);
			}
		} catch (Exception e) {
			logger.error("Error al generar movimientos de prueba: {}", e.getMessage(), e);
		}

		return movimientos;
	}

	@Override
	public List<Movimiento> obtenerMovimientosDesdeSftp(String fechaDDMMAAAA) throws ApplicationException {
		logger.debug("Iniciando obtenerMovimientosDesdeSftp para fecha: {}", fechaDDMMAAAA);

		List<Movimiento> movimientos = new ArrayList<>();
		ChannelSftp sftp = null;

		try {
			sftp = sftpPool.borrowObject();
			sftp.cd(parametros.getPathFtp());

			// Convertir fecha de DDMMAAAA a AAMMDD (YYMMDD)
			String fechaAAMMDD = movimientoUtil.convertirFechaAAMMDD(fechaDDMMAAAA);

			String zipFileName = String.format(MovimientoUtil.ZIP_FILE_NAME_FORMAT, fechaAAMMDD);

			logger.debug("Obteniendo archivo ZIP: {}", zipFileName);

			try (InputStream zipStream = sftp.get(zipFileName)) {
				movimientos = movimientoUtil.procesarArchivoZip(zipStream, fechaDDMMAAAA);
			} catch (SftpException e) {
				logger.error("Error al obtener el archivo ZIP del SFTP: {}", e.getMessage());
				throw new NoMovementsFoundException();
			}
		} catch (NoMovementsFoundException e) {
			logger.error("Error al obenter movientoso: {}", e.getMessage());
			throw new NoMovementsFoundException();
		}catch (SftpException e) {
			logger.error("Error al acceder al SFTP: {}", e.getMessage(), e);
			throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR, "Error al acceder al SFTP");
		} catch (ApplicationException e) {
			throw e;
		}  catch (Exception e) {
			logger.error("Error inesperado en obtenerMovimientosDesdeSftp: {}", e.getMessage(), e);
			throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR);
		}finally {
			if (sftp != null) {
				try {
					sftpPool.returnObject(sftp);
				} catch (Exception e) {
					logger.error("Error al devolver el objeto SFTP al pool: {}", e.getMessage(), e);
				}
			}
		}

		if (movimientos.isEmpty()) {
			throw new NoMovementsFoundException();
		}

		logger.info("Movimientos obtenidos desde SFTP: {}", movimientos.size());
		return movimientos;
	}

	@Override
	public List<Movimiento> obtenerMovimientosEnRango(String fechaInicio, String fechaFin) throws ApplicationException {
		logger.info("Iniciando obtenerMovimientosEnRango desde {} hasta {}", fechaInicio, fechaFin);

		List<Movimiento> movimientos = new ArrayList<>();
		List<String> fechas = movimientoUtil.generarFechasEnRango(fechaInicio, fechaFin);

		for (String fecha : fechas) {
			try {
				List<Movimiento> movimientosDia = obtenerMovimientosDesdeSftp(fecha);
				movimientos.addAll(movimientosDia);
			} catch (NoMovementsFoundException e) {
				logger.warn("No se encontraron movimientos para la fecha: {}", fecha);
			} catch (ApplicationException e) {
				throw e;
			}
		}

		if (movimientos.isEmpty()) {
			throw new NoMovementsFoundException();
		}

		logger.info("Total de movimientos obtenidos en el rango: {}", movimientos.size());
		return movimientos;
	}

	@Override
	public List<MovimientoDTO> obtenerMovimientosEnRangoFiltro(MovimientoRequest request) throws Exception {

		List<Movimiento> movimientos = obtenerMovimientosEnRango(request.getFechaInicio(), request.getFechaFin());

		if (movimientos.isEmpty()) {
			throw new NoMovementsFoundException();
		}
		   List<MovimientoDTO> movimientosFiltrados = movimientos.stream()
		            .filter(movi -> {
		                LocalDate fechaProceso = movi.getFechaProceso().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                return  movi.getNumeroCuenta().equals(request.getNumCuenta());
		            })
		            .map(movi -> {
		                MovimientoDTO dto = new MovimientoDTO();
		                dto.setFechaTxn(movi.getFechaTransaccion());
		                dto.setDescripcionTxn(movi.getDescripcionTransaccion());
		                dto.setMonOriginalTxn(movi.getMonedaTransaccion().equals("0000")? "604" : "840");
		                dto.setMontoTxn(formatImporte(movi.getImporteTransaccion()));
		                dto.setSigMontoTxn(movi.getTipoTransaccion().equals("1")? "D" : "C");
		                dto.setOperacionTxn("");
		                dto.setCodAutTxn(movi.getCodigoAutorizacion());
		                dto.setNumTarjetaTxn(
		                	    movi.getNumeroTarjeta() != null && movi.getNumeroTarjeta().length() >= 4 
		                	        ? movi.getNumeroTarjeta().substring(movi.getNumeroTarjeta().length() - 4)
		                	        : "****" // En caso de que sea null o tenga menos de 4 caracteres
		                	);
 		                return dto;
		            })
		            .collect(Collectors.toList());
	 
		logger.info("Movimientos filtrados por número de cuenta '{}': {} movimientos", request.getNumCuenta(),
				movimientosFiltrados.size());

		// Validar si no hay movimientos tras aplicar el filtro
		if (movimientosFiltrados.isEmpty()) {
			logger.info("No se encontraron movimientos para la cuenta: {} en el rango de fechas: {} a {}",
					request.getNumCuenta(), request.getFechaInicio(), request.getFechaFin());
			throw new NoMovementsFoundException();
		}
		// Retornar lista de movimientos filtrados
		logger.info("Finalizando obtenerMovimientosEnRangoFiltro con {} movimientos filtrados",
				movimientosFiltrados.size());
		return movimientosFiltrados;

	}

	private String formatImporte(String importeRaw) {
	    if (importeRaw == null || importeRaw.isEmpty() || importeRaw.length() < 3) {
	        return "0.00"; // Valor predeterminado si el formato es inválido
	    }

	    try {
	        String parteEntera = importeRaw.substring(0, importeRaw.length() - 2); // Todos excepto los últimos 2 dígitos
	        String parteDecimal = importeRaw.substring(importeRaw.length() - 2); // Últimos 2 dígitos

	        // Formatear como 9999.99
	        return String.format("%,d.%s", Long.parseLong(parteEntera), parteDecimal);
	    } catch (Exception e) {
	        // Manejar errores de formato
	        return "0.00";
	    }
	}

}
