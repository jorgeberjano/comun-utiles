package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.SQLException;
import es.jbp.comun.utiles.conversion.Propiedades;
import es.jbp.comun.utiles.depuracion.GestorLog;
import es.jbp.comun.utiles.hilos.EjecutorAsincrono;
import es.jbp.comun.utiles.hilos.EjecutorAsincrono.Proceso;
import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;
import es.jbp.comun.utiles.tiempo.FechaHora;

/**
 * Gestor de conexiones que permite trabajar tanto con conexiones a una base de
 * datos remota como una local cuando no hay conexion con la primera.
 *
 * @author jberjano
 */
public class GestorConexionesRemotaLocal implements GestorConexiones {

    protected GestorConexionUnica gestorConexionRemota;
    protected GestorConexiones gestorConexionLocal;
    protected GestorConexiones gestorConexionActual;
    protected boolean conexionRemotaCorrecta = false;
    protected boolean conmutacionAutomatica = true;
    protected boolean primeraComprobacion = true;
    protected int comprobarConexionRemotaCadaMs = 5000;
    protected EjecutorAsincrono comprobadorConexionRemota;
    protected String ultimoError;

    public GestorConexionesRemotaLocal() {
    }

    public void configurar(Propiedades propiedades) {

        crearConexionLocal(
                propiedades.getString("bd.local.driver"),
                propiedades.getString("bd.local.conexion"),
                propiedades.getString("bd.local.usuario"),
                desencriptarClave(propiedades.getString("bd.local.clave")));

        crearConexionRemota(
                propiedades.getString("bd.remota.driver"),
                propiedades.getString("bd.remota.conexion"),
                propiedades.getString("bd.remota.usuario"),
                desencriptarClave(propiedades.getString("bd.remota.clave")));
    }

    public void crearConexionRemota(String driver, String cadenaConexion, String usuario, String clave) {
        gestorConexionRemota = new GestorConexionUnica(
                driver,
                cadenaConexion,
                usuario,
                clave,
                false);
        conexionRemotaCorrecta = true;
    }

    public void crearConexionLocal(String driver, String cadenaConexion, String usuario, String clave) {
        if ("jstels.jdbc.xml.XMLDriver2".equals(driver)) {
            gestorConexionLocal = new GestorConexionesEfimeras(
                    driver,
                    cadenaConexion,
                    usuario,
                    clave,
                    true);
        } else {
            gestorConexionLocal = new GestorConexionUnica(
                    driver,
                    cadenaConexion,
                    usuario,
                    clave,
                    true);
        }
    }

    /**
     * Indica el tiempo entre comprobaciones de conexión remota
     *
     * @param comprobarConexionRemotaCadaMs
     */
    public void setComprobarConexionRemotaCadaMs(int comprobarConexionRemotaCadaMs) {
        this.comprobarConexionRemotaCadaMs = comprobarConexionRemotaCadaMs;
    }

    @Override
    public void inicializar() throws ClassNotFoundException {
        if (gestorConexionRemota != null) {
            gestorConexionRemota.inicializar();
        }
        if (gestorConexionLocal != null) {
            gestorConexionLocal.inicializar();
        }

        if (comprobarConexionRemotaCadaMs > 0) {
            comprobadorConexionRemota = new EjecutorAsincrono();
            comprobadorConexionRemota.setLatencia(comprobarConexionRemotaCadaMs);
            comprobadorConexionRemota.ejecutar(new Proceso() {
                @Override
                public boolean procesar() {
                    pedirHoraConexionRemota();
                    return true; // nunca termina
                }

                public String toSrtring() {
                    return "Comprobador de conexión remota";
                }
            });
        }

        pedirHoraConexionRemota();

        comprobarConexionRemota();
    }

    protected FechaHora pedirHoraConexionRemota() {
        if (gestorConexionRemota == null) {
            return null;
        }
        FechaHora fechaHoraRemota = gestorConexionRemota.getFechaHora();
        boolean ok = fechaHoraRemota != null;
        if (!ok) {
            ultimoError = gestorConexionRemota.getUltimoError();
        }
        setConexionRemotaCorrecta(ok);
        return fechaHoraRemota;
    }

