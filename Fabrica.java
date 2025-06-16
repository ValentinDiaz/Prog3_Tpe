import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabrica {
    private int piezasTotales;
    private List<Maquina> maquinas;
    private List<Maquina> mejorSolucionBack;
    private int estadosGeneradosGreddy;
    private int estadosGeneradosBack;

    public Fabrica() {
        this.mejorSolucionBack = new ArrayList<>();
        this.maquinas = new ArrayList<>();
    }

    public void cargarDesdeArchivo(String archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea = br.readLine();

        // Leer cantidad total de piezas
        this.piezasTotales = Integer.parseInt(linea.trim());

        // Leer máquinas
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            String nombre = partes[0].trim();
            int piezas = Integer.parseInt(partes[1].trim());
            maquinas.add(new Maquina(piezas, nombre));
        }

        br.close();
    }

    public List<Maquina> solucionBacktracking() {

        List<Maquina> camino = new ArrayList<>();

        backtracking(camino, 0);

        return mejorSolucionBack;
    }

    /*
     * Estrategia de resolución - Backtracking:
     * - Se genera un árbol de exploración en el que cada nivel representa una nueva
     * puesta en funcionamiento de una máquina.
     * 
     * - Cada nodo del arbol representa una secuencia parcial de máquinas y la
     * cantidad
     * acumulada de piezas producidas hasta ese punto.
     * - Un estado es valido es si la suma de piezas alcanza exactamente el total
     * requerido (piezasTotales).
     * 
     * - Se aplica poda si:
     * - La suma actual supera las piezasTotales.
     * - El camino actual ya tiene más máquinas que la mejor solución encontrada.
     * - Los candidatos se ordenan de mayor a menor cantidad de piezas para explorar
     * primero las combinaciones más prometedoras y así favorecer una poda temprana.
     * - Se lleva un contador de estados generados para medir el costo computacional
     * de la búsqueda.
     */
    private void backtracking(List<Maquina> camino, int suma) {
        estadosGeneradosBack++;

        if (suma == piezasTotales) {
            if (mejorSolucionBack.isEmpty() || camino.size() < mejorSolucionBack.size()) {
                mejorSolucionBack.clear();
                mejorSolucionBack.addAll(camino);
            }
        } else {

            if (suma < piezasTotales || camino.size() < mejorSolucionBack.size()) {

                for (Maquina m : maquinas) {
                    if (suma + m.getPiezas() <= piezasTotales) {
                        camino.add(m);
                        backtracking(camino, suma + m.getPiezas());
                        camino.remove(camino.size() - 1);
                    }
                }

            }

        }
    }

    /*
     * Estrategia de resolución - Greedy:
     * - Se consideran todas las máquinas como opciones posibles (candidatos), y se
     * pueden usar más de una vez.
     * - En cada paso, se elige la máquina que produce más piezas y que todavía no
     * hace que se pase del total necesario, con el fin de que tenga que recurrir a
     * usar otras maquinas y asi optimizar la busqueda.
     * - La idea es acercarse lo más rápido posible al total de piezas, eligiendo
     * siempre la mejor opción en ese momento.
     * - Esta estrategia no siempre garantiza que se llegue a una solución. Si con
     * las máquinas que elegimos no se puede alcanzar
     * la cantidad de piezas que necesitamos, entonces no se cumple el objetivo.
     * 
     * - Se cuenta cuántos candidatos se consideraron para medir cuánto costó
     * encontrar la solución.
     */

    public List<Maquina> solucionGreedy() {
        List<Maquina> solucion = new ArrayList<>();
        List<Maquina> disponibles = new ArrayList<>(this.maquinas);// copiamos el arreglo original de maquinas para no
                                                                   // midificarlo
        disponibles.sort((m1, m2) -> Integer.compare(m2.getPiezas(), m1.getPiezas()));// ordenamos las maquinas de mayor
                                                                                      // a menor capacidad de produccion
                                                                                      // de piezas
        int suma = 0;

        while (!disponibles.isEmpty() && suma < this.piezasTotales) {
            this.estadosGeneradosGreddy++;
            Maquina m = disponibles.get(0);
            if (esFactible(suma, m)) {
                solucion.add(m);
                suma += m.getPiezas();
            } else {
                disponibles.remove(m);
            }
        }

        if (suma == this.piezasTotales) {
            return solucion;
        }
        return Collections.emptyList();
    }

    private boolean esFactible(int sumaActual, Maquina m) {
        return (sumaActual + m.getPiezas()) <= this.piezasTotales;
    }

    public int getPiezasTotales() {
        return piezasTotales;
    }

    public int getEstadosGeneradosGreddy() {
        return estadosGeneradosGreddy;
    }

    public int getEstadosGeneradosBack() {
        return estadosGeneradosBack;
    }

}
