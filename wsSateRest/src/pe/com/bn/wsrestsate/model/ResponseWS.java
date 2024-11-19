package pe.com.bn.wsrestsate.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.transvesal.ResponseCode;
 


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWS {
    private String codigo;      // Código de respuesta (por ejemplo, "0000" para éxito)
    private String mensaje;     // Mensaje informativo sobre la respuesta
    private List<Movimiento> data; // Lista de movimientos
    
    public ResponseWS(ResponseCode responseCode, List<Movimiento> data) {
        this.codigo = responseCode.getCode();
        this.mensaje = responseCode.getMessage();
        this.data = data;
    }
}
