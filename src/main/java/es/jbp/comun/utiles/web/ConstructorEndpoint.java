package es.jbp.comun.utiles.web;

import es.jbp.comun.utiles.conversion.Conversion;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class ConstructorEndpoint {

    private String urlBase;
    private String path;
    private final List<String> listaParametros = new ArrayList<>();
    private final Map<String, Object> mapaParametros = new HashMap<>();
    
    public ConstructorEndpoint urlBase(String urlBase) {
        this.urlBase = urlBase;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        resultado.append(urlBase);
        resultado.append(path);
        if (!mapaParametros.isEmpty()) {        
            resultado.append("?");
            boolean primero = true;
            for (String nombre : listaParametros) {
                if (primero) {                    
                    primero = false;
                } else {
                    resultado.append("&");
                }
                Object valor = mapaParametros.get(nombre);
                String valorString = Conversion.toString(valor);
                if (valorString == null) {
                    continue;
                }                
                resultado.append(codificar(nombre));
                resultado.append("=");
                resultado.append(codificar(valorString));
            }
        }
        return resultado.toString();
    }
    
    private String codificar(String texto) {
        try {
            return URLEncoder.encode(texto, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }

    public ConstructorEndpoint path(String path) {
        this.path = path;
        return this;
    }

    public ConstructorEndpoint parametro(String nombre, Object valor) {
        if (valor != null) {
            listaParametros.add(nombre);
            mapaParametros.put(nombre, valor);
        }
        return this;
    }

    
    
        
}
