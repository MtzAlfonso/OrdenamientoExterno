package ordenamientoExterno;

import ordenamientoInterno.*;
import procesarArchivos.*;
import java.io.*;
import java.util.*;

/**
 * Ordenamiento por polifase.
 * @author José Alfonso Martínez Baeza
 * @version 3.0.1
 * @since 28/septiembre/2019
 */
public class Polifase {
    File original;
    File f0;
    File f1;
    File f2;
    File f3;
    MergeSortAs as;
    MergeSortDes des;
    OpArchivos op;
    int m, pasada, contador, ord;
    
    /**
     * Constructor de la clase Polifase.
     * @param ruta
     */
    public Polifase(String ruta){
        m = 50;
        pasada = 0;
        as = new MergeSortAs();
        des = new MergeSortDes();
        op = new OpArchivos();
        contador = 0;
        ord = 0;
        
        original = new File(ruta);
        f0 = new File("./archivos/auxiliares/f0.txt");
        f1 = new File("./archivos/auxiliares/f1.txt");
        f2 = new File("./archivos/auxiliares/f2.txt");
        f3 = new File("./archivos/auxiliares/f3.txt");
        
        if(f0.exists())
            f0.delete();
        if(f1.exists())
            f1.delete();
        if(f2.exists())
            f2.delete();
        if(f3.exists())
            f3.delete();
    }
    
    public void iniciar(){
        fase1();
        while(op.leer(f1) != null && op.leer(f2) != null || op.leer(f0) != null && op.leer(f3) != null){
            fase2();
        }
    }
    
    public void setOrd(int ord){
        this.ord = ord;
    }
    
    /**
     * Fase 1. Construcción y distribución de los arreglos ordenados.
     */
    void fase1(){
        List<String> elementos;
        List<Float> sub;
        System.out.println("Fase 1...");
        elementos = op.dividir(op.leer(original),",");
            
        while(!elementos.isEmpty()){
            if(elementos.size() > m){
                sub = sToF(elementos.subList(0, m));
                for(int j = 0 ; j < m ; j++)
                    elementos.remove(0);
            }
            else{
                sub = sToF(elementos.subList(0, elementos.size()));
                elementos.clear();
            }
            switch(ord){
                case 1:
                    sub = as.sort(sub, 0, sub.size()-1);
                    break;
                case 2:
                    sub = des.sort(sub, 0, sub.size()-1);
                    break;
                default: 
                    sub = as.sort(sub, 0, sub.size()-1);
                    ord = 1;
                    break;
            }
            System.out.println(sub);
            if(contador%2 == 0)
                op.escribir(f1, fToS(sub));
            else
                op.escribir(f2, fToS(sub));
            contador++;
        }
    }
    
    /**
     * Fase 2. Intercalación de archivos.
     */
    void fase2(){
        System.out.println("Fase 2...");
        //System.out.println(pasada%2);
        if(pasada % 2 == 0){
            if(ord == 1)
                mezclaAs(f1,f2,f0,f3);
            else
                mezclaDes(f1,f2,f0,f3);
            op.vaciarArchivo(f1);
            op.vaciarArchivo(f2);
        }else{
            if(ord == 1)
                mezclaAs(f0,f3,f1,f2);
            else
                mezclaDes(f0,f3,f1,f2);
            op.vaciarArchivo(f0);
            op.vaciarArchivo(f3);
        }
        pasada++;
    }
    
    List<Float> sToF(List<String> listaS){
        List<Float> listaF = new LinkedList<>();
        for(String var: listaS)
            listaF.add(Float.parseFloat(var));
        return listaF;
    }
    
    String fToS(List<Float> listaF){
        String listaS = "";
        for(Float var: listaF){
            if(var != listaF.get(listaF.size()-1))
                listaS = listaS + var.toString() + ",";
            else
                listaS = listaS + var.toString();
        }
        return listaS;
    }
    
