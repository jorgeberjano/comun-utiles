package es.jbp.comun.utiles.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.jbp.comun.utiles.sql.esquema.CampoBd;

/**
 * Utilidad para manejo de metadatos de un resultset.
 *
 * @author jberjano
 */
public class MetadatosConsulta {

    private ResultSetMetaData metadata;

    public MetadatosConsulta(ResultSet rs) {
        try {
            this.metadata = rs.getMetaData();
        } catch (SQLException ex) {
        }
    }

    public int getIndice(String nombreCampo, int numeroOcurrencia) throws SQLException {
        if (metadata == null) {
            return -1;
        }

        int coincidencias = 0;
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            if (metadata.getColumnName(i).equals(nombreCampo)) {
                if (++coincidencias == numeroOcurrencia) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getNumeroCampos() throws SQLException {
        return metadata.getColumnCount();
    }

    public List<CampoBd> getListaCampos() throws SQLException {

        List<CampoBd> lista = new ArrayList();
        for (int i = 1; i <= metadata.getColumnCount(); i++) {

            CampoBd campoBd = crearCampoBd(i);
            lista.add(campoBd);

        }
        return lista;
    }
    
    public CampoBd getCampo(int i) throws SQLException {
        if (i <= metadata.getColumnCount()) {
            return crearCampoBd(i);
        } else {
            return null;
        }
    }


    public CampoBd getCampo(String nombreCampo) throws SQLException {
        return getCampo(nombreCampo, 1);
    }

    public CampoBd getCampo(String nombreCampo, int numeroOcurrencia) throws SQLException {
        if (metadata == null) {
            return null;
        }

        int coincidencias = 0;
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            if (metadata.getColumnName(i).equals(nombreCampo)) {
                if (++coincidencias == numeroOcurrencia) {
                    return crearCampoBd(i);
                }
            }
        }
        return null;
    }

    private CampoBd crearCampoBd(int i) throws SQLException {
        CampoBd campoBd = new CampoBd();
        campoBd.setNombre(metadata.getColumnName(i));
        int decimales = metadata.getScale(i);
        campoBd.setDecimales(decimales);
        campoBd.setTamano(metadata.getPrecision(i));
        campoBd.setNoNulo(metadata.isNullable(i) == ResultSetMetaData.columnNoNulls);
        int tipoSql = metadata.getColumnType(i);
        campoBd.setTipoSql(tipoSql);        
        campoBd.setTipoDato(TipoDato.fromSqlType(tipoSql, metadata.getColumnTypeName(i), decimales));
        return campoBd;
    }

    public boolean existeCampo(String nombreSql) throws SQLException {
        return getCampo(nombreSql) != null;
    }

}
