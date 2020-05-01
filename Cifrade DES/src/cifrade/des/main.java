/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrade.des;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

/**
 *
 * @author EHef_
 */
public class main {

    public static void main(String[] args) throws Exception {
        try {
            //Definimos un texto a cifrar
            String ruta = "";
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                ruta = f.getAbsolutePath();
            }
            String str = "", cadena = "";
            FileReader f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                str += cadena;
            }
            b.close();
            //String str = "Este es el texto a cifrar";

            System.out.println("\nTexto a cifrar:");
            System.out.println(str);

            //Instanciamos la clase
            RSA rsa = new RSA();

            //Generamos un par de claves
            //Admite claves de 512, 1024, 2048 y 4096 bits
            
            
           // rsa.genKeyPair(2048);

           // String file_private = "C:\\Users\\EHef_\\Documents\\tmp\\rsa.pri";
            //String file_public = "C:\\Users\\EHef_\\Documents\\tmp\\rsa.key";

            //Las guardamos asi podemos usarlas despues
            //a lo largo del tiempo
           // rsa.saveToDiskPrivateKey("rsa.pri");
           // rsa.saveToDiskPublicKey("rsa.key");
           
            //rsa.openFromDiskPrivateKey("rsa.pri");
            rsa.openFromDiskPublicKey("C:\\Users\\EHef_\\OneDrive\\Datos adjuntos de correo electrónico\\Documentos\\NetBeansProjects\\RSA\\rsa.key");

            //Ciframos y e imprimimos, el texto cifrado
            //es devuelto en la variable secure
            String secure = rsa.Encrypt(str, true);
            // String secure = "1d71okraijb02p62z4sz68lqfvvbb1qepryjii3kvtunjhzq3tj7a1z1wewkwj6bunl94np9782pkiha2z0tgwyco9fdn8r3ppts";

            System.out.println("\nCifrado:");
            System.out.println(secure);
            //Se manda el texto cifrado a un archivo txt

            FileWriter w = new FileWriter("encrypted.txt");
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            wr.write(new String(secure));
            wr.close();
            //Se obtiene el texto cifrado del documento
            String encrypted = "";
            f = new FileReader("encrypted.txt");
            b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                encrypted += cadena;
            }
            b.close();
            //A modo de ejemplo creamos otra clase rsa
            RSA rsa2 = new RSA();

            //A diferencia de la anterior aca no creamos
            //un nuevo par de claves, sino que cargamos
            //el juego de claves que habiamos guadado

            //Le pasamos el texto cifrado (secure) y nos 
            //es devuelto el texto ya descifrado (unsecure) 
            
             rsa2.openFromDiskPrivateKey("rsa.pri");
            //rsa2.openFromDiskPublicKey("rsa.key");
            String unsecure = rsa2.Decrypt(encrypted, true);
            //Imprimimos
            System.out.println("\nDescifrado:");
            System.out.println(unsecure);
            w = new FileWriter("decrypted.txt");
            bw = new BufferedWriter(w);
            PrintWriter wr2 = new PrintWriter(bw);
            wr2.write(new String(unsecure));
            wr2.close();
        
        }catch(Exception e){
           // System.out.println("Algo salió mal, revisa que los archivos que abriste hayan sido los correctos");
           e.printStackTrace();
        }
    }
}
