package es.jbp.comun.utiles.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.jbp.comun.utiles.sql.esquema.CampoBd;
import es.jbp.comun.utiles.sql.esquema.ConstructorCampoBd;
import es.jbp.comun.utiles.sql.esquema.EsquemaBd;
import es.jbp.comun.utiles.sql.esquema.TablaBd;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author jberjano
 */
public class MetadatosBd {

    private DatabaseMetaData metadata;
    private String catalogo;
    private List<String> listaTablas;
    private final Connection conexion;
    private final ConstructorCampoBd constructorCampoBd = new ConstructorCampoBd();

    public MetadatosBd(Connection conexion) {
        this.conexion = conexion;
    }

    public MetadatosBd(Connection conexion, String catalogo) {
        this.conexion = conexion;
        this.catalogo = catalogo;
    }

    public MetadatosBd(Connection conexion, List<String> listaTablas) {
        this.conexion = conexion;
        this.listaTablas = listaTablas;
    }

    private void verificarMetadata() throws SQLException {
        if (metadata == null) {
            metadata = conexion.getMetaData();
        }
    }

    public List<String> getEsquemas() throws Exception {
        verificarMetadata();

        List<String> esquemas = null;
        ResultSet resultset = null;
        try {
            resultset = metadata.getSchemas(catalogo, "%");
            ConstructorString constructor = new ConstructorString("TABLE_SCHEM");
            esquemas = constructor.obtenerListaEntidades(resultset);
        } finally {
            if (resultset != null) {
                resultset.close();
            }
        }
        return esquemas;
    }

    public List<String> getTablas() throws Exception {
        return getTablas("");
    }

    public List<String> getTablas(String esquema) throws Exception {
        verificarMetadata();

        if (listaTablas != null) {
            return listaTablas;
        }
        ResultSet resultset = null;
        try {
            resultset = metadata.getTables(catalogo, esquema, "%", new String[]{"TABLE"});
            ConstructorString constructor = new ConstructorString("TABLE_NAME");
            listaTablas = constructor.obtenerListaEntidades(resultset);
        } finally {
            if (resultset != null) {
                resultset.close();
            }
        }
        return listaTablas;
    }

    public List<CampoBd> getCampos(String esquema, String tabla) throws Exception {
        verificarMetadata();
        
        List<String> clavesPrimarias = null;
        ResultSet rsPk = null;
        try {
            rsPk = metadata.getPrimaryKeys(catalogo, esquema, tabla);
            ConstructorString constructorString = new ConstructorString("COLUMN_NAME");
            clavesPrimarias = constructorString.obtenerListaEntidades(rsPk);
        } finally {
            if (rsPk != null) {
                rsPk.close();
            }
        }
        if (clavesPrimarias == null) {
            return null;
        }

        List<CampoBd> campos = null;
        ResultSet rs = null;
        try {
            rs = metadata.getColumns(catalogo, esquema, tabla, "%");
            campos = constructorCampoBd.obtenerListaEntidades(rs);
            //campos.stream().forEach(campo -> campo.setTabla(tabla));
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        if (campos == null) {
            return null;
        }

        Map<String, CampoBd> mapaCampos = new HashMap<String, CampoBd>();
        for (CampoBd campo : campos) {
            mapaCampos.put(campo.getNombre(), campo);
        }

        for (String clavePrimaria : clavesPrimarias) {
            CampoBd campo = mapaCampos.get(clavePrimaria);
            campo.setClavePrimaria(true);
        }

        return campos;
    }

    public List<CampoBd> getCampos(String sql) throws Exception {
        Statement st = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData rsmetadatos = rs.getMetaData();

        int col = rsmetadatos.getColumnCount();
        //System.out.println("Columnas: " + col);
        List<CampoBd> listaCampos = new ArrayList<>();
        for (int i = 1; i <= col; i++) {
            CampoBd campo = new CampoBd();
            campo.setNombre(rsmetadatos.getColumnName(i));
            Integer sqlType = rsmetadatos.getColumnType(i);
            Integer decimales = rsmetadatos.getScale(i);
            campo.setTipoSql(sqlType);
            String typeName = rsmetadatos.getColumnTypeName(i);
            campo.setTipoDato(TipoDato.fromSqlType(sqlType, typeName, decimales));
            campo.setTamano(rsmetadatos.getPrecision(i));
            campo.setDecimales(rsmetadatos.getScale(i));
            campo.setNoNulo(rsmetadatos.isNullable(i) == ResultSetMetaData.columnNoNulls);
            //campo.setTabla(rsmetadatos.getTableName(i));
            //System.out.println("Tipo de Dato: " + rsmetadatos.getColumnTypeName(i));
            //System.out.println("Pertenece a la tabla: " + rsmetadatos.getTableName(i) + "\n");
            listaCampos.add(campo);
        }
        
        
        return listaCampos;
    }

    public EsquemaBd getEsquemaBd() throws Exception {
        return getEsquemaBd("");
    }

    public EsquemaBd getEsquemaBd(String esquema) throws Exception {

        verificarMetadata();

        EsquemaBd esquemaBd = new EsquemaBd();

        List<String> nombresTablas = getTablas();

        if (nombresTablas == null) {
            return null;
        }

        for (String tabla : nombresTablas) {
            TablaBd tablaBd = new TablaBd();
            tablaBd.setNombre(tabla);
            List<CampoBd> campos = getCampos(esquema, tabla);
            tablaBd.setCampos(campos);
            esquemaBd.agregarTabla(tablaBd);
        }
        return esquemaBd;
    }

}
