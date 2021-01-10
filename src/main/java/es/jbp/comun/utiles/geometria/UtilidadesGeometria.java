package es.jbp.comun.utiles.geometria;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase de utilidades geometricas para calculo de puntos, angulos, etc
 *
 * @author Jorge Berjano Perez
 *
 */
public class UtilidadesGeometria {

    private UtilidadesGeometria() {
    }

    /**
     * Devuelve el punto medio de un segmento definido por dos puntos.
     */
    public static Point2D puntoMedio(Point2D punto1, Point2D punto2) {
        Point2D puntoMedio = new Point2D.Double();

        puntoMedio.setLocation((punto1.getX() + punto2.getX()) / 2, (punto1.getY() + punto2.getY()) / 2);
        return puntoMedio;
    }

    /**
     * Devuelve la distancia entre dos puntos dados.
     */
    public static double distancia(Point2D punto1, Point2D punto2) {
        return Math.sqrt(Math.pow(punto2.getX() - punto1.getX(), 2) + Math.pow(punto2.getY() - punto1.getY(), 2));
    }

    /**
     * Alinea un rectangulo con otro rectangulo fijo. Desplaza el rectangulo
     * movil para que uno de sus puntos, definido por una direccion, coincida
     * con uno de los puntos del rectangulo fijo, definido por otra dirección.
     *
     * @param rectanguloMovil El rectangulo que se alinea respecto al otro.
     * @param direccionPuntoMovil Dirección del punto del rectangulo movil que
     * sirve de refecerencia para el ajuste.
     * @param rectanguloFijo El rectangulo al que se alinea el rectangulo movil.
     * @param direccionPuntoFijo Dirección del punto del rectangulo fino al que
     * se ajusta el rectangulo movil.
     */
    public static void alinearRectangulo(Rectangle2D rectanguloMovil, Direccion direccionPuntoMovil,
            Rectangle2D rectanguloFijo, Direccion direccionPuntoFijo) {

        Point2D verticeFijo = puntoDelRectangulo(rectanguloFijo, direccionPuntoFijo);
        Point2D verticeMovil = puntoDelRectangulo(rectanguloMovil, direccionPuntoMovil);

        double dx = verticeFijo.getX() - verticeMovil.getX();
        double dy = verticeFijo.getY() - verticeMovil.getY();

        desplazarRectangulo(rectanguloMovil, dx, dy);
    }

    /**
     * Desplaza un rectangulo.
     * @param rectangulo El rectangulo a desplazar
     * @param dx desplazamiento horizontal
     * @param dy desplazamiento vertical
     */
    public static void desplazarRectangulo(Rectangle2D rectangulo, double dx, double dy) {
        rectangulo.setRect(rectangulo.getX() + dx, rectangulo.getY() + dy, rectangulo.getWidth(), rectangulo.getHeight());
    }

    /**
     * Redimensiona un rectangulo hacia una dirección determinada.
     *
     * @param rectangulo El rectangulo que se ha de redimensionar.
     * @param ancho El nuevo ancho del rectangulo.
     * @param alto El nuevo alto del rectangulo.
     * @param direccion La dirección del vertice o lado que permanece fijo.
     */
    public static void redimensionarRectangulo(Rectangle2D rectangulo, double ancho, double alto, Direccion direccion) {

        double xOriginal = rectangulo.getX();
        double yOriginal = rectangulo.getY();
        double altoOriginal = rectangulo.getHeight();
        double anchoOriginal = rectangulo.getWidth();

        double x = xOriginal, y = yOriginal;

        switch (direccion) {
            case CENTRO:
                x = xOriginal + (anchoOriginal - ancho) / 2;
                y = yOriginal + (altoOriginal - alto) / 2;
                break;

            case NORTE:
                x = xOriginal + (anchoOriginal - ancho) / 2;
                y = yOriginal;
                break;

            case NORESTE:
                x = xOriginal + (anchoOriginal - ancho);
                y = yOriginal;
                break;

            case ESTE:
                x = xOriginal + (anchoOriginal - ancho);
                y = yOriginal + (altoOriginal - alto) / 2;
                break;

            case SURESTE:
                x = xOriginal + (anchoOriginal - ancho);
                y = yOriginal + (altoOriginal - alto);
                break;

            case SUR:
                x = xOriginal + (anchoOriginal - ancho) / 2;
                y = yOriginal + (altoOriginal - alto);
                break;

            case SUROESTE:
                x = xOriginal;
                y = yOriginal + (altoOriginal - alto);
                break;

            case OESTE:
                x = xOriginal;
                y = yOriginal + (altoOriginal - alto) / 2;
                break;

            case NOROESTE:
                x = xOriginal;
                y = yOriginal;
                break;
        }

        rectangulo.setRect(x, y, ancho, alto);
    }

