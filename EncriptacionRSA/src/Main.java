

public class Main {
    public static void main(String[] args) {
        try {
            // Crear claves
            CreaClaves.escribirClaves();

            // Encriptar fichero
            EncriptaFichero.encripta();

            // Desencriptar fichero
            DesencriptaFichero.desencriptar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

