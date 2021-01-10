package es.jbp.comun.utiles.geometria;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Adaptador para la clase GeneralPath.
 * Clase de conveniencia para aplrovechar el código que usaba una implementación obsoleta
 * de la clase PolygonDouble.
 * @author Jorge Berjano
 */
public class PolygonDouble implements Shape {

    private GeneralPath path = new GeneralPath();
    private boolean inicializado = false;

    public PolygonDouble() {        
    }
    
    public PolygonDouble(double[] px, double[] py, int npoints) {
        for (int i = 0; i < npoints; i++) {
            addPoint(px[i], py[i]);
        }
        path.closePath();
        inicializado = true;
    }

    public void addPoint(double x, double y) {
        if (inicializado) {
            path.lineTo((float) x, (float) y);
        } else {
            path.moveTo((float) x, (float) y);
            inicializado = true;
        }
    }
    
    public void close() {
        path.closePath();
    }

    public boolean intersects(Rectangle2D r) {
        return path.intersects(r);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return path.intersects(x, y, w, h);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return path.getPathIterator(at, flatness);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return path.getPathIterator(at);
    }

    public synchronized Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }

    public Rectangle getBounds() {
        return path.getBounds();
    }

    public boolean contains(Rectangle2D r) {
        return path.contains(r);
    }

    public boolean contains(double x, double y, double w, double h) {
        return path.contains(x, y, w, h);
    }

    public boolean contains(Point2D p) {
        return path.contains(p);
    }

    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }
}
