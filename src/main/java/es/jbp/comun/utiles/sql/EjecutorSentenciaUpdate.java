package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.sql.sentencia.SentenciaSql;

/**
 * Ejecutor de sentecias modificacion de entidades
 * @author jberjano
 */
public class EjecutorSentenciaUpdate extends EjecutorSentenciaGuardado {

    private boolean permitirSentenciasSinWhere;
    
    public EjecutorSentenciaUpdate(GestorConexiones gestorConexiones) {
        super(gestorConexiones);
    }

    @Override
    public boolean ejecutar() throws Exception {
        valoresRecuperados = null;
        Connection conexion = gestorConexiones.obtenerConexion();
        if (conexion == null) {
            throw new Exception("No hay conexión con la base de datos");
        }
        if (listaPKs.isEmpty() && !permitirSentenciasSinWhere) {
            throw new Exception("Para guardar una entidad necesita tener una clave primaria");
        }

        int filasAfectadas = 0;
        PreparedStatement statement = null;
        try {
            SentenciaSql sentencia = new SentenciaSql();
            sentencia.update(tabla);
            
            List<Campo> camposAsignables = new ArrayList<Campo>();

            for (Campo campo : listaCampos) {
                String valorSql = getSqlValor(campo);                
                sentencia.asignarValorSql(campo.nombre, valorSql);                
                if (valorSql.equals("?")) {
                    camposAsignables.add(campo);
                }
            }    
            
            for (Campo campo : listaPKs) {
                String valorSql = getSqlValor(campo);
                sentencia.where(campo.nombre + " = " + valorSql);
                if (valorSql.equals("?")) {
                    camposAsignables.add(campo);
                }
            }
            
            String sql = sentencia.toString();
            traza(sql);
            statement = conexion.prepareStatement(sql);

            for (Campo campo : camposAsignables) {
                asignarValor(statement, campo.valor);
                traza(campo.valor);
            }
            filasAfectadas = statement.executeUpdate();
            //recuperarValores(statement);
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

    public void setPermitirSentenciasSinWhere(boolean b) {
        permitirSentenciasSinWhere = b;
    }
}
