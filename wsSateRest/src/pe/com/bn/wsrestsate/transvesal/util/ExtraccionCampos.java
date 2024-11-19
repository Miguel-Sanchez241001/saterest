package pe.com.bn.wsrestsate.transvesal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraccionCampos {

    public static Map<String, String> extraerCampos(String linea, List<EstructuraCampo> estructura) {
        Map<String, String> valores = new HashMap<>();

        for (EstructuraCampo campo : estructura) {
            int inicio = campo.getPosicionInicial(); 
            int fin = campo.getDimension();
            if (linea.length() >= fin) {
            	
                valores.put(campo.getNombre(), extractSubstring(linea,inicio, fin));
            } else {
                valores.put(campo.getNombre(), ""); // Si no existe el campo, dejar vacío
            }
        }

        return valores;
    }
    public static  String extractSubstring(String input, int start, int length) {
        if (input == null || start < 0 || length < 0 || start + length > input.length()) {
            throw new IllegalArgumentException("Rango de índices inválido o entrada nula");
        }
        start = start -1;
        return input.substring(start, start + length);
    }
}
