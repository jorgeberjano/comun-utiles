package es.jbp.comun.utiles.sql;

import es.jbp.comun.utiles.sql.compatibilidad.FormateadorSql;

/**
 * Valor Sql en bruto. Se usa el valor tal cual está expresado en la sentencia SQL.
 * @author jorge
 */
public class ValorSqlEnBruto implements ValorSql {
    public String valorSql;

    public ValorSqlEnBruto(String valorSql) {
        this.valorSql = valorSql;
    }
    
    @Override
    public String getValorSql(FormateadorSql formateador) {
        return valorSql;
    }
    
}
