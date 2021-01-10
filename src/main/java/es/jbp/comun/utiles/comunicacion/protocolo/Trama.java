package es.jbp.comun.utiles.comunicacion.protocolo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import es.jbp.comun.utiles.buffer.Buffer;
import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.tiempo.Fecha;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Trama de comunicaciones consistente en una serie de entradas con una clave y 
 * un valor.
 * @author jberjano
 */
public class Trama {

    private final Map<String, Object> mapaValores = new HashMap();

    public Trama() {
    }
        
    public void set(String clave, Object valor) {
        if (valor != null) {
            mapaValores.put(clave, valor);
        }
    }
    
    public String getString(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toString(valor);
    }
    
    public Integer getInteger(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toInteger(valor);
    }
    
    public Long getLong(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toLong(valor);
    }

    public Double getDounble(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toDouble(valor);
    }
    
    public Boolean getBoolean(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toBoolean(valor);
    }
    
    public Fecha getFecha(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toFecha(valor);
    }
    
    public FechaHora getFechaHora(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toFechaHora(valor);
    }
    
    public byte[] getBytes(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toByteArray(valor);
    }
    
    public Buffer getBuffer(String clave) {
        Object valor = mapaValores.get(clave);
        return Conversion.toBuffer(valor);
    }
   
    public Set<String> getClaves() {
        return mapaValores.keySet();
    }

    public Object getObject(String clave) {
        return mapaValores.get(clave);
    }
}
