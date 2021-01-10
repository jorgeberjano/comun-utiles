package es.jbp.comun.utiles.sql.esquema;

import es.jbp.comun.utiles.sql.TipoDato;
import java.sql.SQLException;
import es.jbp.comun.utiles.sql.AdaptadorResultSet;
import es.jbp.comun.utiles.sql.ConstructorEntidad;
import java.util.List;

/**
 * Contructor de entidades CampoBd.
 *
 * @author jberjano
 */
public class ConstructorCampoBd extends ConstructorEntidad<CampoBd> {
      
    @Override
    protected CampoBd construirEntidad(AdaptadorResultSet rs) throws SQLException {

        CampoBd entidad = new CampoBd();

        List<CampoBd> a = rs.getMetadatos().getListaCampos();
        
        entidad.setNombre(rs.getString("COLUMN_NAME"));
        Integer decimales = rs.getInteger("DECIMAL_DIGITS");
        Integer sqlType = rs.getInteger("DATA_TYPE");
        entidad.setTipoSql(sqlType);
        String typeName = rs.getString("TYPE_NAME");
        entidad.setTipoDato(TipoDato.fromSqlType(sqlType, typeName, decimales));
        
        entidad.setNoNulo(!rs.getBoolean("NULLABLE"));    
        entidad.setTamano(rs.getInteger("COLUMN_SIZE"));
        entidad.setDecimales(decimales);            
        return entidad;
    }

}
