package br.pucpr.graph;
import java.util.ArrayList;
import java.util.List;

public class Bactracking {

    public static void main(String[] args) {
        int n = 8; // Número de elementos en la lista
        List<Integer> currentList = new ArrayList<>(n); // Lista para guardar los valores

        // Inicializamos la lista con ceros

        for (int i = 0; i < n; i++) {
            currentList.add(0); // Lista inicial con todos los valores en 0
        }

        backtrack(currentList, n, 0);
    }

    // Función recursiva de backtracking
    public static void backtrack(List<Integer> currentList, int n, int index) {
        // Si hemos llenado la lista con 8 elementos, imprimimos la combinación
        if (index == n) {
            System.out.println(currentList); // Imprimir la lista completa
            return;
        }

        // Probamos los dos valores posibles para el índice actual (-1 o 1)
        for (int val : new int[]{-1, 1}) {
            currentList.set(index, val); // Asignamos el valor -1 o 1 en el índice actual
            backtrack(currentList, n, index + 1); // Llamada recursiva para el siguiente índice
        }
    }
    }

