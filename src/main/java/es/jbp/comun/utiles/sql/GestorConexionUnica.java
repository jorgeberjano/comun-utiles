package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.depuracion.GestorLog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import es.jbp.comun.utiles.sql.compatibilidad.CompatibilidadSql;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.tiempo.FechaHora;
import java.sql.SQLRecoverableException;

/**
 * Gestor de conexiones para un única conexión con la base de datos.
 *
 * @author jberjano
 */
public class GestorConexionUnica implements GestorConexiones {

    protected String driver;
    protected String cadenaConexion;
    protected String usuario;
    protected String password;
    private boolean local;
    private Connection conexion;
    private String ultimoError;
    private ConstructorFechaHora constructorFechaHora = new ConstructorFechaHora();

    @Override
    public void finalize() {
        cerrarConexion();
    }

    public GestorConexionUnica() {
    }

    public GestorConexionUnica(String driver, String cadenaConexion, String usuario, String password) {
        this(driver, cadenaConexion, usuario, password, false);
    }
    
    public GestorConexionUnica(String driver, String cadenaConexion, String usuario, String password, boolean local) {
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
    public Connection obtenerConexion() throws SQLException {

        if (conexion != null) {
            return conexion;
        }
        conexion = DriverManager.getConnection(cadenaConexion, usuario, password);

        return conexion;
    }

    @Override
    public void liberarConexion(Connection conexion) {
    }

    @Override
    public FechaHora getFechaHora() {
        EjecutorSentenciaSelect ejecutorSentenciaSelect = new EjecutorSentenciaSelect(this);
        try {
            String sql = getFormateadorSql().getSelectConsultaFechaHora();
            return ejecutorSentenciaSelect.obtenerEntidad(sql, constructorFechaHora);
        } catch (Exception ex) {
            ultimoError = ex.getMessage();
            return null;
        }
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
            }
            conexion = null;
        }
    }

    @Override
    public void procesarExcepcion(Exception ex) {
        ultimoError = ex.getMessage();
        if (ex instanceof SQLRecoverableException) {
            GestorLog.error("Se ha producido una excepción SQL recuperable, se cierran las conexiones", ex);
            cerrarConexion();
        }
    }
//
//    @Override
//    public boolean esConexionLocal() {
//        return local;
//    }

    @Override
    public FormateadorSql getFormateadorSql() {
        return CompatibilidadSql.getFormateador(driver);
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public String getUltimoError() {
        return ultimoError;
    }

    public String getCadenaConexion() {
        return cadenaConexion;
    }

    public void setCadenaConexion(String cadenaConexion) {
        this.cadenaConexion = cadenaConexion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
