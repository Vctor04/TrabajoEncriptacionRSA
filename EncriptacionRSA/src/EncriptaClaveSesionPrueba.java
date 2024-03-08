/*
 * DESCRIPCION: Programa que encripta una clave de sesión
 * y la vuelve a desencriptar para comprobar su correcto
 * funcionamiento.
 */
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class EncriptaClaveSesionPrueba
{
    public static void main (String args[]) throws Exception
    {
// Empezamos generando una clave secreta de
// sesion para encriptar
        System.out.println(
                "Generando clave de sesion...");
        KeyGenerator generador_clave_sesion =
                KeyGenerator.getInstance("Blowfish");
        generador_clave_sesion.init(128);
        Key clave_sesion =
                generador_clave_sesion.generateKey();
        System.out.println("Clave de sesion generada: "
                + clave_sesion.getAlgorithm());
// Ahora generamos una pareja de claves
// RSA de 1024 bits
        KeyPairGenerator generador_clave_asimetrica =
                KeyPairGenerator.getInstance("RSA");
        generador_clave_asimetrica.initialize(1024);
        KeyPair par_claves =
                generador_clave_asimetrica.generateKeyPair();
        System.out.println("Generada clave asimetrica.");
// Creamos el cifrador
        Cipher cifrador =
                Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cifrador.init(Cipher.ENCRYPT_MODE
                ,par_claves.getPublic());
// Encriptamos la clave secreta
        byte[] bytes_clave_sesion =
                clave_sesion.getEncoded();
        byte[] clave_sesion_cifrada =
                cifrador.doFinal(bytes_clave_sesion);
// La volvemos a desencriptar
        cifrador.init(Cipher.DECRYPT_MODE
                ,par_claves.getPrivate());
        byte[] clave_descifrada =
                cifrador.doFinal(clave_sesion_cifrada);
        Key clave_sesion_descifrada =
                new SecretKeySpec(clave_descifrada,"Blowfish");
        if (clave_sesion.equals(clave_sesion_descifrada))
            System.out.println("Operacion correcta");
        else
            System.out.println("Algo fue mal");
    }
}