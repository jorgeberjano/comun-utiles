package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para ejecutar script de bases de datos
 *
 * @author Jorge Berjano
 */
public class EjecutorScript extends Ejecutor {

    protected final GestorConexiones gestorConexiones;

    public EjecutorScript(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
    }

    public boolean ejecutar(String scriptSql) throws SQLException {
        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return false;
        }

        String[] sentenciasSql = scriptSql.split(";");

        Statement statement = conexion.createStatement();

        for (String sentenciaSql : sentenciasSql) {
            sentenciaSql = sentenciaSql.trim();
            if (!sentenciaSql.isEmpty()) {
                statement.addBatch(sentenciaSql);
            }
        }
        int[] resultados = statement.executeBatch();
        for (int resultado : resultados) {
            if (resultado == statement.EXECUTE_FAILED) {
                return false;
            }
        }

        return true;
    }

}
