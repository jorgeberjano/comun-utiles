package es.jbp.comun.utiles.sql.compatibilidad;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jberjano
 */
public class CompatibilidadSql {
    
    private static Map<String, FormateadorSql> formateadores = new HashMap<>();
    
    static {
        agregarFormateador("oracle.jdbc", new FormateadorOracle());
        agregarFormateador("org.h2", new FormateadorH2());
        agregarFormateador("org.apache.derby.jdbc", new FormateadorDerby());
        agregarFormateador("jstels.jdbc.xml", new FormateadorXml());
        agregarFormateador("net.ucanaccess.jdbc", new FormateadorAccess());
        agregarFormateador("org.sqlite.JDBC", new FormateadorSQLite());
        agregarFormateador("sqlserver", new FormateadorMsSql());
    }

    public static FormateadorSql getFormateador(String driver) {
        if (driver == null) {
            return formateadores.get("oracle.jdbc");
        }
        for (String key : formateadores.keySet()) {
            if (driver.startsWith(key)) {
                return formateadores.get(key);
            }
        }
        return formateadores.get("oracle.jdbc");
    }
    
    public static void agregarFormateador(String driver, FormateadorSql formateador) {
        formateadores.put(driver, formateador);
    }
}   
