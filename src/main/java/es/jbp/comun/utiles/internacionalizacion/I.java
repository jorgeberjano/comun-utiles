package es.jbp.comun.utiles.internacionalizacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para internacionalizar los textos.
 *
 * @author jberjano
 */
public class I {

    private static final Map<String, String> mapaTextos = new HashMap<String, String>();

    public static void configurar(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        mapaTextos.clear();
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split("=");
            if (partes.length == 2) {
                mapaTextos.put(partes[0], partes[1]);
            }
        }
        br.close();
    }

    public static String txt(String texto) {
        String textoSustituto = mapaTextos.get(texto);
        if (textoSustituto != null && !textoSustituto.isEmpty()) {
            return textoSustituto;
        } else {
            //System.out.println(texto);
            return texto;
        }
    }
}
