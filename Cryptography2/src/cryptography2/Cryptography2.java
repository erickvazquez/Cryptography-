
package cryptography2;

import java.security.Key;
import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Cryptography2 {

    public static void main(String[] args) throws IOException {
        //File archivo = null;
        FileReader fr = null;
        //BufferedReader br = null;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] cipherBytes = Files.readAllBytes(new File("C:\\Users\\EHef_\\Documents\\cifrado.txt").toPath());
            // Generate the key first
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Key size
            Key key = keyGen.generateKey();

            // Create Cipher instance and initialize it to encrytion mode
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
            //cipher.init(Cipher.ENCRYPT_MODE, key);
            //byte[] cipherBytes = cipher.doFinal(plainBytes);
            /*String b = encoder.encodeToString(new String(cipherBytes).getBytes(StandardCharsets.UTF_8));//Convertimos a base 64
            wr.write(b);//escribimos en el archivo
            System.out.println("CIPHER DATA : " + b);
            // Reinitialize the Cipher to decryption mode*/
            cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters());
            //byte[] plainBytesDecrypted = cipher.doFinal(cipherBytes);
            byte[] decodedByteArray = decoder.decode(new String(cipherBytes));
            byte[] plainBytesDecrypted = cipher.doFinal(decodedByteArray);
            String b = new String(plainBytesDecrypted);
            System.out.println("\nDECRYPTED DATA : " + b);

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
