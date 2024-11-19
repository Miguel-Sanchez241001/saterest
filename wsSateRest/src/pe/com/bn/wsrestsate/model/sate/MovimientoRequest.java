package pe.com.bn.wsrestsate.model.sate;

import lombok.Data;

 
@Data
public class MovimientoRequest {
    private String fechaInicio;
    private String fechaFin; // Opcional, depende del endpoint
    private String numTarjeta; // Agregado para el endpoint de prueba
}
