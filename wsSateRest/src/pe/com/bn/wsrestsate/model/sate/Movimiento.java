package pe.com.bn.wsrestsate.model.sate;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class Movimiento {
	@JsonIgnore
    private String tipoRegistro;             // Posición 1
	@JsonIgnore
	private String codigoOrganizacion;       // Posición 2-3
    private String codigoAgencia;            // Posición 4-6
    private String codigoCliente;            // Posición 7-20
    private String numeroCuenta;             // Posición 21-39
    private String codigoMoneda;             // Posición 40-43
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaTransaccion;           // Posición 44-51
    private String claseTransaccion;         // Posición 52-53
    private String codigoTransaccion;        // Posición 54-56
    private String numeroTarjeta;            // Posición 57-75
    private String importeTransaccion;       // Posición 76-90
    private String monedaTransaccion;        // Posición 91-94
    private String oficinaTransaccion;       // Posición 95-97
    private String tipoCambio;               // Posición 98-107
    private String importeCambio;            // Posición 108-122
    private String descripcionTransaccion;   // Posición 123-152
    private String tipoTransaccion;          // Posición 153
    @JsonIgnore
    private String moduloLogica;             // Posición 154-156
    private String codigoComercio;           // Posición 157-167
    private String cuentaCargo;              // Posición 168-181
    private String origenTransaccion;        // Posición 182-188
    private String codigoAutorizacion;       // Posición 189-194
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaProceso;               // Posición 195-202
    @JsonIgnore
    private String codigoPlan;               // Posición 203-207
    private String codigoTransaccionOrigen;  // Posición 208-210
    @JsonIgnore
    private String reservado;                // Posición 211-219
    @JsonIgnore
    private String importeOriginalSinITF;    // Posición 220-228
    @JsonIgnore
    private String finalArchivo;             // Posición 229
}
