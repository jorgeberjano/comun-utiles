package es.jbp.comun.utiles.sql;

/**
 * Clase base para los ejecutores de sentencias de bases de datos
 * @author jberjano
 */
public class    Ejecutor {
    private StringBuilder builder = new StringBuilder();
    
    protected void traza(Object obj) {
        if (builder.length() > 0) {
            builder.append("\n");
        }
        if (obj != null) {
            builder.append(obj.toString());
        } else {
            builder.append("null");
        }
    }
    protected void grabarTraza() {
        Dao.trazaSql(builder.toString(), false);
        builder = new StringBuilder();
    }
    
    protected void exito() {
        Dao.trazaSql(builder.toString(), true);
        builder = new StringBuilder();
    }    
}
