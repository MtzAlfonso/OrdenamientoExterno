package ordenamientoExterno;

import java.io.*;

/*Todas las funciones en esta clase serán estaticas para ue puedan ser accesadas por el metodo EjecucionMe
sin nececidad de la creación de un objeto para accesar a ellas
 */
/**
 * Ordenamiento por mezcla equilibrada.
 *
 * @author Alfredo Araiza Granados
 * @version 5.0.4
 * @since 30/septiembre/2019
 */
public class MezclaEquilibrada {

    /**
     * Carpeta para almacenar archivos auxiliares.
     */
    public File directorio;
    /**
     * Archivos auxiliares. Se crean los archivos auxiliares f0,f1 y f2 para
     * guardar las iteraciones, estos elementos son de clase esto para poder
     * modificarlos en todos los procesos siguientes Serán creados dentro del
     * paquete o carpeta de mezcla equilibrada
     */
    public File f1, f2, f0;
    /**
     * Ruta del Archivo Original. Almacena la ruta del archivo original
     */
    public String original;

    /**
     * Contructor Mezcla. Crea los directorios necesarios e inicializa los
     * archivos
     *
     * @param ruta Se lee desde la clase principal del proyecto.
     */
    public MezclaEquilibrada(String ruta) {
        directorio = new File("./archivos");
        directorio.mkdir();
        directorio = new File("./archivos/mezcla");
        directorio.mkdir();
        original = ruta;
        f1 = new File("./archivos/mezcla/F1.txt");
        f2 = new File("./archivos/mezcla/F2.txt");
        f0 = new File("./archivos/mezcla/F0.txt");
        
        if (f0.exists())
            f0.delete();
        if (f1.exists())
            f1.delete();
        if (f2.exists())
            f2.delete();
    }

    /**
     * Método de ejecucion principal. Realiza todos los procesos, este metodo es
     * el unico que se tiene que llamar a la clase principal
     *
     * @param op Para elegir entre Mezcla Ascendente o Descendente
     * @throws java.io.IOException En caso de no poder leer o escribir
     */
    public void EjecucionME(int op) throws IOException {
        if (op == 1) {
            boolean verificar = true;
            // verificar es una variable booleana que cambiará de valor si ya terminaron todas las iteraciones de ordenación 
            CopiaOriginal();
            // Se hace una copia de el Archivo original a f0

            AsBloquesF1F2();
            // Se crean bloques de secuencias ordenadas en F1 y F2  
            verificar = AsMergeEnF0();
            /* Se hace una mezcla en el archivo auxiliar f0 de los bloques ordenados de F1 y F2, merge en F0 regresa un booleano, para verificar si ya se encuentra ordenado,
        para evitar hacer más iteraciones sin sentido, si desde la primera mezcla se encuentra ordenado no entrará al while siguiente
             */
            // Se comprueba que verificar tenga valor de true para entrar al ciclo
            while (verificar) {

                if (verificar) { // Aqui se vuelven a realizar las mismas operaciones de crear Bloques en F1 y F2 y Mezclar en F0
                    AsBloquesF1F2();
                    verificar = AsMergeEnF0();
                } else { // En el caso de que verificar se encuentre ordenado verificar será falso se detiene el while con el break que se encuentra dentro del else
                    break;

                }
            }
            // Si se sale del ciclo solo se termina significa que ya se encuentra ordenado, y se imprime lo siguiente:
            System.out.println("Arreglo ordenado archivos/mezcla/Ordenado.txt");
            System.out.println("Iteraciones en F0,F1 y F2");

            ArchivoOrdenado();
            // Se hace una copia de la ultima linea de f0 en el Archivo Final Ordenado
        } else {
            boolean verificar = true;

            CopiaOriginal();

            DesBloquesF1F2();

            verificar = DesMergeEnF0();

            while (verificar) {

                if (verificar) {
                    DesBloquesF1F2();
                    verificar = DesMergeEnF0();
                } else {
                    break;

                }
            }
            System.out.println("Arreglo Ordenado en el Archivo txt Ordenado");
            System.out.println("Iteraciones en F0,F1 y F2");

            ArchivoOrdenado();
        }
    }