    /**
     * Activa o desactiva la seleccion automatica de conexion
     *
     * @param conmutacionAutomatica
     */
    public void setConmutacionAutomatica(boolean conmutacionAutomatica) {
        this.conmutacionAutomatica = conmutacionAutomatica;
    }

    public void activarModoRemoto() {
        activarGestorConexiones(gestorConexionRemota);
    }

    public void activarModoLocal() {
        activarGestorConexiones(gestorConexionLocal);
    }

    @Override
    public Connection obtenerConexion() throws SQLException {
        if (gestorConexionActual == null) {
            return null;
        }
        return gestorConexionActual.obtenerConexion();
    }

    public Connection obtenerConexionLocal() throws SQLException {
        if (gestorConexionLocal == null) {
            return null;
        }
        return gestorConexionLocal.obtenerConexion();
    }

    public void liberarConexionLocal(Connection conexion) {
        if (gestorConexionLocal != null) {
            gestorConexionLocal.liberarConexion(conexion);
        }
    }

    public Connection obtenerConexionRemota() throws SQLException {
        if (gestorConexionRemota == null) {
            return null;
        }
        return gestorConexionRemota.obtenerConexion();
    }

    public void liberarConexionRemota(Connection conexion) {
        if (gestorConexionRemota != null) {
            gestorConexionRemota.liberarConexion(conexion);
        }
    }

    @Override
    public void liberarConexion(Connection conexion) {
        if (gestorConexionActual == null) {
            return;
        }
        gestorConexionActual.liberarConexion(conexion);
    }

    @Override
    public void procesarExcepcion(Exception ex) {
        if (gestorConexionActual == null) {
            return;
        }
        gestorConexionActual.procesarExcepcion(ex);
    }

    public synchronized void setConexionRemotaCorrecta(boolean b) {
        conexionRemotaCorrecta = b;
    }

    public synchronized boolean hayConexionRemota() {
        return conexionRemotaCorrecta && gestorConexionRemota != null;
    }

    public boolean comprobarConexionRemota() {
        boolean ok = hayConexionRemota();
        if (!ok) {
            if (gestorConexionActual != gestorConexionLocal && conmutacionAutomatica) {
                activarModoLocal();
                if (gestorConexionRemota != null) {
                    GestorLog.error("La conexion remota se ha cerrado. Causa: " + ultimoError, null);
                }
            }
        } else {
            if (gestorConexionActual != gestorConexionRemota && conmutacionAutomatica) {
                activarModoRemoto();
            }
        }

        primeraComprobacion = false;
        return ok;
    }

    @Override
    public boolean esConexionLocal() {
        if (gestorConexionActual == null) {
            return false;
        }
        return gestorConexionActual == gestorConexionLocal;
    }

    @Override
    public FechaHora getFechaHora() {
        if (gestorConexionActual == null) {
            return null;
        }
        return gestorConexionActual.getFechaHora();
    }

    @Override
    public FormateadorSql getFormateadorSql() {
        if (gestorConexionActual == null) {
            return null;
        }
        return gestorConexionActual.getFormateadorSql();
    }

    private void activarGestorConexiones(GestorConexiones gestorConexion) {
        if (gestorConexion == null) {
            return;
        }
        if (gestorConexionActual != gestorConexion) {
            if (gestorConexionActual == gestorConexionRemota && gestorConexionRemota != null) {
                gestorConexionRemota.cerrarConexion();
            }
            gestorConexionActual = gestorConexion;
            //PlantillaSql.setFormateadorSql(getFormateadorSql());
        }
    }

    @Override
    public String getDriver() {
        if (gestorConexionActual == null) {
            return "";
        }
        return gestorConexionActual.getDriver();
    }

    @Override
    public String getUltimoError() {
        return ultimoError;
    }

    public GestorConexiones getGestorConexionLocal() {
        return gestorConexionLocal;
    }

    public GestorConexiones getGestorConexionRemota() {
        return gestorConexionRemota;
    }

    public String desencriptarClave(String clave) {
        return clave;
    }
}
