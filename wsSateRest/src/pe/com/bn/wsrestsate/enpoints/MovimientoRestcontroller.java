package pe.com.bn.wsrestsate.enpoints;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
 
 import pe.com.bn.wsrestsate.model.RequestWS;
import pe.com.bn.wsrestsate.model.ResponseWS;
import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.service.MovimentoService;

import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoRestcontroller {

    private final MovimentoService movimientoService;

    @PostMapping("/crearMovimientosPrueba")
    public ResponseEntity<?> crearMovimientosPrueba(@RequestBody RequestWS request) {
        try {
            List<Movimiento> movimientos = movimientoService.crearMovimientosPrueba(
                    request.getNumTarjeta(),
                    request.getFechaInicio(),
                    request.getFechaFin()
            );
            return ResponseEntity.ok(new ResponseWS("0000", "Movimientos generados con éxito", movimientos));
        } catch (IllegalArgumentException e) {
            // Manejo de error para parámetros inválidos
            return ResponseEntity.badRequest().body(new ResponseWS("9999", e.getMessage(), null));
        } catch (Exception e) {
            // Manejo de error genérico
            return ResponseEntity.status(500).body(new ResponseWS("9999", "Error en el sistema", null));
        }
    }
}


