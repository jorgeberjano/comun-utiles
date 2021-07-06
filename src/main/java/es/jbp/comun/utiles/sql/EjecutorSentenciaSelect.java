package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Ejecutor de sentecias select
 *
 * @author jberjano
 */
public class EjecutorSentenciaSelect extends Ejecutor {

    private final GestorConexiones gestorConexiones;

    public EjecutorSentenciaSelect(GestorConexiones gestorConexiones) {
        this.gestorConexiones = gestorConexiones;
    }

    public <T> T obtenerEntidad(String sentencia, ConstructorEntidad<T> constructor) throws Exception {

        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return null;
        }
        traza(sentencia);
        
        T entidad = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conexion.createStatement();
            resultSet = statement.executeQuery(sentencia);
            constructor.setFormateadorSql(gestorConexiones.getFormateadorSql());
            entidad = constructor.obtenerEntidad(resultSet);
            exito();
        } catch (Exception ex) {
            grabarTraza();
            gestorConexiones.procesarExcepcion(ex);
            throw ex;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            gestorConexiones.liberarConexion(conexion);
        }

        return entidad;
    }

    public <T> List<T> obtenerListaEntidades(String sentencia,
            ConstructorEntidad<T> constructor) throws Exception {
        List<T> lista = null;

        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return lista;
        }
        traza(sentencia);

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conexion.createStatement();
            resultSet = statement.executeQuery(sentencia);
            lista = constructor.obtenerListaEntidades(resultSet);
            exito();
        } catch (Exception ex) {
            grabarTraza();
            gestorConexiones.procesarExcepcion(ex);
            throw ex;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            gestorConexiones.liberarConexion(conexion);
        }

        return lista;
    }

    public <T> PaginaEntidades<T> obtenerPaginaEntidades(String sentencia, ConstructorEntidad<T> constructor,
            int indicePrimerElemento, int numeroElementos) throws Exception {
        PaginaEntidades<T> pagina = new PaginaEntidades<>();

        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            return null;
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        traza(sentencia);
        try {
            statement = conexion.prepareStatement(sentencia, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery();
            List<T> lista = constructor.obtenerPaginaEntidades(resultSet, indicePrimerElemento, numeroElementos);
            pagina.setListaEntidades(lista);
            resultSet.last();
            pagina.setNumeroTotalEntidades(resultSet.getRow());
            exito();
        } catch (Exception ex) {
            grabarTraza();
            gestorConexiones.procesarExcepcion(ex);
            throw ex;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            gestorConexiones.liberarConexion(conexion);
        }

        return pagina;
    }
}
