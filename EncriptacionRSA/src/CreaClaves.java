import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

public class CreaClaves implements Constantes {
    public static void escribirClaves() throws Exception {
        // Generar las claves pública/privada
        // Se instancia un generador de números aleatorios seguro
        SecureRandom sr = new SecureRandom();
        // Se establece una semilla para el generador de números aleatorios seguro usando la fecha actual en milisegundos
        sr.setSeed(new Date().getTime());
        // Se imprime un mensaje indicando que se están generando las claves
        System.out.println("Generando claves...");
        // Se instancia un generador de par de claves RSA
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        // Se inicializa el generador de claves con el tamaño de clave definido en TAMANO_CLAVE_RSA y el generador de números aleatorios seguro
        kpg.initialize(TAMANO_CLAVE_RSA, sr);
        // Se generan las claves pública y privada
        KeyPair par_claves = kpg.generateKeyPair();
        // Se imprime un mensaje indicando que las claves han sido generadas
        System.out.println("Claves generadas");

        // Generar el fichero de la clave pública
        System.out.print("Indique fichero para la clave pública:");
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String fichero_publica = teclado.readLine();
        FileOutputStream fos = new FileOutputStream(fichero_publica);
        fos.write(par_claves.getPublic().getEncoded());
        fos.close();
        System.out.println("Fichero con clave pública generado");

        // Generar el fichero de clave privada
        System.out.print("Indique fichero para la clave privada:");
        String fichero_privada = teclado.readLine();
        System.out.print("La clave privada debe estar encriptada, indique la contraseña con la que encriptarla:");
        char[] password = teclado.readLine().toCharArray();

        // Se crea una especificación de clave PKCS8 a partir de la clave privada
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(par_claves.getPrivate().getEncoded());
        // Se abre un flujo de salida para escribir la clave privada en el archivo especificado
        fos = new FileOutputStream(fichero_privada);
        // Se escribe la clave privada en el archivo
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        // Se cierra el flujo de salida
        fos.close();
        // Se imprime un mensaje indicando que el archivo con la clave privada ha sido generado
        System.out.println("Fichero con clave privada generado");
    }
}