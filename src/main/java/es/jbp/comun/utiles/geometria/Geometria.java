package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;

/**
 * Constantes y funciones referentes a geometr#a.
 *
 * @author Jorge Berjano
 */
public class Geometria {
    // Constante para las comparaciones entre números double.
    // Debido a la imprecisión de los numeros reales representados por el
    // tipo double la comparación entre ellos se hace con un márgen de 
    // tolerancia definido por epsilon.
    static private double epsilon = 0.000001;
    
    // No se puede instanciar esta clase
    private Geometria() {
    }

    /**
     * Obtiene la constante para las comparaciones entre n#meros double.
     */
    static public double getEpsilon() {
        return epsilon;
    }

    /**
     * Asigna la constante para las comparaciones entre n#meros double.
     */    
    static public void setEpsilon(double epsilon) {
        Geometria.epsilon = epsilon;
    }
    
    /**
     * Compara dos n#meros reales con un margen de tolerancia defindo por epsilon.
     * @return Devuelve true si la diferencia entre los operandos es menor que epsilon.
     */ 
    static public boolean iguales(double operando1, double operando2) {
        return Math.abs(operando1 - operando2) < epsilon; 
    }    
    
    static public boolean iguales(Point2D p1, Point2D p2) {
        return Geometria.iguales(p1.getX(), p2.getX()) && 
               Geometria.iguales(p1.getY(), p2.getY());
    }

    static public boolean iguales(VectorPolar2D p1, VectorPolar2D p2) {
        return Geometria.iguales(p1.getX(), p2.getX()) &&
               Geometria.iguales(p1.getY(), p2.getY());
    }
}
