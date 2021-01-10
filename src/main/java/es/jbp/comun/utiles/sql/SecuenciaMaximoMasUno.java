package es.jbp.comun.utiles.sql;

/**
 * Secuencia para la generacion de ids basada en el máximo valor de la 
 * clave primaria numerica que haya en la tabla mas uno.
 * @author jberjano
 */
public class SecuenciaMaximoMasUno extends Secuencia {
    
    @Override
    public String getSelectSql(String nombreTabla, String nombreCampo) {
        StringBuilder valores = new StringBuilder();
        valores.append("SELECT ");        
        String funcionMaximo = formateadorSql.getFucionNVL("MAX(" + nombreCampo + ")", "0");
        valores.append(funcionMaximo);
        valores.append(" + 1 FROM ");
        valores.append(nombreTabla);
        return valores.toString();
    }
    
    @Override
    public String getSql(String nombreTabla, String nombreCampo) {
        return getSelectSql(nombreTabla, nombreCampo);
    }
    
    
}
