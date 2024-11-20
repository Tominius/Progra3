package br.pucpr.graph;

import java.util.ArrayList;
import java.util.List;

public class Matriz {
    int filas;
    int columnas;
    int[][] matriz;

    Matriz(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matriz = new int[filas][columnas];
    }

    public void llenarMatriz(int filas, int columnas, int valor) {
        this.matriz[filas][columnas] = valor;
    }

    public void printMatrix() {
        for (int i = 0; i < this.matriz.length; i++) {
            for (int j = 0; j < this.matriz[i].length; j++) {
                System.out.printf("%3d ", this.matriz[i][j]); // Formato con ancho de 3
            }
            System.out.println(); // Salto de línea después de cada fila
        }
    }



    public static int findMax(List<Integer> list) {

        if (list.isEmpty()) {
            return 0;
        }

        int max = list.get(0); // Suponemos que el primer elemento es el máximo inicialmente
        for (int num : list) {
            if (num > max) {
                max = num; // Actualizamos el máximo si encontramos un número mayor
            }
        }
        return max;
    }


}
