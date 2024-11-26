package pe.com.bn.wsrestsate.model;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class MovimientoDTO {
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date fechaTxn;
    private String descripcionTxn;
    private String monOriginalTxn;
    private String montoTxn;
    private String sigMontoTxn;
    private String operacionTxn;
    private String codAutTxn;
    private String numTarjetaTxn;
 }

