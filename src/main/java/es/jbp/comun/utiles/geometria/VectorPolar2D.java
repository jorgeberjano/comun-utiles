package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;

/**
 *
 * @author Jorge
 */
public class VectorPolar2D implements Cloneable {

    private Angulo angulo;
    private double modulo;

    public VectorPolar2D() {
        angulo = new Angulo();
    }

    public VectorPolar2D(Angulo angulo, double modulo) {
        this.angulo = angulo;
        this.modulo = modulo;
    }

    public VectorPolar2D(double dx, double dy) {
        this();
        modulo = Math.sqrt(dx * dx + dy * dy);
        angulo.setRadianes(Math.atan2(dy, dx));
    }

    public VectorPolar2D(Point2D p1, Point2D p2) {
       this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    @Override
    public String toString() {
        return "Vector " + modulo + " " +  getAngulo().toString();
    }

    @Override
    public Object clone() {
        VectorPolar2D clon;
        try {
            clon = (VectorPolar2D) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
        clon.angulo = (Angulo) angulo.clone();
        return clon;
    }



    public Point2D trasladarPunto(Point2D p) {
        return new Point2D.Double(getX() + p.getX(), getY() + p.getY());
    }

    public double getModulo() {
        return modulo;
    }

    public void setModulo(double modulo) {
        this.modulo = modulo;
    }

    public void setAngulo(Angulo angulo) {
        this.angulo = angulo;
    }

    public Angulo getAngulo() {
        return angulo;
    }

    public double getX() {
        return modulo * Math.cos(angulo.getRadianes());
    }

    public double getY() {
        return modulo * Math.sin(angulo.getRadianes());
    }

    public VectorPolar2D getVectorNormalizado() {
        return new VectorPolar2D(angulo, 1);
    }

    public VectorPolar2D getVectorInvertido() {
        return new VectorPolar2D(angulo.sumar(new Angulo(180)), modulo);
    }
}
