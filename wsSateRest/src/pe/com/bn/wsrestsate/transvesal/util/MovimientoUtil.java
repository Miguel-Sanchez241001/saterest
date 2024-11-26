package pe.com.bn.wsrestsate.transvesal.util;

import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.transvesal.EstructuraMovimiento;
import pe.com.bn.wsrestsate.transvesal.ResponseCode;
import pe.com.bn.wsrestsate.transvesal.exception.ApplicationException;
import pe.com.bn.wsrestsate.transvesal.exception.NoMovementsFoundException;

import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class MovimientoUtil {
    private static final Logger logger = LogManager.getLogger(MovimientoUtil.class);

    // Formatos de nombre de archivo con fechas en formato AAMMDD (YYMMDD)
    public static final String ZIP_FILE_NAME_FORMAT = "INTERFA1%s.zip";
    public static final String TXT_FILE_NAME_FORMAT = "FILOG19%s.txt";

    public List<String> generarFechasEnRango(String fechaInicio, String fechaFin) throws ApplicationException {
        List<String> fechas = new ArrayList<>();
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            LocalDate startDate = LocalDate.parse(fechaInicio, inputFormatter);
            LocalDate endDate = LocalDate.parse(fechaFin, inputFormatter);

            while (!startDate.isAfter(endDate)) {
                fechas.add(startDate.format(inputFormatter));
                startDate = startDate.plusDays(1);
            }
            return fechas;
        } catch (Exception e) {
            logger.error("Error al generar fechas en rango: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseCode.OTHER_ERROR, "Formato de fecha incorrecto");
        }
    }

    public List<Movimiento> procesarArchivoZip(InputStream zipStream, String fechaDDMMAAAA) throws ApplicationException {
        List<Movimiento> movimientos = new ArrayList<>();
        boolean archivoTxtEncontrado = false;

        try (ZipInputStream zis = new ZipInputStream(zipStream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                // Convertir fecha de DDMMAAAA a AAMMDD (YYMMDD)
                String fechaAAMMDD = convertirFechaAAMMDD(fechaDDMMAAAA);

                String expectedTxtFileName = String.format(TXT_FILE_NAME_FORMAT, fechaAAMMDD);
                if (entry.getName().equals(expectedTxtFileName)) {
                    logger.debug("Procesando entrada del ZIP: {}", entry.getName());
                	archivoTxtEncontrado = true;

                    // Leer la entrada ZIP manualmente en un ByteArrayOutputStream
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] tempBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(tempBuffer)) != -1) {
                        buffer.write(tempBuffer, 0, bytesRead);
                    }

                    // Crear un InputStream a partir del contenido leído
                    try (InputStream txtStream = new ByteArrayInputStream(buffer.toByteArray())) {
                        movimientos.addAll(parsearArchivoMovimientos(txtStream));
                    }
                }
            }

            if (!archivoTxtEncontrado) {
                logger.warn("No se encontró el archivo TXT esperado dentro del ZIP para la fecha: {}", fechaDDMMAAAA);
                throw new NoMovementsFoundException();
            }

            if (movimientos.isEmpty()) {
                logger.warn("No se encontraron movimientos en el archivo TXT para la fecha: {}", fechaDDMMAAAA);
                throw new NoMovementsFoundException();
            }

            return movimientos;
        } catch (IOException e) {
            logger.error("Error al procesar el archivo ZIP: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR, "Error al procesar el archivo ZIP");
        }
    }



    private List<Movimiento> parsearArchivoMovimientos(InputStream txtStream) throws ApplicationException {
        List<Movimiento> movimientos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(txtStream))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Ignorar la primera línea
                    continue;
                }
                Movimiento movimiento = parsearLineaMovimiento(line);
                movimientos.add(movimiento);
            }
            return movimientos;
        } catch (IOException e) {
            logger.error("Error al leer el archivo TXT: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseCode.UNEXPECTED_ERROR, "Error al leer el archivo TXT");
        }
    }

    private Movimiento parsearLineaMovimiento(String line) throws ApplicationException {
        try {
            Map<String, String> valores = ExtraccionCampos.extraerCampos(line, EstructuraMovimiento.CAMPOS);

            Movimiento movimiento = new Movimiento();
            movimiento.setTipoRegistro(valores.get("tipoRegistro"));
            movimiento.setCodigoOrganizacion(valores.get("codigoOrganizacion"));
            movimiento.setCodigoAgencia(valores.get("codigoAgencia"));
            movimiento.setCodigoCliente(valores.get("codigoCliente"));
            movimiento.setNumeroCuenta(valores.get("numeroCuenta"));
            movimiento.setCodigoMoneda(valores.get("codigoMoneda"));
            movimiento.setFechaTransaccion(parseDate(valores.get("fechaTransaccion"))); // YYYYMMDD
            movimiento.setClaseTransaccion(valores.get("claseTransaccion"));
            movimiento.setCodigoTransaccion(valores.get("codigoTransaccion"));
            movimiento.setNumeroTarjeta(valores.get("numeroTarjeta"));
            movimiento.setImporteTransaccion(valores.get("importeTransaccion"));
            movimiento.setMonedaTransaccion(valores.get("monedaTransaccion"));
            movimiento.setOficinaTransaccion(valores.get("oficinaTransaccion"));
            movimiento.setTipoCambio(valores.get("tipoCambio"));
            movimiento.setImporteCambio(valores.get("importeCambio"));
            movimiento.setDescripcionTransaccion(valores.get("descripcionTransaccion"));
            movimiento.setTipoTransaccion(valores.get("tipoTransaccion"));
            movimiento.setModuloLogica(valores.get("moduloLogica"));
            movimiento.setCodigoComercio(valores.get("codigoComercio"));
            movimiento.setCuentaCargo(valores.get("cuentaCargo"));
            movimiento.setOrigenTransaccion(valores.get("origenTransaccion"));
            movimiento.setCodigoAutorizacion(valores.get("codigoAutorizacion"));
            movimiento.setFechaProceso(parseDate(valores.get("fechaProceso"))); // YYYYMMDD
            movimiento.setCodigoPlan(valores.get("codigoPlan"));
            movimiento.setCodigoTransaccionOrigen(valores.get("codigoTransaccionOrigen"));
            movimiento.setReservado(valores.get("reservado"));
            movimiento.setImporteOriginalSinITF(valores.get("importeOriginalSinITF"));
            movimiento.setFinalArchivo(valores.get("finalArchivo"));

            return movimiento;
        } catch (Exception e) {
            logger.error("Error al parsear la línea: {}", line, e);
            throw new ApplicationException(ResponseCode.OTHER_ERROR, "Error al parsear la línea del archivo TXT");
        }
    }


    private Date parseDate(String dateString) throws ApplicationException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            return format.parse(dateString);
        } catch (Exception e) {
            logger.error("Error al parsear la fecha: {}", dateString, e);
            throw new ApplicationException(ResponseCode.OTHER_ERROR, "Formato de fecha incorrecto en el archivo TXT");
        }
    }

    /**
     * Convierte una fecha de formato DDMMAAAA a AAMMDD (YYMMDD).
     *
     * @param fechaDDMMAAAA Fecha en formato DDMMAAAA
     * @return Fecha en formato AAMMDD
     * @throws ApplicationException Si el formato de fecha es incorrecto
     */
    public String convertirFechaAAMMDD(String fechaDDMMAAAA) throws ApplicationException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyMMdd");
            Date date = inputFormat.parse(fechaDDMMAAAA);
            String fechaAAMMDD = outputFormat.format(date);
           // logger.debug("Fecha convertida de {} a {}", fechaDDMMAAAA, fechaAAMMDD);
            return fechaAAMMDD;
        } catch (ParseException e) {
            logger.error("Error al convertir la fecha: {}", fechaDDMMAAAA, e);
            throw new ApplicationException(ResponseCode.OTHER_ERROR, "Formato de fecha incorrecto");
        }
    }
}
