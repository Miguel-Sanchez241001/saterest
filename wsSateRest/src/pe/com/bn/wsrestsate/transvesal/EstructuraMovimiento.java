package pe.com.bn.wsrestsate.transvesal;

 
import java.util.Arrays;
import java.util.List;

import pe.com.bn.wsrestsate.transvesal.util.EstructuraCampo;

public class EstructuraMovimiento {
    public static final List<EstructuraCampo> CAMPOS = Arrays.asList(
        new EstructuraCampo(1, 1, "tipoRegistro"),
        new EstructuraCampo(2, 2, "codigoOrganizacion"),
        new EstructuraCampo(4, 3, "codigoAgencia"),
        new EstructuraCampo(7, 14, "codigoCliente"),
        new EstructuraCampo(21, 19, "numeroCuenta"),
        new EstructuraCampo(40, 4, "codigoMoneda"),
        new EstructuraCampo(44, 8, "fechaTransaccion"),
        new EstructuraCampo(52, 2, "claseTransaccion"),
        new EstructuraCampo(54, 3, "codigoTransaccion"),
        new EstructuraCampo(57, 19, "numeroTarjeta"),
        new EstructuraCampo(76, 15, "importeTransaccion"),
        new EstructuraCampo(91, 4, "monedaTransaccion"),
        new EstructuraCampo(95, 3, "oficinaTransaccion"),
        new EstructuraCampo(98, 10, "tipoCambio"),
        new EstructuraCampo(108, 15, "importeCambio"),
        new EstructuraCampo(123, 30, "descripcionTransaccion"),
        new EstructuraCampo(153, 1, "tipoTransaccion"),
        new EstructuraCampo(154, 3, "moduloLogica"),
        new EstructuraCampo(157, 11, "codigoComercio"),
        new EstructuraCampo(168, 14, "cuentaCargo"),
        new EstructuraCampo(182, 7, "origenTransaccion"),
        new EstructuraCampo(189, 6, "codigoAutorizacion"),
        new EstructuraCampo(195, 8, "fechaProceso"),
        new EstructuraCampo(203, 5, "codigoPlan"),
        new EstructuraCampo(208, 3, "codigoTransaccionOrigen"),
        new EstructuraCampo(211, 9, "reservado"),
        new EstructuraCampo(220, 9, "importeOriginalSinITF"),
        new EstructuraCampo(229, 1, "finalArchivo")
    );
}

