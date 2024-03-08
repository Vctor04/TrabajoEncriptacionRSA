import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;

public class DesencriptaFichero implements Constantes {
    public static void desencriptar() throws Exception {
        // Pedimos el fichero a desencriptar y fichero de clave privada a usar
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Indique fichero a desencriptar:");
        String fichero_encriptado = teclado.readLine();

        if (!new File(fichero_encriptado).exists()) {
            System.out.println("El fichero " + fichero_encriptado + " no existe");
            return;
        }

        if (!fichero_encriptado.toLowerCase().endsWith(".crypto")) {
            System.out.println("La extensión de los ficheros encriptados debe ser .crypto");
            return;
        }

        String fichero_desencriptado = fichero_encriptado.substring(0, fichero_encriptado.length() - ".crypto".length());
        System.out.print("Indique que fichero tiene la clave privada a usar:");
        String fichero_privada = teclado.readLine();

        System.out.print("Indique la contraseña con que se encriptó el fichero " + fichero_privada + ":");
        char[] password = teclado.readLine().toCharArray();

        // Recuperamos la clave privada
        FileInputStream fis = new FileInputStream(fichero_privada);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        // Se crea una especificación de clave PKCS8 a partir de los datos en el buffer
        PKCS8EncodedKeySpec clave_privada_spec = new PKCS8EncodedKeySpec(buffer);
        // Se obtiene una instancia de la fábrica de claves RSA
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // Se genera la clave privada utilizando la especificación de clave PKCS8
        PrivateKey clave_privada = kf.generatePrivate(clave_privada_spec);
        // Se imprime un mensaje indicando que la clave privada ha sido recuperada
        System.out.println("Clave privada recuperada");

        // Generamos el fichero desencriptado
        DataInputStream dis = new DataInputStream(new FileInputStream(fichero_encriptado));
        FileOutputStream fos = new FileOutputStream(fichero_desencriptado);

        // Leemos y desencriptamos la clave de sesión
        // Lee la longitud de la clave de sesión cifrada del flujo de entrada
        int longitud = dis.readInt();
        // Lee la clave de sesión cifrada del flujo de entrada y la almacena en un arreglo de bytes
        byte[] clave_sesion_cifrada = new byte[longitud];
        dis.readFully(clave_sesion_cifrada);

        // Se instancia un cifrador RSA con el algoritmo RSA/ECB/PKCS1Padding
        Cipher cifrador_rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Se inicializa el cifrador en modo de descifrado utilizando la clave privada
        cifrador_rsa.init(Cipher.DECRYPT_MODE, clave_privada);
        // Se descifra la clave de sesión cifrada y se almacena en un arreglo de bytes
        byte[] clave_sesion_bytes = cifrador_rsa.doFinal(clave_sesion_cifrada);
        // Se crea una clave de sesión a partir de los bytes descifrados utilizando el algoritmo Blowfish
        SecretKey clave_sesion = new SecretKeySpec(clave_sesion_bytes, "Blowfish");

        // Recuperamos el IV
        byte[] IV = new byte[TAMANO_IV_BYTES];
        dis.readFully(IV);
        IvParameterSpec iv_spec = new IvParameterSpec(IV);

        // Desencriptamos y escribimos el fichero desencriptado
        Cipher cifrador_fichero = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cifrador_fichero.init(Cipher.DECRYPT_MODE, clave_sesion, iv_spec);

        // Se crea un CipherInputStream para descifrar el flujo de entrada utilizando el cifrador de archivo
        CipherInputStream cis = new CipherInputStream(dis, cifrador_fichero);
        // Se crea un buffer para almacenar los datos descifrados
        byte[] bufferOut = new byte[1024];
        int count;
        // Se lee del CipherInputStream y se escribe en el flujo de salida
        while ((count = cis.read(bufferOut)) != -1) {
            // Se escribe en el flujo de salida los datos descifrados
            fos.write(bufferOut, 0, count);
        }

        fos.close();
        cis.close();
        System.out.println("Fichero desencriptado correctamente");
    }
}