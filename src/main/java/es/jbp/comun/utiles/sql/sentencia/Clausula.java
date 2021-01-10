package es.jbp.comun.utiles.sql.sentencia;

/**
 *
 * @author Jorge Berjano
 */
public interface Clausula {
    void setContenido(String str);
    String getContenido();
    void setPalabraClave(String palabraClave);
    String getPalabraClave();
    // Extrae la clausula de una sentencia sql completa
    String extraer(String strSql);
}
