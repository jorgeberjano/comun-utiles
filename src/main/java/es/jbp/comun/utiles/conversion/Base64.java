package es.jbp.comun.utiles.conversion;

/**
 * Implementación del algoritmo para codificar cadenas a base 64.
 * @author jberjano
 */
@Deprecated
public class Base64 {
//    private final static String base64chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static String encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
//        if (bytes == null) {
//            return null;
//        }
//        
//	// the result/encoded string, the padding string, and the pad count
//	String resultado = "", padding = "";
//	int i = bytes.length % 3;
//
//	// add a right zero pad to make this string a multiple of 3 characters
//	if (i > 0) {
//	    for (; i < 3; i++) {
//		padding += "=";
//	    }
//	}
//
//	// increment over the length of the string, three characters at a time
//	for (i = 0; i < bytes.length; i += 3) {
//
//	    // Se agregan saltos de linea cada 76 caracteres, de acuerdo con las 
//	    // especificaciones MIME
//	    if (i > 0 && (i / 3 * 4) % 76 == 0) {
//		resultado += "\r\n";
//            }
//
//	    // these three 8-bit (ASCII) characters become one 24-bit number
//	    long n = 0;
//            n |= (bytes[i] << 16) & 0xFF0000;
//            
//            if (i + 1 < bytes.length) {
//                n |= (bytes[i + 1] << 8) & 0x00FF00;
//            }
//            if (i + 2 < bytes.length) {
//                n |= (bytes[i + 2]) & 0x0000FF;
//            }
//
//	    // this 24-bit number gets separated into four 6-bit numbers
//	    int n1 = (int) ((n >> 18) & 0x3F),
//                n2 = (int) ((n >> 12) & 0x3F),
//                n3 = (int) ((n >> 6) & 0x3F),
//                n4 = (int) (n & 0x3F);
//
//	    // those four 6-bit numbers are used as indices into the base64
//	    // character list
//	    resultado += "" + base64chars.charAt(n1) + base64chars.charAt(n2)
//		    + base64chars.charAt(n3) + base64chars.charAt(n4);
//	}
//
//	return resultado.substring(0, resultado.length() - padding.length()) + padding;
    }
    
    public static byte[] decode(String string) {
        return java.util.Base64.getDecoder().decode(string);
//        if (string == null) {
//            return null;
//        }
//
//	// remove/ignore any characters not in the base64 characters list
//	// or the pad character -- particularly newlines
//	string = string.replaceAll("[^" + base64chars + "=]", "");
//
//        String p = "";
//	// replace any incoming padding with a zero pad (the 'A' character is zero)
//        if (string.endsWith("==")) {
//            p = "AA";
//        } else if (string.endsWith("=")) {
//            p ="A";
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//	string = string.substring(0, string.length() - p.length()) + p;
//
//	// increment over the length of this encoded string, four characters at a time
//	for (int i = 0; i < string.length(); i += 4) {
//
//	    // each of these four characters represents a 6-bit index in the
//	    // base64 characters list which, when concatenated, will give the
//	    // 24-bit number for the original 3 characters
//            
//            int n1 = base64chars.indexOf(string.charAt(i));
//            int n2 = base64chars.indexOf(string.charAt(i + 1));
//            int n3 = base64chars.indexOf(string.charAt(i + 2));
//            int n4 = base64chars.indexOf(string.charAt(i + 3));            
//	    int n = (n1 << 18) | (n2 << 12) | (n3 << 6) | n4;
//
//	    // split the 24-bit number into the original three 8-bit (ASCII) characters
//	    out.write((n >> 16) & 0xFF);
//            
//            if (i + 2 >= string.length() - p.length()) {
//                break;
//            }
//            
//            out.write((n >> 8) & 0xFF);
//            
//            if (i + 3 >= string.length() - p.length()) {
//                break;
//            }
//            
//            out.write(n & 0xFF);
//	}
//
//        return out.toByteArray();
    }
}