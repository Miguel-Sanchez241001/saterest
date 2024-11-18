package pe.com.bn.wsrestsate.model;

import lombok.Data;

@Data
public class RequestWS {
    private String fechaInicio; // Fecha de inicio en formato String (por ejemplo, "2024-01-01")
    private String fechaFin;    // Fecha de fin en formato String (por ejemplo, "2024-01-31")
    private String numTarjeta; 
}
