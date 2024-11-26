package pe.com.bn.wsrestsate.enpoints;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import pe.com.bn.wsrestsate.model.MovimientoDTO;
import pe.com.bn.wsrestsate.model.ResponseWS;
import pe.com.bn.wsrestsate.model.ResponseWSDTO;
import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.model.sate.MovimientoRequest;
import pe.com.bn.wsrestsate.service.MovimentoService;
import pe.com.bn.wsrestsate.transvesal.ResponseCode;
import pe.com.bn.wsrestsate.transvesal.exception.ApplicationException;
import pe.com.bn.wsrestsate.transvesal.exception.NoMovementsFoundException;

import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoRestcontroller {

    private final MovimentoService movimientoService;
    private static final Logger logger = LogManager.getLogger(MovimientoRestcontroller.class);

    /**
     * Endpoint de prueba que devuelve movimientos generados estáticamente.
     */
    @PostMapping("/crearMovimientosPrueba")
    public ResponseEntity<ResponseWS> crearMovimientosPrueba(@RequestBody MovimientoRequest request) {
        try {
            logger.info("Iniciando crearMovimientosPrueba con request: {}", request);

            List<Movimiento> movimientos = movimientoService.crearMovimientosPrueba(
                    request.getNumCuenta(), request.getFechaInicio(), request.getFechaFin()
            );
            logger.info("Movimientos de prueba generados exitosamente.");

            return ResponseEntity.ok(new ResponseWS(ResponseCode.SUCCESS, movimientos));
        } catch (NoMovementsFoundException e) {
            logger.warn("No se encontraron movimientos: {}", e.getMessage());
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (ApplicationException e) {
            logger.error("Error en crearMovimientosPrueba: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (Exception e) {
            logger.error("Error inesperado en crearMovimientosPrueba: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(ResponseCode.UNEXPECTED_ERROR, null));
        }
    }

    /**
     * Endpoint que obtiene movimientos de un día específico.
     */
    @PostMapping("/listarMovimientosDia")
    public ResponseEntity<ResponseWS> listarMovimientosDia(@RequestBody MovimientoRequest request) {
        try {
            logger.info("Iniciando listarMovimientosDia con request: {}", request);

            List<Movimiento> movimientos = movimientoService.obtenerMovimientosDesdeSftp(request.getFechaInicio());

            if (movimientos.isEmpty()) {
                throw new NoMovementsFoundException();
            }

            logger.info("Movimientos obtenidos exitosamente.");

            return ResponseEntity.ok(new ResponseWS(ResponseCode.SUCCESS, movimientos));
        } catch (NoMovementsFoundException e) {
            logger.warn("No se encontraron movimientos: {}", e.getMessage());
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (ApplicationException e) {
            logger.error("Error en listarMovimientosDia: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (Exception e) {
            logger.error("Error inesperado en listarMovimientosDia: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(ResponseCode.UNEXPECTED_ERROR, null));
        }
    }

    /**
     * Endpoint que obtiene movimientos en un rango de fechas.
     */
    @PostMapping("/listarMovimientosRango")
    public ResponseEntity<ResponseWS> listarMovimientosRango(@RequestBody MovimientoRequest request) {
        try {
            logger.info("Iniciando listarMovimientosRango con request: {}", request);

            List<Movimiento> movimientos = movimientoService.obtenerMovimientosEnRango(
                    request.getFechaInicio(), request.getFechaFin()
            );

            if (movimientos.isEmpty()) {
                throw new NoMovementsFoundException();
            }

            logger.info("Movimientos obtenidos exitosamente.");

            return ResponseEntity.ok(new ResponseWS(ResponseCode.SUCCESS, movimientos));
        } catch (NoMovementsFoundException e) {
            logger.warn("No se encontraron movimientos: {}", e.getMessage());
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (ApplicationException e) {
            logger.error("Error en listarMovimientosRango: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(e.getResponseCode(), null));
        } catch (Exception e) {
            logger.error("Error inesperado en listarMovimientosRango: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWS(ResponseCode.UNEXPECTED_ERROR, null));
        }
    }

    /**
     * Endpoint que obtiene movimientos en un rango de fechas y numCuenta.
     */
    @PostMapping("/listarMovimientosRangoAndCount")
    public ResponseEntity<ResponseWSDTO> listarMovimientosRangoAndCount(@RequestBody MovimientoRequest request) {
        try {
            logger.info("Iniciando listarMovimientosRango con request: {}", request);

            List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientosEnRangoFiltro(request);

            logger.info("Movimientos obtenidos exitosamente.");

            return ResponseEntity.ok(new ResponseWSDTO(ResponseCode.SUCCESS, movimientos));
        } catch (NoMovementsFoundException e) {
            logger.warn("No se encontraron movimientos: {}", e.getMessage());
            return ResponseEntity.ok(new ResponseWSDTO(e.getResponseCode(), null));
        } catch (ApplicationException e) {
            logger.error("Error en listarMovimientosRango: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWSDTO(e.getResponseCode(), null));
        } catch (Exception e) {
            logger.error("Error inesperado en listarMovimientosRango: {}", e.getMessage(), e);
            return ResponseEntity.ok(new ResponseWSDTO(ResponseCode.UNEXPECTED_ERROR, null));
        }
    }
}
