package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.sql.compatibilidad.CompatibilidadSql;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Implementación de un gestor de conexiones efimeras que se crea y se destruyen
 * a cada petición.
 *
 * @author jberjano
 */
public class GestorConexionesEfimeras implements GestorConexiones {

    private final String driver;
    private final String cadenaConexion;
    private final String usuario;
    private final String password;
    private final boolean local;
    private List<Connection> pool = new ArrayList();
    private String ultimoError;
    private final ConstructorFechaHora constructorFechaHora = new ConstructorFechaHora();

    public GestorConexionesEfimeras(String driver, String cadenaConexion, String usuario, String password, boolean local) {
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
        return DriverManager.getConnection(cadenaConexion, usuario, password);
    }

    @Override
    public synchronized void liberarConexion(Connection conexion) {
        
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
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

    @Override
    public void procesarExcepcion(Exception ex) {
        ultimoError = ex.getMessage();
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
