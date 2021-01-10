package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import es.jbp.comun.utiles.conversion.Conversion;

/**
 *
 * @author jberjano
 */
public class EjecutorSentenciaSimple extends Ejecutor {

    protected final GestorConexiones gestorConexiones;

    public EjecutorSentenciaSimple(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
    }

    public boolean ejecutar(String sql) throws SQLException {
        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        traza(sql);
        PreparedStatement statement = null;
        try {
            statement = conexion.prepareStatement(sql);
            statement.executeUpdate();
            exito();
        } catch (SQLException ex) {
            grabarTraza();
            gestorConexiones.procesarExcepcion(ex);
            throw ex;
        } finally {
            if (statement != null) {
                statement.close();
            }
            gestorConexiones.liberarConexion(conexion);
        }
        return true;
    }

    public boolean ejecutarBatch(String sql) throws SQLException {
        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        Statement statement = null;

        String sentencias[] = sql.split(";");

        try {
            statement = conexion.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            for (String sentencia : sentencias) {
                if (Conversion.isBlank(sentencia.trim())) {
                    continue;
                }
                statement.addBatch(sentencia);
            }
            statement.executeBatch();
        } finally {
            if (statement != null) {
                statement.close();
            }
            gestorConexiones.liberarConexion(conexion);
        }
        return true;
    }
}
