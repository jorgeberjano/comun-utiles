package es.jbp.comun.utiles.sql.sincronizacion;

import java.util.HashMap;
import java.util.Map;
import es.jbp.comun.utiles.conversion.Conversion;

/**
 *
 * @author jberjano
 */
public class EntidadGenerica {

    private Long pk;
    private Map<String, Object> mapaValores = new HashMap<String, Object>();
    
    public void setPk(String nombre, Object valor) {
        pk = Conversion.toLong(valor);
        set(nombre, valor);
    }
    
    public Long getPk() {
        return pk;
    }
    
    public void set(String nombre, Object valor) {
        mapaValores.put(nombre, valor);
    }

    public Object get(String nombre) {
        return mapaValores.get(nombre);
    }

}
