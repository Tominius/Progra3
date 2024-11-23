package tp2_Progra3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    //Este metodo fue creado para imprimir a que Centro debe ir cada Cliente

    public static void rutas(List<Integer> combs, Matriz matriz ){
        int u = 0;

        for (int j = 0; j < 50; j++) {
            List<Integer> clienteaC = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                if (combs.get(i) == 1) {
                    clienteaC.add(matriz.matriz[i][j]);
                } else {
                    clienteaC.add(Integer.MAX_VALUE);
                }
            }
            u += findMin(clienteaC)[0];
            System.out.println( " El cliente "+ (j+1)+" fue al centro "+ (findMin(clienteaC)[1]+1) );
        }
    }


    public static void startListas(int indice, int fin, Matriz matriz) {

    List<Integer> combs = new ArrayList<>();                // Lista vacía que luego tendra cada una de las combinaciones posibles
        List<Integer> mejorCombinacion = new ArrayList<>(); // Para almacenar la mejor combinación en el transcurso del programa

        Upper upper = new Upper();                  //Comienza con el peor valor, y se actualizara cuando haya un valor pesimista (u) mejor que el del upper
        upper.ActualizarValor(Integer.MAX_VALUE);   //Luego tambien se hara una comparacion con el valor optimista (c) para descartar ramas que "no tienen futuro"

        listas(indice, fin, combs, matriz, upper, mejorCombinacion);

        // Imprimir la mejor combinación al final
        System.out.println(" ");
        System.out.println("Resultado OPTIMO");
        System.out.println(" ");
        rutas(mejorCombinacion, matriz);
        System.out.println(" ");
        System.out.print("Centros que se deben construir: " );
        for (int i = 0; i < mejorCombinacion.size(); i++) {
            if (mejorCombinacion.get(i) ==1){
                System.out.print(" "+(i+1)+" ");
            }
        }
        System.out.println("");

        // Imprime el costo total
        System.out.println("Costo total: " + upper.getValor());
    }

    private static void listas(int indice, int fin, List<Integer> combs, Matriz matriz, Upper upper, List<Integer> mejorCombinacion) {

        // Si llegamos al tamaño esperado (combinación completa)
        if (combs.size() == fin) {

            // Calcular u (pesimista) para la combinación completa
            // Solo se pueden seleccionar valores donde los centros esten construidos

            int u = 0;
            for (int j = 0; j < 50; j++) {
                List<Integer> clienteaC = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    if (combs.get(i) == 1) {
                        clienteaC.add(matriz.matriz[i][j]);
                    } else {
                        clienteaC.add(Integer.MAX_VALUE);
                    }
                }
                u += findMin(clienteaC)[0];
                //System.out.println( " El cliente "+ (j+1)+" fue al centro "+ (findMin(clienteaC)[1]+1) );
            }

            //Suma el costo anual de los centros construidos

            for (int q = 0; q < combs.size(); q++) {
                if (combs.get(q) == 1) {
                    u += matriz.matriz[q][51];
                }
            }

            // Si da 0 le decimos que sea infinito, ya que sino no seguiria bien la logica

            if (u <= 0) {
                u = Integer.MAX_VALUE;
            }

            // Si encontramos un mejor valor, actualizamos upper y la mejor combinancion

            if (u < upper.getValor()) {
                upper.ActualizarValor(u);
                mejorCombinacion.clear();
                mejorCombinacion.addAll(combs);
            }

            return; //retornamos ya completo la combinacion
        }

        // Valores posibles (-1 y 1, construido o no construido)

        int[] posibilidades = {1, -1};

        //Ahora lo que haremos es agregar el primer valor, llamar recursivamente a la funcion en caso de que se cumpla cierto if, y quitar el ultimo valor agregado
        //para dar espacio al segundo ciclo.

        for (int val : posibilidades) {

            combs.add(val); // Añadimos el valor en la posición actual

            // Calcular c (optimista) para combinaciones parciales. Solo se pueden seleccionar valores donde la combinacion no este en -1

            int c = 0;
            for (int j = 0; j < 50; j++) {
                List<Integer> clienteaCC = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    if (combs.size() > i && combs.get(i) == 1) {
                        clienteaCC.add(matriz.matriz[i][j]);
                    } else if (combs.size() <= i) {
                        clienteaCC.add(matriz.matriz[i][j]);
                    }
                }
                c += findMin(clienteaCC)[0];
            }

            //Agregamos los costos anuales de los centros que ya sabemos que se construiran
            for (int p = 0; p < combs.size(); p++) {
                if (combs.get(p) == 1) {
                    c += matriz.matriz[p][51];
                }
            }

            //Si c(valor optimista) tiene un peor valor que upper esto significara que hay una rama que ya sabemos que tendra un mejor resultado final que esta
            //Por lo tanto esta se descarta.
            if (c >= upper.getValor()) {
                combs.remove(combs.size() - 1);
                return;
            }

            // Llamada recursiva
            listas(indice + 1, fin, combs, matriz, upper, mejorCombinacion);

            //Remuevo el ultimo valor agregado asi en el proximo ciclo se agrega el valor en la misma posicion.
            combs.remove(combs.size() - 1);
        }
    }



    public static int[] findMin(List<Integer> list) {
        if (list.isEmpty()) {
            return new int[]{0, -1};
        }

        int min = list.get(0);
        int minIndex = 0;

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
                minIndex = i;
            }
        }

        return new int[]{min, minIndex};
    }


    public static void main(String[] args) {
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Crea grafo de 58 nodos

        Graph centros = new Graph(58); // 58 nodos (50 productores y 8 centros)

        // Crea todas las rutas especificadas en el archivo.txt que nos dio el profe

        try (BufferedReader br = new BufferedReader(new FileReader("dijkstra-master/src/tp2_Progra3/rutas.txt"))) {
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
        Matriz M = new Matriz(8,52);  //Se crea la Matriz que tendra como filas los Centros y como columnas los clientes

        //Relleno las ultimas 2 columnas
        //Estas ultimas 2 columnas tendran el costo al puerto y el costo anual del centro

        try (BufferedReader br = new BufferedReader(new FileReader("dijkstra-master/src/tp2_Progra3/Centros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // split por , | El primer elemento es el centro, el segundo el costo unitario, el Costo anual
                M.llenarMatriz(Integer.parseInt(parts[0]),50, Integer.parseInt(parts[1]));
                M.llenarMatriz(Integer.parseInt(parts[0]),51, Integer.parseInt(parts[2]));
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo.");
        }

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        System.out.println("TP Final de Programacion III");
        System.out.println(" ");
        System.out.println("Matriz Dijkstra");
        System.out.println(" ");

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

        startListas(0, 8,M);

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }
}
