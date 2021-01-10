
package es.jbp.comun.utiles.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Utilidad para la lectura de propiedades con conversión de datos.
 * @author jberjano
 */
public class Propiedades {
    private final Properties properties;
    private final String namespace;    
    
    public Propiedades() {
        this.properties = new Properties();
        this.namespace = "";
    }
            
    public Propiedades(Properties properties) {
        this.properties = properties;
        this.namespace = "";
    }
    
    public Propiedades(Properties properties, String namespace) {
        this.properties = properties;
        this.namespace = namespace;
    }
    
    public Propiedades(Propiedades propiedades, String namespace) {
        
        this(propiedades.getProperties(), concatenar(propiedades.namespace, namespace));
    }
    
    public Propiedades getPropiedades(String namespace) {
        return new Propiedades(this, namespace);
    }
    
    public void cargar(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }
    
    public void guardar(OutputStream outputStream) throws IOException {
        properties.store(outputStream, "");
    }    

    public Properties getProperties() {
        return properties;
    }
    
    public String getString(String nombre) {
        return getString(nombre, "");
    }
    
    public String getString(String nombre, String valorPorDefecto) {
        String valor = getProperty(nombre, valorPorDefecto);
        return valor == null || valor.isEmpty() ? valorPorDefecto : valor;
    }
    
    public void setString(String nombre, String valor) {
        setProperty(nombre, valor);        
    }
    
    public Integer getInteger(String nombre, Integer valorPorDefecto) {
        
        String valorCadena = getProperty(nombre);
        Integer valor = Conversion.toInteger(valorCadena);
        return valor != null ? valor : valorPorDefecto;        
    }
    
    public void setInteger(String nombre, Integer valor) {
        setProperty(nombre, Integer.toString(valor));        
    }
    
    public Long getLong(String nombre, Long valorPorDefecto) {        
        String valorCadena = getProperty(nombre);
        Long valor = Conversion.toLong(valorCadena);
        return valor != null ? valor : valorPorDefecto;        
    }
    
    public void setLong(String nombre, Long valor) {
        setProperty(nombre, Long.toString(valor));
    }
    
    public Boolean getBoolean(String nombre, Boolean valorPorDefecto) {
        String valorCadena = getProperty(nombre);
        Boolean valor = Conversion.toBoolean(valorCadena);
        return valor != null ? valor : valorPorDefecto;        
    }
    
    public void setBoolean(String nombre, boolean valor) {
        setProperty(nombre, valor ? "si" : "no");
    }
    
    public Double getDouble(String nombre, Double valorPorDefecto) {
        
        String valorCadena = getProperty(nombre);
        Double valor = Conversion.toDouble(valorCadena);
        return valor != null ? valor : valorPorDefecto;        
    }
    
    public void setDouble(String nombre, Double valor) {
        setProperty(nombre, Double.toString(valor));
    }
    
    private String getProperty(String nombre) {
        return getProperty(nombre, "");
    }
    
    private String getProperty(String nombre, String valorPorDefecto) {
        return properties.getProperty(concatenar(namespace, nombre), valorPorDefecto);
    }
    
    private void setProperty(String nombre, String valor) {
        properties.setProperty(concatenar(namespace, nombre), valor);
    }
    
    private static String concatenar(String str1, String str2) {
        if (Conversion.isBlank(str1)) {
            return str2;
        } else if (Conversion.isBlank(str2)) {
            return str1;
        } else {
            return str1 + "." + str2;
        }
                    
    }
    
}
