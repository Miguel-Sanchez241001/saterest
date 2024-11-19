package pe.com.bn.wsrestsate.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pe.com.bn.wsrestsate.service.CompService;
import pe.com.bn.wsrestsate.service.external.domain.ParametroInterfazKeyProxy;
import pe.com.bn.wsrestsate.service.external.domain.SistemaParametro;
import pe.com.bn.wsrestsate.transvesal.components.ParametrosComp;
import pe.com.bn.wsrestsate.transvesal.constantes.ConstantesGenerales;

 

@Service
public class CompServiceImpl implements CompService {

	private static final Logger logger = LogManager.getLogger(CompServiceImpl.class);

	@Autowired
	ParametrosComp parametros;
	@Value("${sistema}")
	private String sistema;

	@Value("${cuenta}")
	private String cuenta;

	@Value("${semilla}")
	private String semilla;

	@Value("${idUsuario}")
	private String idUsuario;

	@Value("${rutaClaveSegura}")
	private String rutaClaveSegura;
 
	
    @PostConstruct
    public void init() {
        try {
            asignarParametros();
        } catch (Exception e) {
            logger.error("Error al asignar parámetros: ", e);
        }
    }

	@Override
	public void asignarParametros() throws Exception {
		try {
			byte[] llave = leerClavesSegurades();

			ParametroInterfazKeyProxy proxyComp = new ParametroInterfazKeyProxy();

			SistemaParametro sParam = proxyComp.datoParametroService(sistema, cuenta, semilla, llave, idUsuario);

			logger.info("Codigo de proceso : " + sParam.getProceso().getCodigo());
			parametros.setErrorComp(sParam.getProceso().getCodigo());
			parametros.setDesErrorComp(sParam.getProceso().getDescripcion());

			if (sParam.getProceso().getCodigo().equals("00000")) {
				for (int n = 0; n < sParam.getGrupoParametro().getGrupoParametro().size(); n++) {
					int cantFilas = sParam.getGrupoParametro().getGrupoParametro().get(n).getParametro().getParametro()
							.size() - 1;
					for (int j = 0; j < cantFilas + 1; j++) {
						String param = sParam.getGrupoParametro().getGrupoParametro().get(n).getParametro()
								.getParametro().get(j).getAliasParam();
						String valor = sParam.getGrupoParametro().getGrupoParametro().get(n).getParametro()
								.getParametro().get(j).getValorParam();
						if (!param.equals("")) {
							this.setParametros(sParam.getGrupoParametro().getGrupoParametro().get(n).getAliasGrupo(),
									param, valor);
						}
					}
				}
				logger.info("Asignacion de parametros correctamente.");
				 
			} else {
				throw new Exception("No se pudo obtener los datos Comp " + sParam.getProceso().getCodigo());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private void setParametros(String aliasGrupo, String param, String valor) {

		if (aliasGrupo.equals(ConstantesGenerales.GRUPO_CONEXION_MC)) {
			this.setDatosMC(param, valor);
		}
		 
	}

 

	private void setDatosMC(String param, String valor) {
	    if (param.equals(ConstantesGenerales.PARAM_USER_FTP)) {
	        parametros.setUsuarioFtp(valor);
	    } else if (param.equals(ConstantesGenerales.PARAM_CLAVE_FTP)) {
	        parametros.setClaveFtp(valor);
	    } else if (param.equals(ConstantesGenerales.PARAM_PATH_FTP)) {
	        parametros.setPathFtp(valor);
	    } else if (param.equals(ConstantesGenerales.PARAM_IP_FTP)) {
	        parametros.setIpFtp(valor);
	    } else if (param.equals(ConstantesGenerales.PARAM_PUERTO_FTP)) {
	        parametros.setPuertoFtp(valor);
	    }
	}


	public byte[] leerClavesSegurades() throws Exception {
	    try {
	        Path filePath = new File(rutaClaveSegura).toPath();
	        return Files.readAllBytes(filePath);
	    } catch (IOException ioe) {
	        throw new Exception("Error con la lectura del archivo clavesegurades.key", ioe);
	    }
	}
}
