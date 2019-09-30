package ordenamientoExterno;

import ordenamientoInterno.*;
import procesarArchivos.*;
import java.io.*;
import java.util.*;

/**
 * Ordenamiento por polifase.
 *
 * @author José Alfonso Martínez Baeza
 * @version 3.0.4
 * @since 30/septiembre/2019
 */
public class Polifase {

    /**
     * Archivo original. Se asigna a un archivo distinto a <code>f0</code> para
     * poder guardar los datos desordenados.
     */
    private File original;
    /**
     * Archivo auxiliar. Se utiliza para guardar los bloques de datos derivados
     * del archivo original.
     */
    private File f0, f1, f2, f3;
    /**
     * Algoritmo de ordenamiento interno ascendente.
     */
    private MergeSortAs as;
    /**
     * Guarda las iteraciones del algoritmo en un archivo de registro.
     */
    private PrintWriter pw;
    /**
     * Carpeta donde se guardan los archivos auxiliares y de prueba.
     */
    private File directorio;
    /**
     * Algoritmo de ordenamiento interno descendente.
     */
    private MergeSortDes des;
    /**
     * Objeto para la manipulación de archivos.
     */
    private OpArchivos op;
    /**
     * Tamaño de los bloques en los que se divide el archivo original.
     */
    private int m;
    /**
     * Variable para controlar el tipo de ordenamiento.
     */
    private int ord;
    private int pasada, contador;

    /**
     * Constructor de la clase Polifase.
     *
     * @param ruta
     */
    public Polifase(String ruta) {
        m = 50;
        pasada = 0;
        as = new MergeSortAs();
        des = new MergeSortDes();
        op = new OpArchivos();
        contador = 0;
        ord = 0;

        original = new File(ruta);
        directorio = new File("./archivos");
        directorio.mkdir();
        directorio = new File("./archivos/polifase");
        directorio.mkdir();
        
        f0 = new File("./archivos/polifase/f0.txt");
        f1 = new File("./archivos/polifase/f1.txt");
        f2 = new File("./archivos/polifase/f2.txt");
        f3 = new File("./archivos/polifase/f3.txt");
        
        try {
            pw = new PrintWriter(new File("./archivos/polifase/registro.txt"));
        } catch (FileNotFoundException ex) {}

        if (f0.exists())
            f0.delete();
        if (f1.exists())
            f1.delete();
        if (f2.exists())
            f2.delete();
        if (f3.exists())
            f3.delete();
    }

    /**
     * Inicia el proceso del algoritmo de Polifase.
     */
    public void iniciar() {
        fase1();
        while (op.leer(f1) != null && op.leer(f2) != null || op.leer(f0) != null && op.leer(f3) != null) {
            fase2();
        }
        if(f0.length() == 0)
            System.out.println("\nEl archivo quedó ordenado en "+ f1.getPath());
        else
            System.out.println("\nEl archivo quedó ordenado en " + f0.getPath());
        
        pw.println("\nArchivo ordenado");
        pw.close();
    }

    /**
     * Asigna el valor a la variable ord. Ascendente o Descendente.
     *
     * @param ord Guarda un valor entero entre 1 y 2.
     */
    public void setOrd(int ord) {
        this.ord = ord;
    }

    /**
     * Construcción y distribución de los arreglos ordenados.
     */
    private void fase1() {
        List<String> elementos;
        List<Float> sub;
        //directorio.mkdir();
        //System.out.println("Fase 1...");
        pw.println("Fase 1...");
        elementos = op.dividir(op.leer(original), ",");

        while (!elementos.isEmpty()) {
            if (elementos.size() > m) {
                sub = op.sToF(elementos.subList(0, m));
                for (int j = 0; j < m; j++) {
                    elementos.remove(0);
                }
            } else {
                sub = op.sToF(elementos.subList(0, elementos.size()));
                elementos.clear();
            }
            switch (ord) {
                case 1:
                    sub = as.sort(sub, 0, sub.size() - 1);
                    break;
                case 2:
                    sub = des.sort(sub, 0, sub.size() - 1);
                    break;
                default:
                    sub = as.sort(sub, 0, sub.size() - 1);
                    ord = 1;
                    break;
            }
            //System.out.println(sub);
            pw.println(sub);
            if (contador % 2 == 0) {
                op.escribir(f1, op.fToS(sub));
            } else {
                op.escribir(f2, op.fToS(sub));
            }
            contador++;
        }
    }

    /**
     * Intercalación de archivos.
     */
    private void fase2() {
        //System.out.println("Fase 2...");
        pw.println("Fase 2...");
        if (pasada % 2 == 0) {
            if (ord == 1) {
                mezclaAs(f1, f2, f0, f3);
            } else {
                mezclaDes(f1, f2, f0, f3);
            }
            op.vaciarArchivo(f1);
            op.vaciarArchivo(f2);
        } else {
            if (ord == 1) {
                mezclaAs(f0, f3, f1, f2);
            } else {
                mezclaDes(f0, f3, f1, f2);
            }
            op.vaciarArchivo(f0);
            op.vaciarArchivo(f3);
        }
        pasada++;
    }

