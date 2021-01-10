package es.jbp.comun.utiles.xml;

import java.util.Stack;

/**
 * Clase para crear conenido XML
 * @author jberjano
 */
public class CreadorXml {

    private StringBuilder stringBuilder = new StringBuilder();
    private String identacion = "";
    private Stack<String> pilaTags = new Stack<String>();
    private boolean abriendoTag = false;
    private String tab = "    ";

    public void setTab(String tab) {
        this.tab = tab;
    }
    
    public String getTab() {
        return tab;
    }

    private String getIdentacion() {
        if (identacion.length() != pilaTags.size() * tab.length()) {
            actualizarIdentacion();
        }
        return identacion;
    }

    private void actualizarIdentacion() {
        StringBuilder builder = new StringBuilder();
        int nivelIdentacion = pilaTags.size();
        for (int i = 0; i < nivelIdentacion; i++) {
            builder.append(tab);
        }
        identacion = builder.toString();
    }

    public void abrirTag(String nombreTag) {

        if (abriendoTag) {
            stringBuilder.append(">\n");
            abriendoTag = false;
        }        
        stringBuilder.append(getIdentacion());

        stringBuilder.append("<");
        stringBuilder.append(nombreTag);
        abriendoTag = true;
        pilaTags.push(nombreTag);
        
    }

    public void atributo(String nombreAtributo, String valor) {
        stringBuilder.append(" ");
        stringBuilder.append(nombreAtributo);
        stringBuilder.append("=\"");
        stringBuilder.append(valor);
        stringBuilder.append("\"");
    }

    public void cerrarTag() {
        String nombreTag = pilaTags.pop();
        if (abriendoTag) {
            stringBuilder.append("/>\n");
            abriendoTag = false;
        } else {
            stringBuilder.append(getIdentacion());
            stringBuilder.append("</");
            stringBuilder.append(nombreTag);
            stringBuilder.append(">\n");
        }
    }

    public String toString() {
        return stringBuilder.toString();
    }
}
