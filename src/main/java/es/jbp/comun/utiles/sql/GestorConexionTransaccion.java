package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.SQLException;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Gestor de conexiones para realizar transacciones.
 * @author jberjano
 */
public class GestorConexionTransaccion implements GestorConexiones {
    private GestorConexiones gestorConexiones;
    private Connection conexion;

    public GestorConexionTransaccion(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
        
    }

    public void iniciarTransaccion() throws SQLException {
        if (conexion == null) {
            conexion = gestorConexiones.obtenerConexion();
        }
        conexion.setAutoCommit(false);
    }
    
    public void commit() throws SQLException {
        if (conexion == null) {
            return;
        }
        conexion.commit();
        conexion.setAutoCommit(true);
        gestorConexiones.liberarConexion(conexion);
        conexion = null;
    }
    
    public void rollback() throws SQLException {
        if (conexion == null) {
            return;
        }
        conexion.rollback();
        conexion.setAutoCommit(true);
        gestorConexiones.liberarConexion(conexion);
        conexion = null;
    }
    
    @Override
    public void inicializar() throws ClassNotFoundException {
        gestorConexiones.inicializar();        
    }
    
    @Override
    public Connection obtenerConexion() throws SQLException {
        if (conexion == null) {
            conexion = gestorConexiones.obtenerConexion();
        }
        conexion.setAutoCommit(false);
        return conexion;
    }

    @Override
    public void liberarConexion(Connection conexion) {        
    }

    @Override
    public FechaHora getFechaHora() {
        return gestorConexiones.getFechaHora();
    }

    @Override
    public FormateadorSql getFormateadorSql() {
        return gestorConexiones.getFormateadorSql();
    }

    @Override
    public String getDriver() {
        return gestorConexiones.getDriver();
    }

    public GestorConexiones getGestorConexiones() {
        return gestorConexiones;
    }

    @Override
    public String getUltimoError() {
        return gestorConexiones.getUltimoError();
    }

	@Override
	public void procesarExcepcion(Exception ex) {
		gestorConexiones.procesarExcepcion(ex);
	}
}
