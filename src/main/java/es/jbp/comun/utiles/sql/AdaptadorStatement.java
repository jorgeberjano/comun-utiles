package es.jbp.comun.utiles.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import es.jbp.comun.utiles.tiempo.FechaAbstracta;

/**
 * Adaptador para un statement con funciones para asignar valores mediante
 * conversion automatica de tipos.
 *
 * @author jberjano
 */
public class AdaptadorStatement {

    private final PreparedStatement statement;
    private int numeroOcurrencia = 1;

    public AdaptadorStatement(PreparedStatement resultSet) {
        this.statement = resultSet;
    }

    public void setValor(int indice, Object valor, Integer tipoSql) throws SQLException {
        if (valor == null) {
            statement.setNull(indice, tipoSql);
        } else if (valor instanceof FechaAbstracta) {
            statement.setObject(indice, ((FechaAbstracta) valor).getTimestamp(), tipoSql);
        } else {
            statement.setObject(indice, valor, tipoSql);
        }
    }  

    public boolean execute() throws SQLException {
        return statement.execute();
    }

    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }
}