    /**
     * Realiza una Copia del Archivo Original en F0.
     *
     * @throws FileNotFoundException En caso de no encontrar un Archivo
     */
    public void CopiaOriginal() throws FileNotFoundException {
        InputStream input; // Se inicializa InputStream que nos dará el contenido que se escribirá
        OutputStream output;// OutputStream permitirá la escritura de lo encontrado en input
        try {
            input = new FileInputStream(original);
            //Se toma el contenido de el archivo original
            output = new FileOutputStream(f0);
            //Se utiliza el lugar para escribir el contenido en output;
            byte[] buffer = new byte[1024];// Se toma un espacio de almacenamiento para almacenar lo que se leerá de input
            int length;
            /*Un entero que se utiliza para comprobar si el tamaño de lo leído en input es mayor a cero o tiene contenido
            así para escribir el contenido que tenga buffer en f0,*/
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Se cierran input y output para ya no utilizarlos  en memoria
            input.close();
            output.close();
        } catch (IOException e) {
            //Se utiliza este catch proveniente de un try para obtener la excepción de que lo que se manda a input no existe
        }
    }

    /**
     * Genera bloques de secuencias ordenadas en F1 y F2.
     *
     * @throws FileNotFoundException En caso de no encontrar un archivo
     * @throws IOException En caso de no poder leer o escribir
     */
    public void AsBloquesF1F2() throws FileNotFoundException, IOException {
        FileReader f = new FileReader(f0);

        BufferedReader br = new BufferedReader(f);
        //Se lee f0 para obtener la info de este archivo

        //Se utiliza el siguiente bloque de código hasta el final de el while que contiene a linea 1, para encontrar la ultima linea del archivo
        String linea1 = "";
        String linea2 = br.readLine();// Lee la primerra linea

        while (linea1 != null) {//Nunca será nula por que se incializa con un valor
            linea1 = br.readLine();//Lee la segunda linea
            if (linea1 != null) {//Comprueba que la segunda linea no sea nula o tenga valores
                linea2 = linea1;//Se le da linea1 a linea 2 pero si el if no se cumple entonces se queda en la linea actual ya que esa es la ultima en si
            }
        }
        // El anterior while se repite hasta encontrar la ultima linea

        //Se hace un split de la primera linea de f0 con un separador ","
        String[] arreglo = linea2.split(",");
        BufferedWriter archivo1 = new BufferedWriter(new FileWriter(f1, true));
        BufferedWriter archivo2 = new BufferedWriter(new FileWriter(f2, true));
        //Se inicializan FileWriter y BufferedWriter para escribir en f1 y f2 se le manda true para que no haya sobreescritura

        int pos1 = 0, pos2;// Variables para recorrer el arreglo que contiene los valores de una linea en F0

        while (pos1 < arreglo.length) {//Pos1 aumentará cuando sea igual a arreglo.length el ciclo se detiene. 
            do {
                archivo1.write(arreglo[pos1]);// Se agrega el primer valor a f1 seguido de una coma
                archivo1.write(",");
                pos1++;
            } while (pos1 < arreglo.length && Float.parseFloat(arreglo[pos1 - 1]) <= Float.parseFloat(arreglo[pos1]));
            // Esto se realizará mientras la posición anterior sea menor a la actual. Para esto se hace una conversión a float de la pos1 y su anterior y se comparán
            // Ya que estos valores son strings

            //Se escribe un apostrofe para indicar el fin de un bloque seguido de una coma en f1
            archivo1.write("'");
            archivo1.write(",");
            //Si la pos1 en este momento llego a ser mayor a arreglo.length el break detiene el ciclo
            if (pos1 == arreglo.length) {
                break;
            }

            //Si esto no sucede entonces se le da a pos2 el valor de pos1 para seguir en esa posición en el arreglo
            pos2 = pos1;

            //Se realizan los mismos pasos anteriores pero para f2
            do {
                archivo2.write(arreglo[pos2]);
                archivo2.write(",");
                pos2++;
            } while (pos2 < arreglo.length && Float.parseFloat(arreglo[pos2 - 1]) <= Float.parseFloat(arreglo[pos2]));

            archivo2.write("'");
            archivo2.write(",");
            pos1 = pos2;//Se le da a pos1 el valor de pos2 para que en la siguiente iteración se mantenga en la posición arctual del arreglo 
        }

        //Se crea una nueva linea en f1 y f2    
        archivo1.newLine();
        archivo2.newLine();
        //Se cierran ambos archivos para no mantenerlos en memoria y además se realize la escritura
        archivo1.close();
        archivo2.close();
        //Se cierra la lectura de f0
        br.close();

    }

