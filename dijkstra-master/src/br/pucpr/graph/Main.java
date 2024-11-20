package br.pucpr.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

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



        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        Scanner in = new Scanner(System.in);
        System.out.println("TP Final de Programacion III");

        Matriz M = new Matriz(8,52);

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 49; j++) {
                M.llenarMatriz(i,j, centros.path(50+i, j)); // path lo que hace es devolver el camino mas corto entre un nodo y otro
                                                            // en este caso estamos verificando el menor camino de cada productor a cada centro
            }
        }

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


        M.printMatrix();

        // La matriz en sus filas tienen los centros del 51 al 58 incluidos (8 centros en total)
        // En sus columnas tiene los productores del 1 al 50 incluidos (50 centros en total)

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        while (true) {
            System.out.println("Please, enter your desired route. Leave the field blank to exit.");
            int source = readStation("Source", in);
            int destination = readStation("Destination", in);

            System.out.println("Ruta mas rapida " + centros.path(source, destination));

        }
    }
}
