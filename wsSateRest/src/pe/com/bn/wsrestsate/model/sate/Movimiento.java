package pe.com.bn.wsrestsate.model.sate;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class Movimiento {
	@JsonIgnore
    private String tipoRegistro;             // Posici�n 1
	@JsonIgnore
	private String codigoOrganizacion;       // Posici�n 2-3
    private String codigoAgencia;            // Posici�n 4-6
    private String codigoCliente;            // Posici�n 7-20
    private String numeroCuenta;             // Posici�n 21-39
    private String codigoMoneda;             // Posici�n 40-43
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaTransaccion;           // Posici�n 44-51
    private String claseTransaccion;         // Posici�n 52-53
    private String codigoTransaccion;        // Posici�n 54-56
    private String numeroTarjeta;            // Posici�n 57-75
    private String importeTransaccion;       // Posici�n 76-90
    private String monedaTransaccion;        // Posici�n 91-94
    private String oficinaTransaccion;       // Posici�n 95-97
    private String tipoCambio;               // Posici�n 98-107
    private String importeCambio;            // Posici�n 108-122
    private String descripcionTransaccion;   // Posici�n 123-152
    private String tipoTransaccion;          // Posici�n 153
    @JsonIgnore
    private String moduloLogica;             // Posici�n 154-156
    private String codigoComercio;           // Posici�n 157-167
    private String cuentaCargo;              // Posici�n 168-181
    private String origenTransaccion;        // Posici�n 182-188
    private String codigoAutorizacion;       // Posici�n 189-194
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaProceso;               // Posici�n 195-202
    @JsonIgnore
    private String codigoPlan;               // Posici�n 203-207
    private String codigoTransaccionOrigen;  // Posici�n 208-210
    @JsonIgnore
    private String reservado;                // Posici�n 211-219
    @JsonIgnore
    private String importeOriginalSinITF;    // Posici�n 220-228
    @JsonIgnore
    private String finalArchivo;             // Posici�n 229
}
