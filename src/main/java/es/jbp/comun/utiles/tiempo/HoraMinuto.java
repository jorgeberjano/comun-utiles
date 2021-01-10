package es.jbp.comun.utiles.tiempo;

/**
 * Clase que representa una hora y un minuto.
 *
 * @author jberjano
 */
public class HoraMinuto {

    private int hora;
    private int minuto;

    public HoraMinuto() {
    }

    public HoraMinuto(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
        normalizar();
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
        normalizar();
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
        normalizar();
    }

    public int getMinutoAbsoluto() {
        return minuto + hora * 60;
    }

    public int comparar(HoraMinuto hm) {
        return getMinutoAbsoluto() - hm.getMinutoAbsoluto();
    }
    
    public boolean esAnterior(HoraMinuto hm) {
        return comparar(hm) < 0;
    }

    public boolean esPosterior(HoraMinuto hm) {
        return comparar(hm) > 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HoraMinuto other = (HoraMinuto) obj;
        if (this.hora != other.hora) {
            return false;
        }
        if (this.minuto != other.minuto) {
            return false;
        }
        return true;
    }

    private boolean estaNormalizada() {
        return hora >= 0 && hora < 24 && minuto >= 0 && minuto < 60;
    }

    private final void normalizar() {

        if (estaNormalizada()) {
            return;
        }
        if (minuto >= 60 || minuto < 0) {
            hora += minuto / 60;
            minuto %= 60;            
        }
        if (minuto < 0) {
            minuto += 60;
            hora--;
        }
        if (hora > 24 || hora < 0) {
            hora %= 24;
        }
        if (hora < 0) {
            hora += 24;
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hora, minuto);
    }

    public static HoraMinuto sumarMinutos(HoraMinuto horaMinuto, int minutos) {
        HoraMinuto resultado = new HoraMinuto();
        resultado.setHora(horaMinuto.getHora());
        resultado.setMinuto(horaMinuto.getMinuto() + minutos);
        resultado.normalizar();
        return resultado;
    }

}
