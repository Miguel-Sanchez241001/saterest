package pe.com.bn.wsrestsate.enpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EnpointRestContoller {

    // M�todo GET de prueba
    @GetMapping("/obtener")
    public ResponseEntity<Map<String, Object>> obtenerDatos() {
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "0000");
        response.put("msg", "Solicitud GET exitosa");
        response.put("data", "Aqu� se obtendr�an los datos requeridos");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // M�todo POST de prueba
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearDato() {
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "0000");
        response.put("msg", "Solicitud POST exitosa - Dato creado");
        response.put("data", "Aqu� se incluir�an los detalles del dato creado");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // M�todo PUT de prueba
    @PutMapping("/actualizar")
    public ResponseEntity<Map<String, Object>> actualizarDato() {
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "0000");
        response.put("msg", "Solicitud PUT exitosa - Dato actualizado");
        response.put("data", "Aqu� se incluir�an los detalles del dato actualizado");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // M�todo DELETE de prueba
    @DeleteMapping("/eliminar")
    public ResponseEntity<Map<String, Object>> eliminarDato() {
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "0000");
        response.put("msg", "Solicitud DELETE exitosa - Dato eliminado");
        response.put("data", "Aqu� se incluir�an los detalles del dato eliminado");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
