package es.jbp.comun.utiles.sql;

/**
 *
 * @author jberjano
 */
public class SecuenciaOracle extends Secuencia {
    
    private final String nombreSecuencia;

    public SecuenciaOracle(String nombreSecuencia) {
        this.nombreSecuencia = nombreSecuencia;
    }
    
    @Override
    public String getSql(String nombreTabla, String nombreCampo) {        
        return nombreSecuencia + ".nextval";
    }
    
     @Override
    public String getSelectSql(String nombreTabla, String nombreCampo) {        
        return "SELECT " + getSql(nombreTabla, nombreCampo) + " from DUAL";
    }
}
