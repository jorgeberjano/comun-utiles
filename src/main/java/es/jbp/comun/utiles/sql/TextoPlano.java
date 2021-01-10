package es.jbp.comun.utiles.sql;

/**
 * Sirve para definir un parametro de texto en la plantilla SQL sin que al
 * siustituir genere las comillas de cadena.
 * @author jberjano
 */
public class TextoPlano {
    private String texto;

    public TextoPlano(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }    
}
