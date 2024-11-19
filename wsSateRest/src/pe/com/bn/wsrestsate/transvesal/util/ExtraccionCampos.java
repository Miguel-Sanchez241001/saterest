package pe.com.bn.wsrestsate.transvesal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraccionCampos {

    public static Map<String, String> extraerCampos(String linea, List<EstructuraCampo> estructura) {
        Map<String, String> valores = new HashMap<>();

        for (EstructuraCampo campo : estructura) {
            int inicio = campo.getPosicionInicial() - 1; // Restar 1 porque las posiciones empiezan en 1
            int fin = campo.getPosicionFinal();
            if (linea.length() >= fin) {
                valores.put(campo.getNombre(), linea.substring(inicio, fin).trim());
            } else {
                valores.put(campo.getNombre(), ""); // Si no existe el campo, dejar vacío
            }
        }

        return valores;
    }
}