    /**
     * Mezcla de bloques en F0.
     *
     * @return Regresa <code>false</code> si el ordenamiento se completo y
     * <code>true</code> si continua verficando.
     * @throws FileNotFoundException En caso de no encontrar el archivo
     * @throws IOException En caso de no poder leer o escribir
     */
    public boolean AsMergeEnF0() throws FileNotFoundException, IOException {
        //La variable verificar indica si el texto del archivo original ya se encuentra ordenado
        boolean verificar = true;
        int i = 0, j = 0;
        //Se realiza la lectura de f1 y f2
        FileReader archivo1 = new FileReader(f1);
        BufferedReader b1 = new BufferedReader(archivo1);
        FileReader archivo2 = new FileReader(f2);
        BufferedReader b2 = new BufferedReader(archivo2);

        //Se inicializa la escritura para f0 y se manda true en FileWriter para que no haya sobreescritura
        BufferedWriter archivo0 = new BufferedWriter(new FileWriter(f0, true));
        //Se crea nueva linea en f0
        archivo0.newLine();

        //Este proceso es el mismo que en Bloques para f0 se utiliza para llegar a la ultima linea de un archivo y leerla 
        //aqui se hace para f1 y f2
        String cadaux = "";
        String cadaux2 = "";
        String cadena1 = b1.readLine();
        String cadena2 = b2.readLine();

        while (cadaux != null) {
            cadaux = b1.readLine();
            cadaux2 = b2.readLine();
            if (cadaux != null) {
                cadena1 = cadaux;
                cadena2 = cadaux2;
            }
        }

        //Si cualquiera de los dos archivos en su utilima linea se encuentra vacío se detiene este método porque realiza un return
        if ("".equals(cadena1) || "".equals(cadena2)) {
            //Se cierra la lectura de f1 y f2, y se cierra la escritura de f0
            b1.close();
            b2.close();
            archivo0.close();
            //Se cambia el valor de verificar, y se retorna
            verificar = false;
            return verificar;
        }

        //Ahora se toman 2 arreglos que contendrán la info de f1 y f2 se separan de "," con split 
        String[] arreglo = cadena1.split(",");// arreglo para f1
        String[] arr = cadena2.split(",");//arreglo para f2

        //i y j se utilizan para recorrer los valores de f1 y f2 si llegan a ser mayores a cualqueira de sus arreglos se detiene el while
        while (i < arreglo.length && j < arr.length) {
            //Compará si la posición en i y en j en el arreglo es igual a un apostrofe significando que ya terminaron un bloque
            while (!"'".equals(arreglo[i]) && !"'".equals(arr[j])) {
                //Se hace una conversión de los elementos de los arreglos a float para compararlos ya que estos valores son Strings en los arreglos
                if (Float.parseFloat(arreglo[i]) <= Float.parseFloat(arr[j])) {// Se comprueba que el valor de f1 sea menor o igual al de f2
                    archivo0.write(arreglo[i]);//Se escribe el valor de f1 en f0
                    archivo0.write(",");//Seguido de una coma
                    i++;
                } else if (Float.parseFloat(arreglo[i]) > Float.parseFloat(arr[j])) {//En otro caso si el valor de f1 es mayor al de f2
                    archivo0.write(arr[j]);//Se escribe el valor de f2 en f0 seguido de una coma
                    archivo0.write(",");
                    j++;
                }
            }
            //Se comprueba si el valor de la posición de i es distinta a un apostrofe o no  termino de escribir su bloque en f0
            if (!"'".equals(arreglo[i])) {//Si es distinto el valor de f1 en la posición i del arreglo a "'" haz lo siquiente
                while (!"'".equals(arreglo[i])) {//Se escribe el resto de valores del bloque hasta llegar a un apostrofe
                    archivo0.write(arreglo[i]);
                    archivo0.write(",");
                    i++;
                }
            }
            //Se comprueba si el bloque no terminado este en f2, o aún no se llega al apostrofe en el arreglo de f2
            if (!"'".equals(arr[j])) {//Si es distinto el valor de f2 en la posición j del arreglo a "'" haz lo siquiente
                while (!"'".equals(arr[j])) {//Se escribe el resto de valores del bloque hasta llegar a un apostrofe
                    archivo0.write(arr[j]);
                    archivo0.write(",");
                    j++;
                }
            }
            i++;
            j++;
        }
        //Se comprueba si "i" incremento hasta llegar al final del arreglo que contiene la info de f1
        if (i == arreglo.length) {
            while (j < arr.length && !"'".equals(arr[j])) {// También se comprueba que "j" no haya llegado al final de la linea de f2
                //Se escribirá el resto de información contenida en arr(info de f2) en f0 
                archivo0.write(arr[j]);
                archivo0.write(",");
                j++;
            }
        } //Se comprueba si "j" incremento hasta llegar al final del arreglo de f2
        else if (j == arr.length) {
            while (i < arreglo.length && !"'".equals(arreglo[i])) {// También se comprueba que "i" no haya llegado al final del arreglo de f1
                //Se escribirá el resto de información contenida en arreglo(info de f1) en f0
                archivo0.write(arreglo[i]);
                archivo0.write(",");
                i++;
            }
        }
        //Se cierra la lectura de f1 y f2 y la escritura de f0
        b1.close();
        b2.close();
        archivo0.close();
        //Se retorna verificar
        return verificar;
    }

