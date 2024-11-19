package pe.com.bn.wsrestsate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import pe.com.bn.wsrestsate.model.sate.Movimiento;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                movimiento.setFechaTransaccion(new Date(fechaInicioDate.getTime() + i * 86400000L)); // Incrementa un día por cada iteración
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
        logger.info("Iniciando obtenerMovimientosDesdeSftp para fecha: {}", fechaDDMMAAAA);

        List<Movimiento> movimientos = new ArrayList<>();
        ChannelSftp sftp = null;

        try {
            sftp = sftpPool.borrowObject();
            sftp.cd(parametros.getPathFtp());

            // Convertir fecha de DDMMAAAA a AAMMDD (YYMMDD)
            String fechaAAMMDD = movimientoUtil.convertirFechaAAMMDD(fechaDDMMAAAA);

            String zipFileName = String.format(MovimientoUtil.ZIP_FILE_NAME_FORMAT, fechaAAMMDD);

            logger.info("Obteniendo archivo ZIP: {}", zipFileName);

            try (InputStream zipStream = sftp.get(zipFileName)) {
                movimientos = movimientoUtil.procesarArchivoZip(zipStream, fechaDDMMAAAA);
            } catch (SftpException e) {
                logger.error("Error al obtener el archivo ZIP del SFTP: {}", e.getMessage(), e);
                throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR, "Error al acceder al archivo en SFTP");
            }
        } catch (SftpException e) {
            logger.error("Error al acceder al SFTP: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR, "Error al acceder al SFTP");
        } catch (ApplicationException e) {
            // Re-lanzar la excepción personalizada
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado en obtenerMovimientosDesdeSftp: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR);
        } finally {
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
                // Continuar con la siguiente fecha
            } catch (ApplicationException e) {
                // Otros errores, lanzar excepción
                throw e;
            }
        }

        if (movimientos.isEmpty()) {
            throw new NoMovementsFoundException();
        }

        logger.info("Total de movimientos obtenidos en el rango: {}", movimientos.size());
        return movimientos;
    }
}
