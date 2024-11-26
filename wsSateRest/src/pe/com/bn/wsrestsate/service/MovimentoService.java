package pe.com.bn.wsrestsate.service;

import pe.com.bn.wsrestsate.model.MovimientoDTO;
import pe.com.bn.wsrestsate.model.sate.Movimiento;
import pe.com.bn.wsrestsate.model.sate.MovimientoRequest;

import java.util.List;

public interface MovimentoService {
    List<Movimiento> crearMovimientosPrueba(String numTarjeta, String fechaInicio, String fechaFin) throws Exception;
    List<Movimiento> obtenerMovimientosDesdeSftp(String fecha) throws Exception;
    List<Movimiento> obtenerMovimientosEnRango(String fechaInicio, String fechaFin) throws Exception;
    List<MovimientoDTO> obtenerMovimientosEnRangoFiltro(MovimientoRequest request) throws Exception;

}