    /**
     * Genera Archivo Ordenado a partir de la ultima linea de F0.
     * @throws FileNotFoundException
     * @throws IOException En caso de no poder leer o escribir
     */
    public void ArchivoOrdenado() throws FileNotFoundException, IOException {
        File ordenado = new File("./archivos/mezcla/Ordenado.txt");
        //Se crea un nuevo archivo llamado Ordenado en la carpeta de mezcla equilibrada
        FileReader f = new FileReader(f0);
        BufferedReader br = new BufferedReader(f);
        //Se crea la lectura del archivo F0 y para esto se utiliza br

        //La misma lógica ya mencionada se va a la ultima linea de f0 
        String linea1 = "";
        String linea2 = br.readLine();
        BufferedWriter archivo = new BufferedWriter(new FileWriter(ordenado));
        //BufferedWriter para poder realizar la escritura en el archivo creado ordenado
        while (linea1 != null) {
            linea1 = br.readLine();
            if (linea1 != null) {
                linea2 = linea1;
            }
        }
        //Se escribe la ultima linea de f0 en Ordenado
        archivo.write(linea2);
        //Se cierra la escritura de Ordenado y la Lecttura de f0
        archivo.close();
        br.close();

    }

    /**
     * Genera bloques de secuencias ordenadas en F1 y F2.
     * @throws FileNotFoundException
     * @throws IOException En caso de no poder leer o escribir
     */
    public void DesBloquesF1F2() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(f0));
        String linea1 = "";
        String linea2 = br.readLine();

        while (linea1 != null) {
            linea1 = br.readLine();
            if (linea1 != null) {
                linea2 = linea1;
            }
        }

        String[] arreglo = linea2.split(",");
        BufferedWriter archivo1 = new BufferedWriter(new FileWriter(f1, true));
        BufferedWriter archivo2 = new BufferedWriter(new FileWriter(f2, true));
        int pos1 = 0, pos2;

        while (pos1 < arreglo.length) {
            do {
                archivo1.write(arreglo[pos1]);
                archivo1.write(",");
                pos1++;
            } while (pos1 < arreglo.length && Float.parseFloat(arreglo[pos1 - 1]) >= Float.parseFloat(arreglo[pos1]));
            //Se comprueba que sea mayor o igual el valor de pos1 anterior con el actual

            archivo1.write("'");
            archivo1.write(",");

            if (pos1 == arreglo.length) {
                break;
            }
            pos2 = pos1;

            do {
                archivo2.write(arreglo[pos2]);
                archivo2.write(",");
                pos2++;
            } while (pos2 < arreglo.length && Float.parseFloat(arreglo[pos2 - 1]) >= Float.parseFloat(arreglo[pos2]));
            //Se comprueba que sea mayor o igual el valor de pos2 anterior con el actual
            archivo2.write("'");
            archivo2.write(",");
            pos1 = pos2;
        }

        archivo1.newLine();
        archivo2.newLine();
        archivo1.close();
        archivo2.close();
        br.close();

    }

    /**
     * Mezcla de bloques en F0.
     *
     * @return Regresa <code>false</code> si el ordenamiento se completo y
     * <code>true</code> si continua verficando.
     * @throws FileNotFoundException En caso de no encontrar el archivo
     * @throws IOException En caso de no poder leer o escribir
     */
    public boolean DesMergeEnF0() throws FileNotFoundException, IOException {

        boolean verificar = true;
        int i = 0, j = 0;

        FileReader archivo1 = new FileReader(f1);
        BufferedReader b1 = new BufferedReader(archivo1);
        FileReader archivo2 = new FileReader(f2);
        BufferedReader b2 = new BufferedReader(archivo2);

        BufferedWriter archivo0 = new BufferedWriter(new FileWriter(f0, true));
        archivo0.newLine();
        String cadaux = "";
        String cadaux2 = "";
        String cadena1 = b1.readLine();
        String cadena2 = b2.readLine();

        while (cadaux != null) {
            cadaux = b1.readLine();
            cadaux2 = b2.readLine();
            if (cadaux != null) {
                cadena1 = cadaux;
                cadena2 = cadaux2;
            }
        }

        if ("".equals(cadena1) || "".equals(cadena2)) {
            b1.close();
            b2.close();
            archivo0.close();
            verificar = false;
            return verificar;
        }

        String[] arreglo = cadena1.split(",");
        String[] arr = cadena2.split(",");

        while (i < arreglo.length && j < arr.length) {
            while (!"'".equals(arreglo[i]) && !"'".equals(arr[j])) {
                if (Float.parseFloat(arreglo[i]) >= Float.parseFloat(arr[j])) {//Igualmente ahora se comprueba que el arreglo en la posición i en arreglo sea mayor a la posición en  arr
                    archivo0.write(arreglo[i]);
                    archivo0.write(",");
                    i++;
                } else if (Float.parseFloat(arreglo[i]) < Float.parseFloat(arr[j])) {// Y ahora que arreglo en i sea menor a arr en j 
                    archivo0.write(arr[j]);
                    archivo0.write(",");
                    j++;
                }
            }
            if (!"'".equals(arreglo[i])) {
                while (!"'".equals(arreglo[i])) {
                    archivo0.write(arreglo[i]);
                    archivo0.write(",");
                    i++;
                }
            }
            if (!"'".equals(arr[j])) {
                while (!"'".equals(arr[j])) {
                    archivo0.write(arr[j]);
                    archivo0.write(",");
                    j++;
                }
            }
            i++;
            j++;
        }
        if (i == arreglo.length) {
            while (j < arr.length && !"'".equals(arr[j])) {
                archivo0.write(arr[j]);
                archivo0.write(",");
                j++;
            }
        } else if (j == arr.length) {
            while (i < arreglo.length && !"'".equals(arreglo[i])) {
                archivo0.write(arreglo[i]);
                archivo0.write(",");
                i++;
            }
        }
        b1.close();
        b2.close();
        archivo0.close();
        return verificar;
    }
}
