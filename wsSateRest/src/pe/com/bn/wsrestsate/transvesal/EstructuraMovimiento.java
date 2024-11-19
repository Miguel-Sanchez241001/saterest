package pe.com.bn.wsrestsate.transvesal;

 
import java.util.Arrays;
import java.util.List;

import pe.com.bn.wsrestsate.transvesal.util.EstructuraCampo;

public class EstructuraMovimiento {
    public static final List<EstructuraCampo> CAMPOS = Arrays.asList(
        new EstructuraCampo(1, 1, "tipoRegistro"),
        new EstructuraCampo(2, 3, "codigoOrganizacion"),
        new EstructuraCampo(4, 6, "codigoAgencia"),
        new EstructuraCampo(7, 20, "codigoCliente"),
        new EstructuraCampo(21, 39, "numeroCuenta"),
        new EstructuraCampo(40, 43, "codigoMoneda"),
        new EstructuraCampo(44, 51, "fechaTransaccion"),
        new EstructuraCampo(52, 53, "claseTransaccion"),
        new EstructuraCampo(54, 56, "codigoTransaccion"),
        new EstructuraCampo(57, 75, "numeroTarjeta"),
        new EstructuraCampo(76, 90, "importeTransaccion"),
        new EstructuraCampo(91, 94, "monedaTransaccion"),
        new EstructuraCampo(95, 97, "oficinaTransaccion"),
        new EstructuraCampo(98, 107, "tipoCambio"),
        new EstructuraCampo(108, 122, "importeCambio"),
        new EstructuraCampo(123, 152, "descripcionTransaccion"),
        new EstructuraCampo(153, 153, "tipoTransaccion"),
        new EstructuraCampo(154, 156, "moduloLogica"),
        new EstructuraCampo(157, 167, "codigoComercio"),
        new EstructuraCampo(168, 181, "cuentaCargo"),
        new EstructuraCampo(182, 188, "origenTransaccion"),
        new EstructuraCampo(189, 194, "codigoAutorizacion"),
        new EstructuraCampo(195, 202, "fechaProceso"),
        new EstructuraCampo(203, 207, "codigoPlan"),
        new EstructuraCampo(208, 210, "codigoTransaccionOrigen"),
        new EstructuraCampo(211, 219, "reservado"),
        new EstructuraCampo(220, 228, "importeOriginalSinITF"),
        new EstructuraCampo(229, 229, "finalArchivo")
    );
}