    private void mezclaAs(File f1, File f2, File f0, File f3) {
        boolean bandera = false;
        //System.out.println("Mezcla...");
        pw.println("Mezcla...");
        List<String> archivo1 = op.dividir(op.leer(f1), "]");
        List<String> archivo2 = op.dividir(op.leer(f2), "]");
        List<Float> aux1, aux2;

        int i, j, k, s1, s2;
        contador = 0;

        for (i = 0; i < archivo2.size(); i++) {
            j = 0;
            k = 0;

            List<Float> mezclada = new LinkedList<>();
            aux1 = op.sToF(op.dividir(archivo1.get(i), ","));
            aux2 = op.sToF(op.dividir(archivo2.get(i), ","));

            s1 = aux1.size();
            s2 = aux2.size();
            while (mezclada.size() != s1 + s2) {
                if (j == s1 - 1 && k == s2 - 1) {
                    if (aux1.get(j) < aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        mezclada.add(aux2.get(k));
                    } else {
                        mezclada.add(aux2.get(k));
                        mezclada.add(aux1.get(j));
                    }
                } else if (j == s1 - 1) {
                    if (aux1.get(j) < aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        for (int h = k; h < s2; h++) {
                            mezclada.add(aux2.get(h));
                        }
                    } else {
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                } else if (k == s2 - 1) {
                    if (aux2.get(k) < aux1.get(j)) {
                        mezclada.add(aux2.get(k));
                        for (int g = j; g < s1; g++) {
                            mezclada.add(aux1.get(g));
                        }
                    } else {
                        mezclada.add(aux1.get(j));
                        j++;
                    }
                } else {
                    if (aux1.get(j) < aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        j++;
                    } else {
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }
            }
            //System.out.println(mezclada);
            pw.println(mezclada);
            if (contador % 2 == 0) {
                op.escribir(f0, op.fToS(mezclada));
                bandera = true;
            } else {
                op.escribir(f3, op.fToS(mezclada));
                bandera = false;
            }
            contador++;
        }
        if (archivo1.size() != archivo2.size()) {
            aux1 = op.sToF(op.dividir(archivo1.get(i), ","));
            if (!bandera) {
                op.escribir(f0, op.fToS(aux1));
            } else {
                op.escribir(f3, op.fToS(aux1));
            }
            //System.out.println(aux1);
            pw.println(aux1);
        }
    }

    private void mezclaDes(File f1, File f2, File f0, File f3) {
        boolean bandera = false;
        //System.out.println("Mezcla...");
        pw.println("Mezcla...");
        List<String> archivo1 = op.dividir(op.leer(f1), "]");
        List<String> archivo2 = op.dividir(op.leer(f2), "]");
        List<Float> aux1, aux2;

        int i, j, k, s1, s2;
        contador = 0;

        for (i = 0; i < archivo2.size(); i++) {
            j = 0;
            k = 0;

            List<Float> mezclada = new LinkedList<>();
            aux1 = op.sToF(op.dividir(archivo1.get(i), ","));
            aux2 = op.sToF(op.dividir(archivo2.get(i), ","));

            s1 = aux1.size();
            s2 = aux2.size();
            while (mezclada.size() != s1 + s2) {
                if (j == s1 - 1 && k == s2 - 1) {
                    if (aux1.get(j) > aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        mezclada.add(aux2.get(k));
                    } else {
                        mezclada.add(aux2.get(k));
                        mezclada.add(aux1.get(j));
                    }
                } else if (j == s1 - 1) {
                    if (aux1.get(j) > aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        for (int h = k; h < s2; h++) {
                            mezclada.add(aux2.get(h));
                        }
                    } else {
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                } else if (k == s2 - 1) {
                    if (aux2.get(k) > aux1.get(j)) {
                        mezclada.add(aux2.get(k));
                        for (int g = j; g < s1; g++) {
                            mezclada.add(aux1.get(g));
                        }
                    } else {
                        mezclada.add(aux1.get(j));
                        j++;
                    }
                } else {
                    if (aux1.get(j) > aux2.get(k)) {
                        mezclada.add(aux1.get(j));
                        j++;
                    } else {
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }
            }
            //System.out.println(mezclada);
            pw.println(mezclada);
            if (contador % 2 == 0) {
                op.escribir(f0, op.fToS(mezclada));
                bandera = true;
            } else {
                op.escribir(f3, op.fToS(mezclada));
                bandera = false;
            }
            contador++;
        }
        if (archivo1.size() != archivo2.size()) {
            aux1 = op.sToF(op.dividir(archivo1.get(i), ","));
            if (!bandera) {
                op.escribir(f0, op.fToS(aux1));
            } else {
                op.escribir(f3, op.fToS(aux1));
            }
        }
    }
}