    /**
     * Obtiene un punto del rectangulo determinado por una dirección. Por
     * ejemplo si la direccion es NOROESTE devuelve la esquina superior
     * izquierda y si es NORTE devuelve el punto medio del lado superior.
     *
     * @param rectangulo El rectangulo.
     * @param direccion La dirección del punto.
     * @return El punto del rectangulo.
     */
    public static Point2D puntoDelRectangulo(Rectangle2D rectangulo, Direccion direccion) {
        int signoX = direccion.getSignoX();
        int signoY = direccion.getSignoY();
        double x = rectangulo.getX() + rectangulo.getWidth() * (signoX + 1) / 2;
        double y = rectangulo.getY() + rectangulo.getHeight() * (signoY + 1) / 2;
        return new Point2D.Double(x, y);
    }

    /**
     * Obtiene un rectangulo inscrito en otro rectangulo contenedor que tenga
     * las proporciones dadas por un ancho y un alto.
     *
     * @param rectanguloContenedor El rectangulo donde se inscribe el rectangulo
     * resultante.
     * @param ancho El ancho del rectangulo que se quiere inscribir.
     * @param alto El alto del rectangulo resultante.
     * @return El rectangulo inscrito.
     */
    public static Rectangle2D rectanguloInscrito(Rectangle2D rectanguloContenedor, double ancho, double alto) {

        if (ancho == 0 || alto == 0) {
            return null;
        }

        double anchoContenedor = rectanguloContenedor.getWidth();
        double altoContenedor = rectanguloContenedor.getHeight();

        double altoFinal;
        double anchoFinal;

        double proporcionImagen = (double) alto / (double) ancho;
        double proporcionContenedor = (double) altoContenedor / (double) anchoContenedor;

        double x, y;

        if (proporcionImagen <= proporcionContenedor) {
            anchoFinal = anchoContenedor;
            altoFinal = proporcionImagen * anchoFinal;
            x = 0;
            y = (altoContenedor - altoFinal) / 2.0;

        } else {
            altoFinal = altoContenedor;
            anchoFinal = (1.0 / proporcionImagen) * altoFinal;
            x = ((anchoContenedor - anchoFinal) / 2.0);
            y = 0;
        }
        return new Rectangle2D.Double(x, y, anchoFinal, altoFinal);
    }

    /**
     * Crea un rectangulo determinado por un punto fijo, la situación de dicho
     * punto, el alto y el ancho.
     *
     * @param posicion Posición del punto fijo.
     * @param direccion Situación del punto dijo respecto al rectagulo definida
     * por una dirección.
     * @param ancho El ancho del rectangulo.
     * @param alto El alto del rectangulo.
     * @return El rectangulo creado.
     *
     */
    public static Rectangle2D crearRectangulo(Point2D posicion, Direccion direccion, double ancho, double alto) {
        int signoX = direccion.getSignoX();
        int signoY = direccion.getSignoY();

        double dx = (1 - signoX) / 2.0;
        double dy = (1 - signoY) / 2.0;

        double despX = dx * ancho;
        double despY = dy * alto;

        return new Rectangle2D.Double(posicion.getX() - despX, posicion.getY() - despY, ancho, alto);
    }

}
