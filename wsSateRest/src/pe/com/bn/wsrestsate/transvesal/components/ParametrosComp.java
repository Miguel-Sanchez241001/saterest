package pe.com.bn.wsrestsate.transvesal.components;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component("parametros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParametrosComp {

    // Parámetros relacionados con la conexión FTP
    private String usuarioFtp;             // Corresponde a ConstantesGenerales.PARAM_USER_FTP
    private String claveFtp;               // Corresponde a ConstantesGenerales.PARAM_CLAVE_FTP
    private String pathFtp;                // Corresponde a ConstantesGenerales.PARAM_PATH_FTP
    private String ipFtp;                  // Corresponde a ConstantesGenerales.PARAM_IP_FTP
    private String puertoFtp;              // Corresponde a ConstantesGenerales.PARAM_PUERTO_FTP

    // Variables para manejar errores
    private String errorComp;              // Código de error
    private String desErrorComp;           // Descripción del error

    // Otros campos pueden ser añadidos según sea necesario
}
