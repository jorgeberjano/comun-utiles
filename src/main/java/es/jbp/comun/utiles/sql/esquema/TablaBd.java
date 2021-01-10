package es.jbp.comun.utiles.sql.esquema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jberjano
 */
public class TablaBd {
    private String nombre;
    private List<CampoBd> campos = new ArrayList<CampoBd>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<CampoBd> getCampos() {
        return campos;
    }

    public void setCampos(List<CampoBd> campos) {
        this.campos = campos;
    }
    
    public void agregarCampo(CampoBd campo) {
        campos.add(campo);
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    public String traza() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tabla: ");
        builder.append(nombre);
        builder.append("\n");
        for (CampoBd campo : campos) {
            builder.append(campo.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public CampoBd getCampo(String nombre) {
        for (CampoBd campo : campos) {
            if (campo.getNombre().compareToIgnoreCase(nombre) == 0) {
                return campo;
            }
        }
        return null;
    }
}
