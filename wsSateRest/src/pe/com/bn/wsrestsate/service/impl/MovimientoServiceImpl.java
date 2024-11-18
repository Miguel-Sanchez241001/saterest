package pe.com.bn.wsrestsate.service.impl;

 import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

 import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.service.MovimentoService;

 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimentoService {
     private static final Logger logger = LogManager.getLogger(MovimientoServiceImpl.class);

// @Autowired
//    private SparkSession sparkSession;

   // @Value("${hadoop.data.rutaMovimientos}")
    //private String rutaMovimientos;

    
    @Override
    public List<Movimiento> crearMovimientosPrueba(String numTarjeta, String fechaInicio, String fechaFin) {
        //mostrarMovimientos();
        List<Movimiento> movimientos = new ArrayList<>();

        // Validación básica de los parámetros
        if (numTarjeta == null || numTarjeta.isEmpty()) {
            throw new IllegalArgumentException("El número de tarjeta no puede estar vacío.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden estar vacías.");
        }

        // Generar movimientos de prueba
        for (int i = 1; i <= 5; i++) {
            Movimiento movimiento = new Movimiento();
            movimiento.setId(String.valueOf(i));
            movimiento.setFechaTxn(new Date()); // Puedes ajustar para usar fechas simuladas entre fechaInicio y fechaFin
            movimiento.setDescripcionTxn("Compra " + i);
            movimiento.setMonOriginalTxn("USD");
            movimiento.setMontoTxn("10" + i + ".00");
            movimiento.setSigMontoTxn("5" + i + ".00");
            movimiento.setOperacionTxn("Compra");
            movimiento.setCodAutTxn("AUTH" + i);
            movimiento.setNumTarjetaTxn(numTarjeta);
            movimiento.setTipoTarjeta("Visa");

            movimientos.add(movimiento);
        }
    
        return movimientos;
    }


//    public void mostrarMovimientos() {
//        try {
//
//            String pathRutaLocal = "H:\\proyectoGit\\restSateSpark\\sate\\src\\main\\resources\\FILOG19240223.parquet";
//             Dataset<Row> movimientos = sparkSession.read().format("avro") .load(pathRutaLocal);
//
//
//             movimientos.printSchema();
//             movimientos.show(false);
//
//            // Cuenta de filas para verificar la carga
//            long rowCount = movimientos.count();
//            logger.info("Número de filas en el archivo Parquet: {}", rowCount);
//            movimientos.write().parquet("parquet");
//        } catch (Exception e) {
//            logger.error("Error al leer el archivo Parquet: ", e);
//        }
//    }


    @Override
    public void movimientosparquet() {
      //  mostrarMovimientos();
    }


	@Override
	public List<Movimiento> busc(String numTarjeta, String fechaInicio, String fechaFin) {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return null;
	}
}

