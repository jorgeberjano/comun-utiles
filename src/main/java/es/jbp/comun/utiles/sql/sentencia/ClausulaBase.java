package es.jbp.comun.utiles.sql.sentencia;

/**
 * Implementacion basica de una clausula sql
 *
 * @author Jorge Berjano
 */
public class ClausulaBase implements Clausula {

    private String palabraClave;
    private StringBuilder contenido;

    @Override
    public String toString() {
        String contenido = getContenido();
        if (contenido.isEmpty()) {
            return "";
        }
        return getPalabraClave() + " " + getContenido();
    }

    public ClausulaBase(String palabraClave) {
        this.palabraClave = palabraClave;
        this.contenido = new StringBuilder();
    }

    public void asignar(String texto) {
        if (contenido.length() > 0) {
            contenido = new StringBuilder();
        }
        contenido.append(texto);
    }
    
    public void agregar(String elemento, String separador) {
        if (contenido.length() > 0) {
            contenido.append(separador);
        }
        contenido.append(elemento);
    }

    /**
     * @return the palabraClave
     */
    @Override
    public String getPalabraClave() {
        return palabraClave;
    }

    /**
     * @param palabraClave the palabraClave to set
     */
    public void setPalabraClave(String palabraClave) {
        this.palabraClave = palabraClave;
    }

    /**
     * @return the contenido
     */
    @Override
    public String getContenido() {
        return contenido.toString();
    }

    /**
     * @param cadena the contenido to set
     */
    @Override
    public void setContenido(String cadena) {
        this.contenido = new StringBuilder(cadena);
    }

    /**
     * Extrae la clausula de una sentencia sql completa y devuelve el resto
     * @param sql la sentecia sql completa
     * @return el resto de la sentencia
     */
    public String extraer(String sql) {
            
        String sqlMayusculas = sql.toUpperCase();
        int pos = sqlMayusculas.indexOf(palabraClave);
        
        if (pos != -1) {
            String clausula = sql.substring(pos + palabraClave.length()).trim();
            setContenido(clausula);
            return sql.substring(0, pos);
        } else {
            return sql;
        }
    }

}
