import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

class EncriptaFichero implements Constantes {
    public static void encripta() throws Exception {
        // Pedir el fichero a encriptar y el fichero de clave pública a usar
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Indique fichero a encriptar:");
        String fichero_encriptar = teclado.readLine();
        if (!new File(fichero_encriptar).exists()) {
            System.out.println("El fichero " + fichero_encriptar + " no existe");
            return;
        }
        String fichero_encriptado = fichero_encriptar + ".txt.crypto";
        System.out.print("Indique qué fichero tiene la clave pública a usar:");
        String fichero_publica = teclado.readLine();

        // Recuperar la clave pública
        // Se crea un flujo de entrada para leer el archivo que contiene la clave pública
        FileInputStream fis = new FileInputStream(fichero_publica);
        // Se crea un buffer del tamaño del archivo
        byte[] buffer = new byte[fis.available()];
        // Se lee el contenido del archivo y se almacena en el buffer
        fis.read(buffer);
        // Se cierra el flujo de entrada
        fis.close();

        // Se crea una especificación de clave X.509 a partir de los datos en el buffer
        X509EncodedKeySpec clave_publica_spec = new X509EncodedKeySpec(buffer);
        // Se obtiene una instancia de la fábrica de claves RSA
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // Se genera la clave pública utilizando la especificación de clave X.509
        PublicKey clave_publica = kf.generatePublic(clave_publica_spec);
        // Se imprime un mensaje indicando que la clave pública ha sido recuperada
        System.out.println("Clave pública recuperada");

        // Generar el fichero encriptado
        // Se crea un generador de números aleatorios seguro
        SecureRandom sr = new SecureRandom();
        // Se establece una semilla para el generador de números aleatorios seguro usando la fecha actual en milisegundos
        sr.setSeed(new Date().getTime());
        // Se abre un flujo de entrada para leer el archivo que se va a encriptar
        fis = new FileInputStream(fichero_encriptar);
        // Se crea un DataOutputStream para escribir en el archivo encriptado
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fichero_encriptado));

        // 1. Generar una clave de sesión
        System.out.println("Generando clave de sesión...");
        KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
        kg.init(TAMANO_CLAVE_SESION, sr);
        SecretKey clave_sesion = kg.generateKey();

        // 2. Guardar la clave de sesión encriptada en el fichero
        // Se imprime un mensaje indicando que se está guardando la clave de sesión encriptada
        System.out.println("Guardando la clave de sesión encriptada...");
        // Se instancia un cifrador RSA con el algoritmo RSA/ECB/PKCS1Padding
        Cipher cifrador_rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Se inicializa el cifrador en modo de cifrado utilizando la clave pública y el generador de números aleatorios seguro
        cifrador_rsa.init(Cipher.ENCRYPT_MODE, clave_publica, sr);
        // Se encripta la clave de sesión y se almacena en un arreglo de bytes
        byte[] clave_sesion_encriptada = cifrador_rsa.doFinal(clave_sesion.getEncoded());
        // Se escribe la longitud de la clave de sesión encriptada en el archivo
        dos.writeInt(clave_sesion_encriptada.length);
        // Se escribe la clave de sesión encriptada en el archivo
        dos.write(clave_sesion_encriptada);

        // 3. Generar un IV aleatorio
        byte[] IV = new byte[TAMANO_IV_BYTES];
        sr.nextBytes(IV);
        IvParameterSpec iv_spec = new IvParameterSpec(IV);
        dos.write(IV);

        // 4. Guardar los datos encriptados en el fichero
        // Se imprime un mensaje indicando que se están guardando los datos encriptados en el fichero
        System.out.println("Guardando " + fichero_encriptar + " en el fichero encriptado " + fichero_encriptado);
        // Se instancia un cifrador para el fichero con el algoritmo Blowfish/CBC/PKCS5Padding
        Cipher cifrador_fichero = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        // Se inicializa el cifrador en modo de cifrado utilizando la clave de sesión y el vector de inicialización
        cifrador_fichero.init(Cipher.ENCRYPT_MODE, clave_sesion, iv_spec);
        // Se crea un CipherOutputStream para escribir en el archivo encriptado utilizando el cifrador para el fichero
        CipherOutputStream cos = new CipherOutputStream(dos, cifrador_fichero);

        // Se lee un byte del flujo de entrada y se escribe en el CipherOutputStream hasta que se alcance el final del flujo de entrada
        int b = fis.read();
        while (b != -1) {
            cos.write(b);
            b = fis.read();
        }
        fis.close();
        cos.close();
        dos.close();
        System.out.println("Fichero encriptado correctamente");
    }
}