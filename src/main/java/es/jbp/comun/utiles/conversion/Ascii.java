package es.jbp.comun.utiles.conversion;

/**
 * Clase para la conversion de Códigos ascii.
 *
 * @author jberjano
 */
public class Ascii {

    public static final int NUL = 0;
    public static final int SOH = 1;
    public static final int STX = 2;
    public static final int ETX = 3;
    public static final int EOT = 4;
    public static final int ENQ = 5;
    public static final int ACK = 6;
    public static final int BEL = 7;
    public static final int BS = 8;
    public static final int TAB = 9;
    public static final int LF = 10;
    public static final int VT = 11;
    public static final int FF = 12;
    public static final int CR = 13;
    public static final int SO = 14;
    public static final int SI = 15;
    public static final int DLE = 16;
    public static final int DC1 = 17;
    public static final int DC2 = 18;
    public static final int DC3 = 19;
    public static final int DC4 = 20;
    public static final int NAK = 21;
    public static final int SYN = 22;
    public static final int ETB = 23;
    public static final int CAN = 24;
    public static final int EM = 25;
    public static final int SUB = 26;
    public static final int ESC = 27;
    public static final int FS = 28;
    public static final int GS = 29;
    public static final int RS = 30;
    public static final int US = 31;

    private static final String[] codigosAscii = {
        "NUL",
        "SOH",
        "STX",
        "ETX",
        "EOT",
        "ENQ",
        "ACK",
        "BEL",
        "BS",
        "TAB",
        "LF",
        "VT",
        "FF",
        "CR",
        "SO",
        "SI",
        "DLE",
        "DC1",
        "DC2",
        "DC3",
        "DC4",
        "NAK",
        "SYN",
        "ETB",
        "CAN",
        "EM",
        "SUB",
        "ESC",
        "FS",
        "GS",
        "RS",
        "US"};
    
    public static String toString(byte octeto) {
        return octeto >= 0 && octeto < 32 ? codigosAscii[octeto] : String.valueOf((char) octeto);
    }
    
    public static Byte fromString(String str) {
        for (byte i = 0; i < 32; i++) {
            if (codigosAscii[i].equals(str)) {
                return i;
            }
        }
        return null;
    } 
    
    
}
