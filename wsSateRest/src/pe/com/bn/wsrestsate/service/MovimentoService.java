package pe.com.bn.wsrestsate.service;

import java.util.List;

import pe.com.bn.wsrestsate.model.sate.Movimiento;

 
public interface MovimentoService {
     public List<Movimiento> crearMovimientosPrueba(String numTarjeta, String fechaInicio, String fechaFin);
     
     
     
     public List<Movimiento> busc(String numTarjeta, String fechaInicio, String fechaFin);

     public void movimientosparquet();
}
