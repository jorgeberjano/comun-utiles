package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Vector dos dimensiones para realizar operaciones geométricas.
 *
 * @author Jorge Berjano
 */
public class Vector2D extends Point2D.Double implements Serializable {
        
    public Vector2D() {
        super();
    }
    
    public Vector2D(double x, double y) {
        super(x, y);
    }
    
    public Vector2D(Point2D p1, Point2D p2) {
        super(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }
    
    public Vector2D(Angulo angulo, double modulo) {
        super(modulo * Math.cos(angulo.getRadianes()), modulo * Math.sin(angulo.getRadianes()));
    }

    @Override
    public String toString() {
        return getAngulo().toString();
    }
    
    public double getModulo() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }
    
    public void setModulo(double modulo) {
        Angulo angulo = getAngulo();
        x = modulo * Math.cos(angulo.getRadianes());
        y = modulo * Math.sin(angulo.getRadianes());
    }
    
    public Vector2D normalizar() {
        double modulo = getModulo();
        return new Vector2D(Math.abs(x / modulo), Math.abs(y / modulo));
    }
    
    public Vector2D invertirSentido() {
        return new Vector2D(-x, -y);
    }
    
    public Point2D trasladarPunto(Point2D p) {
        return new Point2D.Double(getX() + p.getX(), getY() + p.getY());
    }
    
    public void setAngulo(Angulo angulo) {
        double modulo = getModulo();
        x = modulo * Math.cos(angulo.getRadianes());
        y = modulo * Math.sin(angulo.getRadianes());
    }
    
    public Angulo getAngulo() {
        Angulo angulo = new Angulo();        
        angulo.setRadianes(Math.atan2(y, x));
        return angulo;
    }
}
