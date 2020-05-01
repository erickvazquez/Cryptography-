
package cryptography1;

import java.security.Key;
import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Cryptography1 {

    public static void main(String[] args) throws IOException {
        //File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        File f;
        f = new File("C:\\Users\\EHef_\\Documents\\cifrado.txt");
        FileWriter w = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(w);
        PrintWriter wr = new PrintWriter(bw);
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] plainBytes = Files.readAllBytes(new File("C:\\Users\\EHef_\\Documents\\archivo.txt").toPath());
            // Generate the key first
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Key size
            Key key = keyGen.generateKey();

            // Create Cipher instance and initialize it to encrytion mode
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherBytes = cipher.doFinal(plainBytes);
           String b = encoder.encodeToString(cipherBytes);
            wr.write(b);//escribimos en el archivo
            System.out.println("CIPHER DATA : " + new String(cipherBytes));
            System.out.println("CIPHER DATA BASE 64: " + b);
            wr.close();
            bw.close();
            w.close();
            BufferedReader br1 =  new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ingrese Y si desea continuar: ");
            String respuesta = br1.readLine();
            if(respuesta.equals("Y")){
                fr = new FileReader(new File("C:\\Users\\EHef_\\Documents\\cifrado.txt"));
                br = new BufferedReader(fr);
                String line;
                String texto = "";
                while((line=br.readLine())!=null)
                    texto += line;
            byte[] decodedByteArray = decoder.decode(texto);
            // Reinitialize the Cipher to decryption mode
            cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters());
            byte[] plainBytesDecrypted = cipher.doFinal(decodedByteArray);
            String c = new String(plainBytesDecrypted);
            System.out.println("\nDECRYPTED DATA : " + c);
            br1.close();
            fr.close();
            }
            else{
                System.out.println("ok");
            }

        } catch (Exception e) {
            System.out.println("¡¡El mensaje ha sido modificado!!");
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
