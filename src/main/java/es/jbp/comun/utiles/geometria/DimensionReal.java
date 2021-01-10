package es.jbp.comun.utiles.geometria;

import java.awt.geom.Dimension2D;

/**
 * Reperesenta una dimensión ancho-alto implementada con numeros reales de precision doble.
 * Permite la serialización xml.
 * La razón de implementar esta clase es que no la hay (de momento) en las librerias Java.
 *
 * @author Jorge Berjano
 */
public class DimensionReal extends Dimension2D {
    
    private double width;
    private double height;
    
    public DimensionReal() {        
    }
    
    public DimensionReal(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public double getWidth() {
        return width;
    }    

    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getHeight() {
        return height;
    }

    public void setSize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);  
    }
}
