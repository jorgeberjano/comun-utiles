package es.jbp.comun.utiles.tiempo;

/**
 * Clase que representa una hora, minuto y segundo.
 *
 * @author jberjano
 */
public class Hora {

    private int hora;
    private int minuto;
    private int segundo; 

    public Hora() {
        
    }
	
	public Hora(String texto) {
		setTexto(texto);
	}

    public Hora(int hora, int minuto, int segundo) {
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
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

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }
    
    public int getSegundoAbsoluto() {
        return segundo + minuto * 60 + hora * 3600;
    }

    public int comparar(Hora hm) {
        return getSegundoAbsoluto() - hm.getSegundoAbsoluto();
    }

    @Override
    public int hashCode() {
        return getSegundoAbsoluto();
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
        final Hora other = (Hora) obj;
        if (this.hora != other.hora) {
            return false;
        }
        if (this.minuto != other.minuto) {
            return false;
        }
        if (this.segundo != other.segundo) {
            return false;
        }
        return true;
    }

    private boolean estaNormalizada() {
        return hora >= 0 && hora < 24 && minuto >= 0 && minuto < 60 && segundo >= 0 && segundo < 60;
    }

    private final void normalizar() {

        if (estaNormalizada()) {
            return;
        }
        if (segundo >= 60 || segundo < 0) {
            minuto += segundo / 60;
            segundo %= 60;            
        }
        if (segundo < 0) {
            segundo += 60;
            minuto--;
        }
        
        if (minuto >= 60 || minuto < 0) {
            hora += minuto / 60;
            minuto %= 60;            
        }
        if (minuto < 0) {
            minuto += 60;
            hora--;
        }
        if (hora >= 24 || hora < 0) {
            hora %= 24;
        }
        if (hora < 0) {
            hora += 24;
        }
    }
	
	public String getTextoHoraMinuto() {
		return String.format("%02d:%02d", hora, minuto);
	}
	
	public String getTexto() {
		return String.format("%02d:%02d:%02d", hora, minuto, segundo);
	}
	
	public final void setTexto(String texto) {
		String[] partes = texto.split(":");
		hora = extraerEntero(partes, 0);
		minuto = extraerEntero(partes, 1);
		segundo = extraerEntero(partes, 2);
		normalizar();
	}

    @Override
    public String toString() {
        return getTexto();
    }

    public static Hora sumarMinutos(Hora hora, int minutos) {
        Hora resultado = new Hora();
        resultado.setHora(hora.getHora());
        resultado.setMinuto(hora.getMinuto() + minutos);
        resultado.setSegundo(hora.getSegundo());
        return resultado;
    }

	private int extraerEntero(String[] partes, int i) {
		if (i >= partes.length) {
			return 0;
		}		
		return Integer.parseInt(partes[i]);		
	}
}
