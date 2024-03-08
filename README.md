# **ENCRIPTACIÓN RSA**

_Proyecto para recuperar el tema 5 de encriptación y desencriptacion de ficheros._

### _Qué es RSA ?_ 📋
**RSA** es seguramente el algoritmo de clave pública más conocido, más
probado, y más sencillo que se conoce.
Su nombre se debe a los tres inventores: Ron Rivest, Adi Shamir y Leonard
Adleman.
Este algoritmo se basa en la dificultad de factorizar números grandes, es
decir, sacar los primos (átomos) que componen el número. Se conjetura que
romper el algoritmo es equivalente a factorizar un número grande. El tamaño
de número a factorizar suele ser del orden de 2^512, 2^1024, 2^2048 o 2^4096.
RSA se puede usar tanto para encriptación como para
generar firmas digitales.



### _Funcionamiento_ ⚙️
He hecho un programa que consta de 4 clases fundamentales. La primera se llama **_"CreaClaves"_**, que como su nombre dice, genera las claves publica y privada, guardandolas en ficheros que tu le tienes que proporcionar. En mi caso los guardo en dos ficheros de texto plano (.txt). 

En segundo lugar, está la clase de encriptación llamada **_"EncriptaFichero"_**, que se encarga de encriptar un fichero que nos pide por pantalla, para lo cual nos pide el fichero donde hemos guardado la clave pública. Generando un fichero con el nombre del proporcinado, pero con la extensión ".txt.crypto". 

En tercer lugar, tenemos la clase **_"DesencriptaFichero"_**, la cual desencripta el fichero que la clase anterior ha generado. Para esto nos pide por pantalla el archivo encriptado, el cual tiene que tener la extensión .crypto, y también nos pide el fichero con la clave privada y la contraseña (la cual tiene que ser igual que la que le pusimos al crear la clave privada). Una vez hecho esto, nos genera un fichero con la información desencriptada al cual le pone el mismo nombre que el fichero encriptado solo que le quita la extensión .crypto. (Lo cual nos crearía un archivo ".txt.txt", para que podamos diferenciarlo del original).

Por último tenemos la clase **_"Main"_**, que ejecuta todas las clases anteriores.

### _Ejemplo de ejecución_ ⌨️

![Imagen](https://files.catbox.moe/v45rlp.png)


### _Construido con_ 🛠️
* IntelliJ IDEA 2023.3.2 - Java

### _Fuentes_ 📖
Tanto para la base del código como para los conceptos esenciales e información, me he basado en apuntes proporcionados por el profesor. 
Enlace: https://silo.tips/download/seguridad-criptografia-y-comercio-electronico-con-java
