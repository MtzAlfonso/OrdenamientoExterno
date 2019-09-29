package ordenamientoInterno;

import java.util.List;

/**
 * Ordenamiento Merge Sort Ascendente.
 *
 * @author José Alfonso Martínez Baeza
 * @version 2.0
 * @since 23/septiembre/2019
 */
public class MergeSortAs {

    /**
     * Realiza la mezcla de sublistas del ordenamiento <i>MergeSort</i>. Se
     * realizará de manera recursiva hasta que las sublistas tengan longitud
     * igual a 1. Necesita 4 parámetros.
     *
     * @param arr Lista de valores a ordenar.
     * @param l Indice del primer elemento.
     * @param m Indice del elemento medio.
     * @param r Indice del último elemento.
     */
    private void merge(List<Float> arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        float L[] = new float[n1];
        float R[] = new float[n2];

        for (int i = 0; i < n1; ++i) {
            L[i] = arr.get(l + i);
        }

        for (int j = 0; j < n2; ++j) {
            R[j] = arr.get(m + 1 + j);
        }

        int i = 0, j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr.set(k, L[i]);
                i++;
            } else {
                arr.set(k, R[j]);
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr.set(k, L[i]);
            i++;
            k++;
        }

        while (j < n2) {
            arr.set(k, R[j]);
            j++;
            k++;
        }
    }

    /**
     * Dentro de este método se encuentran las llamadas recursivas para cada
     * sublista. Requiere de 3 parámetros.
     *
     * @param arr Lista a ordenar.
     * @param l Primer elemento de la sublista.
     * @param r Ultimo elemento de la sublista.
     * @return Retorna la lista ordenada.
     */
    public List sort(List<Float> arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            sort(arr, l, m);

            sort(arr, m + 1, r);

            merge(arr, l, m, r);
        }
        return arr;
    }
}
