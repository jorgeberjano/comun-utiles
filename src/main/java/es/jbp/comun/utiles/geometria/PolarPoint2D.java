package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;

/**
 * Punto geométrico expresado en coordenadas polares.
 * @author Jorge Berjano
 */
public class PolarPoint2D extends Point2D {
    private double radio;
    private Angulo angulo = new Angulo();

    public PolarPoint2D() {         
    }
             
    public PolarPoint2D(double radio, Angulo angulo) {
        this.radio = radio;
        this.angulo = angulo;
    }

    @Override
    public Object clone() {
        PolarPoint2D clon = (PolarPoint2D) super.clone();
        clon.angulo = (Angulo) angulo.clone();
        return clon;
    }
    
    
    @Override
    public double getX() {
        return radio * Math.cos(angulo.getRadianes());
    }

    @Override
    public double getY() {
        return radio * -Math.sin(angulo.getRadianes());
    }

    @Override
    public void setLocation(double x, double y) {
        radio = Math.sqrt(x * x + y * y);
        angulo.setRadianes(Math.atan2(y, x));
    }

    public Angulo getAngulo() {
        return angulo;
    }

    public void setAngulo(Angulo angulo) {
        this.angulo = angulo;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }
    
    
}
