package cifrade.des;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.Normalizer;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CifradeDES {

    public static SecureRandom sr = new SecureRandom();

    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /* Retorna un hash MD5 a partir de un texto */
    public static String md5(String txt) {
        return Hash.getHash(txt, "MD5");
    }

    /* Retorna un hash SHA1 a partir de un texto */
    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }

    public static String encriptar(String clave, byte[] iv, String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] encriptado = cipher.doFinal(value.getBytes());
            return DatatypeConverter.printBase64Binary(encriptado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decriptar(String clave, byte[] iv, String encriptado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);

            byte[] dec = cipher.doFinal(DatatypeConverter.parseBase64Binary(encriptado));
            return new String(dec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String cifrar(String ruta, String publicKey, String privateKey, String CKey) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
        RSA rsa = new RSA();
        FileReader f = new FileReader(ruta);
        String cadena = "", mensaje = "";
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            mensaje += cadena;
        }
        b.close();
        f.close();
        String clave = "zapatitosblancos"; // 128 bit
        mensaje = Normalizer.normalize(mensaje, Normalizer.Form.NFD);
        mensaje = mensaje.replaceAll("[,| |.|;|:|'|^|*|+|-|/|1|2|3|4|5|6|7|8|9|0|\n|?|¡|¿|_]", "");
        mensaje = mensaje.replaceAll("[^\\p{ASCII}]", "");
        System.out.println("Mensaje: " + mensaje);
        String vector = "1234567812345678";
        byte[] iv = vector.getBytes();
        String cifrado = encriptar(clave, iv, mensaje);
        FileWriter w = new FileWriter("encrypted.txt");
        BufferedWriter bw = new BufferedWriter(w);
        PrintWriter wr = new PrintWriter(bw);
        String firma = sha1(mensaje);
        rsa.openFromDiskPrivateKey(privateKey);
        firma = rsa.Encrypt(firma, false);
        cifrado += ("/////" + firma);
        wr.write(new String(cifrado));
        wr.close();
        w.close();
        bw.close();
        rsa.openFromDiskPublicKey(publicKey);
        String Key = rsa.Encrypt(clave, true);
        FileWriter w2 = new FileWriter(CKey);
        BufferedWriter bw2 = new BufferedWriter(w2);
        PrintWriter wr2 = new PrintWriter(bw2);
        wr2.write(new String(Key));
        wr2.close();
        w2.close();
        bw2.close();
        return cifrado;
    }

    public static String decifrar(String archivo, String publicKey, String privateKey, String CKey) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSA rsa = new RSA();
        String vector = "1234567812345678";
        byte[] iv = vector.getBytes();
        FileReader f = new FileReader(CKey);
        String cadena = "", claveCifrada = "", mensaje = "", firma = "", claveDecifrada = "", Hash = "";
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            claveCifrada += cadena;
        }
        b.close();
        f.close();
        try {
            rsa.openFromDiskPrivateKey(privateKey);
            claveDecifrada = rsa.Decrypt(claveCifrada, true);
            System.out.println(claveDecifrada);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cadena = "";
        FileReader f2 = new FileReader(archivo);
        BufferedReader b2 = new BufferedReader(f2);
        while ((cadena = b2.readLine()) != null) {
            mensaje += cadena;
        }
        String[] parts = mensaje.split("/////");
        mensaje = parts[0]; // 
        System.out.println(mensaje);
        firma = parts[1]; // 
        System.out.println(firma);
        mensaje += "123";
        /////////////7
        System.out.println(mensaje);
        /////////////77
        try {
            rsa.openFromDiskPublicKey(publicKey);
            firma = rsa.Decrypt(firma, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mensaje = decriptar(claveDecifrada, iv, mensaje);
        System.out.println("Mensaje: " + mensaje);
        Hash = sha1(mensaje);
        System.out.println(Hash);
        System.out.println(firma);
        if (Hash.equals(firma)) {
            return mensaje;
        } else {
            return new String("Error, mensaje corrupto");
        }

    }

    public static void main(String[] args) throws IOException, FileNotFoundException {

        //byte[] iv = new byte[8];
        //sr.nextBytes(iv);
        String mensaje = "CORO\n"
                + "Su libertad, México crea,\n"
                + "surge la Patria, nace la luz;\n"
                + "nos convoca tu voz, Politécnico,\n"
                + "nos conduce tu amor, juventud.\n"
                + "\n"
                + "ESTROFA I\n"
                + "Politécnico, fragua encendida\n"
                + "con la chispa del genio creador\n"
                + "en ti forja su nueva estructura\n"
                + "nuestra noble y pujante nación.\n"
                + "\n"
                + "En la aurora de un día venturoso\n"
                + "te dio vida la Revolución;\n"
                + "una estrella te puso en las manos,\n"
                + "¡que no apague su limpio fulgor!\n"
                + "\n"
                + "ESTROFA II\n"
                + "En dinámico anhelo conjugas\n"
                + "las dos fuerzas de un mundo viril:\n"
                + "es la ciencia crisol de esperanzas\n"
                + "es la técnica, fuerza motriz.\n"
                + "\n"
                + "Guinda y blanco, indómita almena\n"
                + "que defiende tu ardor juvenil,\n"
                + "oriflama en las lides gallardas\n"
                + "en tus manos triunfal banderín.	\n"
                + "\n"
                + "E​STROFA III\n"
                + "Tus brigadas de nítida albura\n"
                + "ciencia augusta, saber bondad,\n"
                + "en su diaria tarea resplandecen\n"
                + "infinita su dádiva ideal.\n"
                + "\n"
                + "Energía que modelas paisajes\n"
                + "insurgente y activo soñar,\n"
                + "un humano concepto sostiene\n"
                + "tu cultura de ser integral.\n"
                + "\n"
                + "ESTROFA IV\n"
                + "Mueve al hombre tu fe constructiva\n"
                + "se oye el ritmo de su despertar,\n"
                + "sinfonía de las urbes fabriles\n"
                + "alma agreste de un himno rural.\n"
                + "\n"
                + "Corazón valeroso y ardiente\n"
                + "que edificas baluarte de paz\n"
                + "solidaria su acción con tus filas\n"
                + "vive el pueblo tu hermosa verdad.";
        try {
            System.out.println(String.format("cifrado: %s", cifrar(new String("himnoPlane.txt"), new String("D:\\RSA\\Erick.key"), new String("Alicia.pri"), new String("CKey.txt"))));
            System.out.println("Descifrado: " + decifrar(new String("encrypted.txt"), new String("Alicia.key"), new String("D:\\RSA\\Erick.pri"), new String("CKey.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
