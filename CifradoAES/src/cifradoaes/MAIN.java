/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifradoaes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author EHef_
 */
public class MAIN {
    byte[] chave = "chave de 16bytes".getBytes();
IvParameterSpec IV = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
String TEST = "TEST";

    public String encriptaAES(String chaveCriptografada) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            System.out.println("MENSAJE: "+chaveCriptografada);
            byte[] mensagem = chaveCriptografada.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(chave, "AES"), this.IV);
            chaveCriptografada = new String(cipher.doFinal(mensagem));
            System.out.println("Mensaje encriptado: "+chaveCriptografada);

            chaveCriptografada = DatatypeConverter.printBase64Binary(chaveCriptografada.getBytes());
            this.TEST = DatatypeConverter.printBase64Binary(TEST.getBytes());
            System.out.println("TEST: "+TEST);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return chaveCriptografada;

    }

    public String descriptografaAES(String chaveCriptografada) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        System.out.println("Mensaje Encriptado CON BASE 64: "+chaveCriptografada);
        byte[] base64decodedBytes = DatatypeConverter.parseBase64Binary(chaveCriptografada);
        this.DATA=new String(DatatypeConverter.parseBase64Binary(this.DATA));
        System.out.println("TEST: "+TEST);
        System.out.println("Mensaje Encriptado: "+new String(base64decodedBytes));
        try {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.chave, "AES") , this.IV);
            byte[] decrypted = cipher.doFinal(base64decodedBytes);
            chaveCriptografada = new String(decrypted);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return chaveCriptografada;
    }
    public static void main(String[] args) throws Exception{
        AESCipher cipher = new AESCipher();
        String mensajeEncriptado = cipher.encriptaAES("mensaje");
        System.out.println("Mensaje encriptado CON BASE 64: "+mensajeEncriptado);
        System.out.println("Mensaje desencriptado: "+cipher.descriptografaAES(mensajeEncriptado));
    }
}
