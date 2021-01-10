/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jbp.comun.utiles.conversion;

import es.jbp.comun.utiles.conversion.Base64;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author jfjara
 */
public class Base64Test {

    String textos[] = {
        "Ñ",
        "Ññáéíóúüöäëï",
        "administrador:shs12345",
        "hola mundo",
        "hola mund",
        "hola mun",        
        "abcdefghijklmnopqrstuvwxyz01234567890"};

    String[] codificados = {
        "w5E=",
        "w5HDscOhw6nDrcOzw7rDvMO2w6TDq8Ov",
        "YWRtaW5pc3RyYWRvcjpzaHMxMjM0NQ==",
        "aG9sYSBtdW5kbw==",
        "aG9sYSBtdW5k",
        "aG9sYSBtdW4=",        
        "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXowMTIzNDU2Nzg5MA==",
        };

    @Test
    public void testEncode() throws UnsupportedEncodingException {

        for (int i = 0; i < textos.length; i++) {
            String resultado = Base64.encode(textos[i].getBytes("UTF-8"));
            assertEquals(resultado, codificados[i]);
        }

    }
    
    @Test
    public void testDecode() throws UnsupportedEncodingException {

        for (int i = 0; i < textos.length; i++) {
            String resultado = new String(Base64.decode(codificados[i]), "UTF-8");
            
            assertEquals(resultado, textos[i]);
        }

    }

}
