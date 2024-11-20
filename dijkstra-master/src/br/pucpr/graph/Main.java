package br.pucpr.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {


    public static void startListas(int indice, int fin, Matriz matriz, int upper) {
        List<Integer> combs = new ArrayList<>();  // Lista vacía

        listas(indice, fin, combs, matriz, upper);
    }

    private static void  listas(int indice, int fin, List<Integer> combs, Matriz matriz, int upper) {

        // Si llegamos al tamaño esperado

        if (combs.size() == fin) {
            return;
        }



        // Valores posibles (-1 y 1)
        int[] posibilidades = {1, -1};

        // probamos los 2 valores posibles
        for (int val : posibilidades) {



            combs.add(val); // Añadimos el valor en la posición actual

            //Buscamos u

            int u = 0; // Mejor valor pesimista

            for (int j = 0; j < 50; j++) {

                List<Integer> clienteaC = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    if (combs.size() > i) {
                        if (combs.get(i) == 1) {
                            clienteaC.add(matriz.matriz[i][j]);
                        }
                    }
                }
                u += findMin(clienteaC);
            }

            for (int q = 0; q < combs.size(); q++) {
                if (combs.get(q) == 1) {
                    u+= matriz.matriz[q][51];
                }
            }

            if (u== 0) {
                 u = Integer.MAX_VALUE;
            }

            System.out.println("U: "+u);

            // Buscamos c
            int c = 0; // Mejor valor posible

            for (int j = 0; j < 50; j++) {

                List<Integer> clienteaCC = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    if (combs.size() > i) {
                        if (combs.get(i) == 1) {
                            clienteaCC.add(matriz.matriz[i][j]);
                        }
                    }else{
                        clienteaCC.add(matriz.matriz[i][j]);
                    }
                }
                c += findMin(clienteaCC);
            }

            for (int p = 0; p < combs.size(); p++) {
                if (combs.get(p) == 1) {
                    c+= matriz.matriz[p][51];
                }
            }

            if (c== 0) {
                c = Integer.MAX_VALUE;
            }

            System.out.println("C: "+c);

            System.out.println(combs);

            listas(indice + 1, fin, combs,matriz, Integer.MAX_VALUE);  // Llamada recursiva para el siguiente índice
            combs.remove(combs.size() - 1);  // borramos el ultimo indice asi podemos probar el siguiente valor




        }
    }

    public static int findMin(List<Integer> list) {

        if (list.isEmpty()) {
            return 0;
        }

        int min = list.get(0); // Suponemos que el primer elemento es el máximo inicialmente
        for (int num : list) {
            if (num < min) {
                min = num; // Actualizamos el máximo si encontramos un número mayor
            }
        }
        return min;
    }

    private static int readStation(String type, Scanner in) {
        while (true) {
            System.out.println(type + ":");
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Bye bye!");
                System.exit(0);
            }
            try {
                int station = Integer.parseInt(line);
                if (station >= 0 && station <= 58) return station-1;
            } catch (NumberFormatException e) {
            }
            System.out.println("Invalid station! Try again.");
        }
    }


    public static void main(String[] args) {
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Crea grafo de 58 nodos

        Graph centros = new Graph(58); // 58 nodos (50 productores y 8 centros)

        // Crea todas las rutas especificadas en el archivo.txt que nos dio el profe

        try (BufferedReader br = new BufferedReader(new FileReader("dijkstra-master/src/br/pucpr/graph/rutas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // split por , ; El primer elemento es el nodo origen, el segundo el destino, el tercero el costo o peso de arista
                centros.makeEdge(Integer.parseInt( parts[0]),Integer.parseInt( parts[1]), Integer.parseInt( parts[2]));
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo.");
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Matriz M = new Matriz(8,52);

        //Relleno las ultimas 2 columnas

        try (BufferedReader br = new BufferedReader(new FileReader("dijkstra-master/src/br/pucpr/graph/Centros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // split por , ; El primer elemento es el centro, el segundo el costo unitario, el Costo anual
                M.llenarMatriz(Integer.parseInt(parts[0]),50, Integer.parseInt(parts[1]));
                M.llenarMatriz(Integer.parseInt(parts[0]),51, Integer.parseInt(parts[2]));
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo.");
            e.printStackTrace();
        }

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        Scanner in = new Scanner(System.in);
        System.out.println("TP Final de Programacion III");



        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 49; j++) {
                M.llenarMatriz(i,j, (centros.path(50+i, j) + M.matriz[i][50])*10); // path lo que hace es devolver el camino mas corto entre un nodo y otro
                                                            // en este caso estamos verificando el menor camino de cada productor a cada centro
            }
        }



        M.printMatrix();


        // La matriz en sus filas tienen los centros del 51 al 58 incluidos (8 centros en total)
        // En sus columnas tiene los productores del 1 al 50 incluidos (50 centros en total)

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

       startListas(0, 8,M,Integer.MAX_VALUE);

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        while (true) {
            System.out.println("Please, enter your desired route. Leave the field blank to exit.");
            int source = readStation("Source", in);
            int destination = readStation("Destination", in);

            System.out.println("Ruta mas rapida " + centros.path(source, destination));

        }
    }
}