    void mezclaAs(File f1, File f2, File f0, File f3){
        boolean bandera = false;
        System.out.println("Mezcla...");
        List<String> archivo1 = op.dividir(op.leer(f1),"]");
        List<String> archivo2 = op.dividir(op.leer(f2),"]");
        List<Float> aux1, aux2;
        
        int i, j, k, s1, s2;
        contador = 0;
        
        for(i = 0; i < archivo2.size(); i++){
            j = 0;
            k = 0;

            List<Float> mezclada = new LinkedList<>();
            aux1 = sToF(op.dividir(archivo1.get(i),","));
            aux2 = sToF(op.dividir(archivo2.get(i),","));

            s1 = aux1.size();
            s2 = aux2.size();
            while(mezclada.size() != s1+s2){
                //System.out.println("j= "+ j +", k= "+ k);
                //System.out.println("aux1[j]= "+ aux1.get(j) + ", aux2[k]= "+ aux2.get(k));
                if(j == s1-1 && k == s2-1){
                    //System.out.println("j= "+ j +", k= "+ k);
                    if(aux1.get(j) < aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        mezclada.add(aux2.get(k));
                    }else{
                        mezclada.add(aux2.get(k));
                        mezclada.add(aux1.get(j));
                    }
                }
                else if(j == s1-1){
                    if(aux1.get(j) < aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        for(int h = k ; h < s2 ; h++){
                           mezclada.add(aux2.get(h)); 
                        }
                    }else{
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }else if(k == s2-1){
                    if(aux2.get(k) < aux1.get(j)){
                        mezclada.add(aux2.get(k));
                        for(int g = j ; g < s1 ; g++){
                           mezclada.add(aux1.get(g));
                        } 
                    }else{
                        mezclada.add(aux1.get(j));
                        j++;
                    }
                }else{
                    if(aux1.get(j) < aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        j++;
                    }else{
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }
            }
            System.out.println(mezclada);
            if(contador % 2 == 0){
                op.escribir(f0, fToS(mezclada));
                bandera = true;
            }
            else{
                op.escribir(f3, fToS(mezclada));
                bandera = false;
            }
            contador++;
        }
        if(archivo1.size() != archivo2.size()){
            aux1 = sToF(op.dividir(archivo1.get(i),","));
            if (!bandera)
                op.escribir(f0, fToS(aux1));
            else
                op.escribir(f3, fToS(aux1));
        }
    }
    void mezclaDes(File f1, File f2, File f0, File f3){
        boolean bandera = false;
        System.out.println("Mezcla...");
        List<String> archivo1 = op.dividir(op.leer(f1),"]");
        List<String> archivo2 = op.dividir(op.leer(f2),"]");
        List<Float> aux1, aux2;
        
        int i, j, k, s1, s2;
        contador = 0;
        
        for(i = 0; i < archivo2.size(); i++){
            j = 0;
            k = 0;

            List<Float> mezclada = new LinkedList<>();
            aux1 = sToF(op.dividir(archivo1.get(i),","));
            aux2 = sToF(op.dividir(archivo2.get(i),","));

            s1 = aux1.size();
            s2 = aux2.size();
            while(mezclada.size() != s1+s2){
                //System.out.println("j= "+ j +", k= "+ k);
                //System.out.println("aux1[j]= "+ aux1.get(j) + ", aux2[k]= "+ aux2.get(k));
                if(j == s1-1 && k == s2-1){
                    //System.out.println("j= "+ j +", k= "+ k);
                    if(aux1.get(j) > aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        mezclada.add(aux2.get(k));
                    }else{
                        mezclada.add(aux2.get(k));
                        mezclada.add(aux1.get(j));
                    }
                }
                else if(j == s1-1){
                    if(aux1.get(j) > aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        for(int h = k ; h < s2 ; h++){
                           mezclada.add(aux2.get(h)); 
                        }
                    }else{
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }else if(k == s2-1){
                    if(aux2.get(k) > aux1.get(j)){
                        mezclada.add(aux2.get(k));
                        for(int g = j ; g < s1 ; g++){
                           mezclada.add(aux1.get(g));
                        } 
                    }else{
                        mezclada.add(aux1.get(j));
                        j++;
                    }
                }else{
                    if(aux1.get(j) > aux2.get(k)){
                        mezclada.add(aux1.get(j));
                        j++;
                    }else{
                        mezclada.add(aux2.get(k));
                        k++;
                    }
                }
            }
            System.out.println(mezclada);
            if(contador % 2 == 0){
                op.escribir(f0, fToS(mezclada));
                bandera = true;
            }
            else{
                op.escribir(f3, fToS(mezclada));
                bandera = false;
            }
            contador++;
        }
        if(archivo1.size() != archivo2.size()){
            aux1 = sToF(op.dividir(archivo1.get(i),","));
            if (!bandera)
                op.escribir(f0, fToS(aux1));
            else
                op.escribir(f3, fToS(aux1));
        }
    }
}