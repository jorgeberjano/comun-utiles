package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.sql.sentencia.SentenciaSql;

/**
 * Ejecutor de sentecias inserción de entidades
 *
 * @author jberjano
 */
public class EjecutorSentenciaInsert extends EjecutorSentenciaGuardado {

    public EjecutorSentenciaInsert(GestorConexiones gestorConexiones) {
        super(gestorConexiones);
    }

    @Override
    public boolean ejecutar() throws Exception {
        valoresRecuperados = null;        
        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            throw new Exception("No hay conexión con la base de datos");
        }

        int filasAfectadas = 0;
        PreparedStatement statement = null;
        try {
            SentenciaSql sentencia = new SentenciaSql();
            sentencia.insert(tabla);

            List<Campo> camposAsignables = new ArrayList<Campo>();
            for (Campo campo : getListaTodosCampos()) {
                String valorSql = getSqlValor(campo);
                sentencia.asignarValorSql(campo.nombre, valorSql);
                if (valorSql.equals("?")) {
                    camposAsignables.add(campo);
                }
            }
            String sql = sentencia.toString();
            traza(sql);
            statement = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (Campo campo : camposAsignables) {
                traza(campo.valor);
                asignarValor(statement, campo.valor);
            }
            filasAfectadas = statement.executeUpdate();
            recuperarValores(statement);
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
            limpiar();
        }
        return filasAfectadas > 0;
    }

}
