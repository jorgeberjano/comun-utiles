package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.depuracion.GestorLog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.sql.compatibilidad.CompatibilidadSql;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.tiempo.FechaHora;
import java.sql.SQLRecoverableException;

/**
 * Implementación de un gestor de conexiones como un pool.
 *
 * @author jberjano
 */
public class PoolConexiones implements GestorConexiones {

    private final String driver;
    private final String cadenaConexion;
    private final String usuario;
    private final String password;
    private final boolean local;
    private List<Connection> pool = new ArrayList();
    private String ultimoError;
    private final ConstructorFechaHora constructorFechaHora = new ConstructorFechaHora();

    @Override
    public void finalize() {
        cerrarConexiones();
    }

    public PoolConexiones(String driver, String cadenaConexion, String usuario, String password, boolean local) {
        this.driver = driver;
        this.cadenaConexion = cadenaConexion;
        this.usuario = usuario;
        this.password = password;
        this.local = local;
    }

    @Override
    public void inicializar() throws ClassNotFoundException {
        Class.forName(driver);
    }

    @Override
    public synchronized Connection obtenerConexion() throws SQLException {

        if (pool.isEmpty()) {
            return DriverManager.getConnection(cadenaConexion, usuario, password);
        }
        Connection conexion = pool.get(pool.size() - 1);
        pool.remove(conexion);
        return conexion;
    }

    @Override
    public synchronized void liberarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                pool.add(conexion);
            }
            //System.out.println("Conexiones: " + pool.size());
        } catch (SQLException ex) {
        }
    }

    @Override
    public FechaHora getFechaHora() {
        EjecutorSentenciaSelect ejecutorSentenciaSelect = new EjecutorSentenciaSelect(this);
        try {
            String sql = getFormateadorSql().getSelectConsultaFechaHora();
            return ejecutorSentenciaSelect.obtenerEntidad(sql, constructorFechaHora);
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized void cerrarConexiones() {
        for (Connection conexion : pool) {
            try {
                conexion.close();
            } catch (SQLException ex) {
            }
        }
        pool.removeAll(pool);
    }

    @Override
    public void procesarExcepcion(Exception ex) {
        ultimoError = ex.getMessage();
        if (ex instanceof SQLRecoverableException) {
            GestorLog.error("Se ha producido una excepción SQL recuperable, se cierran las conexiones", ex);
            cerrarConexiones();
        }
    }

    @Override
    public boolean esConexionLocal() {
        return local;
    }

    @Override
    public FormateadorSql getFormateadorSql() {
        return CompatibilidadSql.getFormateador(driver);
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public String getUltimoError() {
        return ultimoError;
    }

}
