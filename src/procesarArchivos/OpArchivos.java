package procesarArchivos;

import java.util.*;
import java.io.*;

/**
 * Métodos para el manejo de archivos.
 *
 * @author José Alfonso Martínez Baeza
 * @version 1.0
 * @since 28/septiembre/2019
 */
public class OpArchivos {

    public List<Float> sToF(List<String> listaS) {
        List<Float> listaF = new LinkedList<>();
        for (String var : listaS) {
            listaF.add(Float.parseFloat(var));
        }
        return listaF;
    }

    public String fToS(List<Float> listaF) {
        String listaS = "";
        for (Float var : listaF) {
            if (var != listaF.get(listaF.size() - 1)) {
                listaS = listaS + var.toString() + ",";
            } else {
                listaS = listaS + var.toString();
            }
        }
        return listaS;
    }

    public List<String> dividir(String linea, String token) {
        List<String> bloque;
        bloque = new ArrayList<>(Arrays.asList(linea.split(token)));
        return bloque;
    }

    public void vaciarArchivo(File f) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(f);
            pw.print("");
            pw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public String leer(File f) {
        String linea = null;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
            linea = br.readLine();
            br.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return linea;
    }

    public void escribir(File f, String cadena) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(f, true));
            bw.write(cadena + "]");
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
