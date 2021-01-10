package es.jbp.comun.utiles.sql;

import static java.sql.Types.BIGINT;
import static java.sql.Types.BLOB;
import static java.sql.Types.BOOLEAN;
import static java.sql.Types.CHAR;
import static java.sql.Types.DATE;
import static java.sql.Types.DECIMAL;
import static java.sql.Types.DOUBLE;
import static java.sql.Types.FLOAT;
import static java.sql.Types.INTEGER;
import static java.sql.Types.LONGNVARCHAR;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.SMALLINT;
import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.TINYINT;
import static java.sql.Types.VARCHAR;


/**
 * Define los tipos de datos posibles que pueden tener los campos de una tabla.
 * @author jberjano
 */
public enum TipoDato implements Cloneable {
    CADENA,
    ENTERO,
    REAL,
    BOOLEANO,
    FECHA,
    FECHA_HORA,
    BYTES,
    DESCONOCIDO;
    
    public static TipoDato fromSqlType(Integer tipoSql, String typeName, Integer decimales) {
        
        if (tipoSql == null) {    
            return DESCONOCIDO;
        }
        // En SQLite no existen tipos fechas propiamente dicho, pero el typename
        // si delata que se he declarado como tipo de fecha
        if (tipoSql != DATE && tipoSql != TIMESTAMP) {
            if ("TIMESTAMP".equals(typeName)) {
                return TipoDato.FECHA_HORA;
            } else if ("DATE".equals(typeName)) {
                return TipoDato.FECHA;
            }
        }
        
        switch (tipoSql) {
            case CHAR:
            case VARCHAR:
            case LONGNVARCHAR:
                return CADENA;   
            case TINYINT:
            case SMALLINT:
            case INTEGER:  
            case BIGINT:
                return ENTERO;
            case DECIMAL:
            case NUMERIC:              
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
            default:
                return DESCONOCIDO;
        }
    }    
}
