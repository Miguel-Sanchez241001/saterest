package pe.com.bn.wsrestsate.model.sate;
 
import java.util.Date;

import lombok.Data;

@Data
public class Movimiento {
    private String id;
    private Date fechaTxn;
    private String descripcionTxn;
    private String monOriginalTxn;
    private String montoTxn;
    private String sigMontoTxn;
    private String operacionTxn;
    private String codAutTxn;
    private String numTarjetaTxn;
    private String tipoTarjeta;

}
