package es.jbp.comun.utiles.sql.sentencia;

import es.jbp.comun.utiles.conversion.Conversion;
import es.jbp.comun.utiles.tiempo.FechaAbstracta;

/**
 * Representa una sentencia Sql
 *
 * @author Jorge Berjano
 */
public class SentenciaSql {

    public enum Comando {

        Nulo, Select, Insert, Update, Delete
    };
    private Comando comando;

    private String strTabla = "";
    private String strListaCampos = "";
    private String strListaValores = "";
    private String strListaModificaciones = "";

    private ClausulaBase select;
    private ClausulaBase from;
    private ClausulaBase where;
    private ClausulaBase group;
    private ClausulaBase having;
    private ClausulaBase order;

    public SentenciaSql() {
        comando = Comando.Nulo;
        crearClausulas();
    }

    public SentenciaSql(String sql) {
        comando = Comando.Nulo;
        crearClausulas();
        setSql(sql);
    }

    private void crearClausulas() {
        select = new ClausulaBase("SELECT");
        from = new ClausulaBase("FROM");
        where = new ClausulaBase("WHERE");
        group = new ClausulaBase("GROUP BY");
        having = new ClausulaBase("HAVING");
        order = new ClausulaBase("ORDER BY");
    }

    public void select(String campos) {
        comando = Comando.Select;
        select.agregar(campos, ", ");
    }

    public void from(String tabla) {
        if (Conversion.isBlank(tabla)) {
            return;
        }
        from.agregar(tabla, ", ");
    }

    public void where(String str) {
        if (Conversion.isBlank(str)) {
            return;
        }
        where.agregar(str, " AND ");
    }
    
    public void orderBy(String str) {
        if (Conversion.isBlank(str)) {
            return;
        }
        order.agregar(str, ", ");
    }

    public void orderBy(String campo, boolean descendente) {
        if (Conversion.isBlank(campo)) {
            return;
        }
        order.agregar(campo + (descendente ? " DESC" : ""), ", ");
    }

    public void insert(String tabla) {
        comando = Comando.Insert;
        this.strTabla = tabla;
    }

    public void update(String tabla) {
        comando = Comando.Update;
        this.strTabla = tabla;
    }

    public void delete(String tabla) {
        comando = Comando.Delete;
        this.strTabla = tabla;
    }

//    void agregarLeftJoin(String strTabla, String strOn) {
//        agregarJoin(strTabla, "LEFT", strOn);
//    }
//
//    void agregarRightJoin(
//            String strTabla, String strOn) {
//        agregarJoin(strTabla, "RIGHT", strOn);
//    }
//
//    void agregarInnerJoin(
//            String strTabla, String strOn) {
//        agregarJoin(strTabla, "INNER", strOn);
//    }
//    void agregarJoin(
//            String strTabla, String strDireccion, String strOn) {
//        if (strFrom.isEmpty()) {
//            return;
//        }
//
//        strFrom = "(" + strFrom + ") " + strDireccion + " JOIN " + strTabla + " ON " + strOn;
//    }
    public void setSql(String sql) {
        // TODO: implementar para otro tipo de comandos
        comando = Comando.Select;
        strTabla = "";
        strListaCampos = "";
        strListaValores = "";
        strListaModificaciones = "";

        sql = order.extraer(sql);
        sql = having.extraer(sql);
        sql = group.extraer(sql);
        sql = where.extraer(sql);
        sql = from.extraer(sql);
        sql = select.extraer(sql);
    }

    public String getSql() {
        switch (comando) {
            case Select:
                return getSelectSql();
            case Insert:
                return getInsertSql();
            case Update:
                return getUpdateSql();
            case Delete:
                return getDeleteSql();
            default:
                return "";
        }
    }

    private String getSelectSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(select.toString());
        sql.append(" ");
        sql.append(from.toString());
        sql.append(" ");
        sql.append(where.toString());
        sql.append(" ");
        sql.append(group.toString());
        sql.append(" ");
        sql.append(having.toString());
        sql.append(" ");
        sql.append(order.toString());
        return sql.toString();
    }

    private String getInsertSql() {
        String sql;

        sql = "INSERT INTO " + strTabla
                + " (" + strListaCampos + ") VALUES (" + strListaValores + ")";

        return sql;
    }

    private String getUpdateSql() {
        String sql;

        sql = "UPDATE " + strTabla
                + " SET " + strListaModificaciones
                + " " + where;

        return sql;
    }

    private String getDeleteSql() {
        String sql;

        sql = "DELETE FROM " + strTabla + where;
        return sql;
    }

//    public void asignarLuego(String strCampo) {
//        asignarValorSql(strCampo, "?");
//    }

    public void asignar(String strCampo, Object valor) {

        String strValor = aFormatoSql(valor);
        asignarValorSql(strCampo, "'" + strValor + "'");
    }

    // TODO: esto debe ser dependiente del tipo de base de datos
    public static String aFormatoSql(Object valor) {
        if (valor == null) {
            return "null";
        } else if (valor instanceof Boolean) {
            return (Boolean) valor ? "1" : "0";
        } else if (valor instanceof Integer
                || valor instanceof Long
                || valor instanceof Double
                || valor instanceof Float) {
            return valor.toString();
        } else if (valor instanceof FechaAbstracta) {
            return "'" + valor.toString() + "'";
        } else {
            // Se sustituyen las comillas por dos comillas (codigo de escape)
            return "'" + valor.toString().replace("'", "''") + "'";
        }
    }

    public void asignarNull(String strCampo) {
        asignarValorSql(strCampo, "Null");
    }

    public void asignarValorSql(String strCampo, String strValorSql) {
        if (strCampo.isEmpty()) {
            return;
        }

        if (strValorSql.isEmpty()) {
            strValorSql = "Null";
        }

        strValorSql = strValorSql.trim();
        strCampo = strCampo.trim();
//    if (strCampo.indexOf('[') == -1)
//        strCampo = EncorchetarSql(strCampo);

//        if (strValorSql.compareToIgnoreCase("null") != 0) {
//            enlazarStrings(strFind, " AND ", strCampo + " = " + strValorSql);
//        }
        int nPunto = strCampo.indexOf('.');
        if (nPunto != -1) {
            strCampo = strCampo.substring(nPunto + 1);
        }

        if (comando == Comando.Insert) {
            if (!strListaCampos.isEmpty()) {
                strListaCampos += ", ";
            }
            strListaCampos += strCampo;
            if (!strListaValores.isEmpty()) {
                strListaValores += ", ";
            }
            strListaValores += strValorSql;
        } else if (comando == Comando.Update) {
            if (!strListaModificaciones.isEmpty()) {
                strListaModificaciones += ", ";
            }
            strListaModificaciones += strCampo + " = " + strValorSql;
        }
    }

    public void limpiarValores() {
        strListaCampos = "";
        strListaValores = "";
        strListaModificaciones = "";
    }
    
    public String getSelect() {
        return select.getContenido();
    }
    
    public String getFrom() {
        return from.getContenido();
    }
    
    public String getWhere() {
        return where.getContenido();
    }

    @Override
    public String toString() {
        return getSql();
    }
}
