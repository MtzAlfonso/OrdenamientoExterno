package controlador;

import java.util.*;
import java.io.*;
import ordenamientoExterno.*;

/**
 * Contiene el código principal para que inicie el programa.
 *
 * @author Alfredo Araiza Granados<br>Eduardo Fajardo Téllez<br>José Alfonso
 * Martínez Baeza
 *
 * @version 2.0.1
 * @since 30/septiembre/2019
 */
public class Principal {

    /**
     * Incluye el código para mostrar las instrucciones necesarias para mostrar
     * las opciones que el usuario tiene para utilizar correctamente el
     * programa.
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int op;
        Scanner sc = new Scanner(System.in);
        /**
         * Menú principal, incluye el menú recursivo para los diferentes metodos
         * de ordenamiento.
         */
        do {
            System.out.println("\nProyecto Nº 1 - Ordenamiento externo");
            System.out.println("\nElija la opcion que deseé:");
            System.out.println(" 1: Polifase");
            System.out.println(" 2: Mezcla directa");
            System.out.println(" 3: Distribución");
            System.out.println(" 4: Salir del programa");
            System.out.print(">> ");

            op = sc.nextInt();
            switch (op) {
                case 1:
                    System.out.println("\n** Polifase **");
                    System.out.println("Ingrese la ruta completa del archivo a ordenar: (/user/.../archivo.txt)");
                    System.out.print("> ");
                    Polifase polifase = new Polifase(sc.next());
                    System.out.println("\nTipo de ordenamiento:");
                    System.out.println(" 1: Ascendente");
                    System.out.println(" 2: Descendente");
                    System.out.print("> ");
                    polifase.setOrd(sc.nextInt());
                    polifase.iniciar();
                    break;
                case 2:
                    System.out.println("\n** Mezcla Equilibrada **");
                    System.out.println("Ingrese la ruta completa del archivo a ordenar: (/user/.../archivo.txt)");
                    System.out.print("> ");
                    MezclaEquilibrada mez = new MezclaEquilibrada(sc.next());
                    System.out.println("\nTipo de ordenamiento:");
                    System.out.println(" 1: Ascendente");
                    System.out.println(" 2: Descendente");
                    System.out.print("> ");
                    mez.EjecucionME(sc.nextInt());
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        } while (op != 4);
    }

}
