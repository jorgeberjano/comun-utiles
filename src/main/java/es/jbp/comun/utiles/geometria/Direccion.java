package es.jbp.comun.utiles.geometria;

/**
 * Direcci#n representada por un punto cardinal
 * @author Jorge Berjano
 */
public enum Direccion {
    CENTRO(0, 0),
    NORTE(0, -1),
    NORESTE(1, -1),
    ESTE(1, 0),
    SURESTE(1, 1),
    SUR(0, 1),
    SUROESTE(-1, 1),
    OESTE(-1, 0),
    NOROESTE(-1, -1);

    private int signoX, signoY;

    private Direccion(int signoX, int signoY) {
        this.signoX = signoX;
        this.signoY = signoY;
    }

    /**
     * Obtiene la dirección resultante de la union de dos direcciones dadas.
     * De las direcciones NORTE y ESTE el resultado ser#a NORESTE.
     */
    public static Direccion union(Direccion direccion1, Direccion direccion2) {
        int nuevoSignoX = direccion1.getSignoX() + direccion2.getSignoX();
        int nuevoSignoY = direccion1.getSignoY() + direccion2.getSignoY();
        for (Direccion direccion : values()) {
            if (nuevoSignoX == direccion.signoX && nuevoSignoY == direccion.signoY) {
                return direccion;
            }
        }
        return null;
    }

    public Direccion opuesto() {
        switch (this) {
            case NORTE: return SUR;
            case NORESTE: return SUROESTE;
            case ESTE: return OESTE;
            case SURESTE: return NOROESTE;
            case SUR: return NORTE;
            case SUROESTE: return NORESTE;
            case OESTE: return ESTE;
            case NOROESTE: return SURESTE;
            default: return CENTRO;
        }
    }

    public Direccion opuestoHorizontal() {
        switch (this) {
            case NORESTE: return NOROESTE;
            case ESTE: return OESTE;
            case SURESTE: return SUROESTE;
            case SUROESTE: return SURESTE;
            case OESTE: return ESTE;
            case NOROESTE: return NORESTE;
            default: return null;
        }
    }

    public Direccion opuestoVertical() {
        switch (this) {
            case NORTE: return SUR;
            case NORESTE: return SURESTE;
            case SURESTE: return NORESTE;
            case SUR: return NORTE;
            case SUROESTE: return NOROESTE;
            case NOROESTE: return SUROESTE;
            default: return null;
        }
    }

    public int getSignoX() {
        return signoX;
    }

    public int getSignoY() {
        return signoY;
    }

    public boolean isHorizontal() {
        return signoX != 0 && signoY == 0;
    }

    public boolean isVertical() {
        return signoX == 0 && signoY != 0;
    }

    public static Direccion[] valoresBasicos() {
        Direccion[] basicas = { NORTE, SUR, ESTE, OESTE };
        return basicas;
    }
}
