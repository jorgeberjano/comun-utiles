package es.jbp.comun.utiles.sql.esquema;

import static java.sql.Types.BLOB;
import static java.sql.Types.BOOLEAN;
import static java.sql.Types.CHAR;
import static java.sql.Types.DECIMAL;
import static java.sql.Types.DOUBLE;
import static java.sql.Types.FLOAT;
import static java.sql.Types.INTEGER;
import static java.sql.Types.LONGNVARCHAR;
import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.VARCHAR;
import static java.util.Calendar.DATE;


/**
 *
 * @author jberjano
 */
public enum TipoDatoBd {
    CADENA,
    ENTERO,
    REAL,
    BOOLEANO,
    FECHA,
    FECHA_HORA,
    BYTES;
    
    public static TipoDatoBd fromSqlType(Integer tipoSql, Integer decimales) {
        
        if (tipoSql == null) {
            return null;
        }
        
        switch (tipoSql) {
            case CHAR:
            case VARCHAR:
            case LONGNVARCHAR:
                return CADENA;            
            case INTEGER:            
                return ENTERO;
            case DECIMAL:
                return decimales != null && decimales > 0 ? REAL : ENTERO;
            case DOUBLE:
            case FLOAT:
                return REAL;
            case BOOLEAN:
                return BOOLEANO;
            case DATE:
                return FECHA;
            case TIMESTAMP:
                return FECHA_HORA;
            case BLOB:
                return BYTES;                
        }
        return null;
    }
}
