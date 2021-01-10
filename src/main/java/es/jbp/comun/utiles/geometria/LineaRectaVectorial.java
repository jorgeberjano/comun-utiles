package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Linea recta de longitud infinita definida por la ecuación vectorial:
 *      punto = lambda * direccion + origen
 * Donde:
 *      punto: cualquier punto de la recta
 *      lambda: el parametro
 *      direccion: el vector de dirección
 *      origen: el punto de origen
 *
 * @author Jorge Berjano
 */
public class LineaRectaVectorial implements Serializable {

    private VectorPolar2D direccion;
    private Point2D origen;
    static private final VectorPolar2D vectorNulo = new VectorPolar2D();

    /**
     * Construye la linea recta que pasa por un punto y tiene direcci#n definida
     * por un vector.
     * @param origen El punto de origen
     * @param direccion El vector que define la direcci#n de la linea.
     */
    public LineaRectaVectorial(Point2D origen, VectorPolar2D direccion) {
        this.direccion = direccion;
        this.origen = origen;
    }

    /**
     * Contruye una linea recta a partir de dos puntos.
     */
    public LineaRectaVectorial(Point2D p1, Point2D p2) {
        this.direccion = new VectorPolar2D(p1, p2);
        this.origen = p1;
    }

    @Override
    public String toString() {
        return "Linea " + origen + " " + direccion;
    }



    /**
     * Construye la linea recta que pasa por un punto y tiene dirección definida
     * por un angulo.
     */
    public LineaRectaVectorial(Point2D origen, Angulo angulo) {
        this.origen = origen;
        this.direccion = new VectorPolar2D(angulo, 1);
    }

    /**
     * Obtiene el angulo de la linea recta. El angulo siempre esta entre 0 y 180 grados.
     */
    public Angulo getAngulo() {
        double grados = direccion.getAngulo().getGrados();
        if (grados >= 180) grados -= 180;
        return new Angulo(grados);
    }

    public Angulo calcularMenorAngulo(LineaRectaVectorial otraLinea) {
        // TODO: comprobar que este angulo sea menor que 90
        Angulo a1 = getDireccion().getAngulo().restar(otraLinea.getDireccion().getAngulo());
        Angulo a2 = otraLinea.getDireccion().getAngulo().restar(getDireccion().getAngulo());

        if (a1.getGrados() > 90) {
            a1.setGrados(a1.getGrados() - 90);
        }

        if (a2.getGrados() > 90) {
            a2.setGrados(a2.getGrados() - 90);
        }//        System.out.println("a1=" + a1);
//        System.out.println("a2=" + a2);
        Angulo a = a1.getGrados() < a2.getGrados() ? a1 : a2;
//        System.out.println("a="+a);

        if (a.getGrados() > 90) {
            System.err.println("Grados > 90: " + a);
        }
        return a;
    }

    /**
     * Indica si la linea recta es paralela a otra.
     */
    public boolean esParalela(LineaRectaVectorial otraLinea) {
        VectorPolar2D vectorDireccionNormalizado1 = direccion.getVectorNormalizado();
        VectorPolar2D vectorDireccionNormalizado2 = otraLinea.getDireccion().getVectorNormalizado();
        return Geometria.iguales(vectorDireccionNormalizado1, vectorDireccionNormalizado2) ||
                Geometria.iguales(vectorDireccionNormalizado1, vectorDireccionNormalizado2.getVectorInvertido());
    }

    /**
     * Devuelve el punto donde se corta la linea con otra linea dada o null
     * si las lineas son paralelas.
     *
     * @param otraLinea La otra linea.
     */
    public Point2D interseccion(LineaRectaVectorial otraLinea) {
        VectorPolar2D direccion2 = otraLinea.getDireccion();
        Point2D origen2 = otraLinea.getOrigen();

        double Ox1 = origen.getX();
        double Oy1 = origen.getY();

        double Ox2 = origen2.getX();
        double Oy2 = origen2.getY();

        double Vx1 = direccion.getX();
        double Vy1 = direccion.getY();

        double Vx2 = direccion2.getX();
        double Vy2 = direccion2.getY();

        // En el caso de que alguno de los vectores sea nulo se devuelve null
        if (Geometria.iguales(direccion, vectorNulo) ||
                Geometria.iguales(direccion2, vectorNulo)) {
            return null;
        }

        // En el caso de que sean paralelas se devuelve null
        if (esParalela(otraLinea)) {
            return null;
        }

        // Se resuelve la ecuación vectorial segun el valor de Vx1 y Vx2
        double lambda1;
        double fd; // Factor de despeje

        if (Math.abs(Vy2) > Math.abs(Vx2)) {
            fd = -Vx2 / Vy2;
            lambda1 = ((Ox2 - Ox1) + (Oy2 - Oy1) * fd) / (Vx1 + Vy1 * fd);
        } else {
            fd = -Vy2 / Vx2;
            lambda1 = ((Ox2 - Ox1) * fd + (Oy2 - Oy1)) / (Vx1 * fd + Vy1);
        }

        return new Point2D.Double(lambda1 * Vx1 + Ox1, lambda1 * Vy1 + Oy1);
    }

    public Point2D calcularPuntoDistancia(double distancia) {
        double angulo = direccion.getAngulo().getRadianes();
        double x = origen.getX() + distancia * Math.cos(angulo);
        double y = origen.getY() + distancia * Math.sin(angulo);
        return new Point2D.Double(x, y);
    }

    public VectorPolar2D getDireccion() {
        return direccion;
    }

    public void setDireccion(VectorPolar2D direccion) {
        this.direccion = direccion;
    }

    public Point2D getOrigen() {
        return origen;
    }

    public void setOrigen(Point2D origen) {
        this.origen = origen;
    }
}
