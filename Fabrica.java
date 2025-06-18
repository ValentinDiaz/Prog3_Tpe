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
     * puesta en funcionamiento de una máquina:
     * 
     * * - El árbol de exploración se genera probando, en cada nivel, una máquina que se agrega a la secuencia (camino).
     *   Es decir, en cada rama del árbol se simula poner en funcionamiento una máquina más.
     *
     * - Un estado es válido si la suma de piezas del camino actual no supera el total de piezas requeridas (piezasTotales).
     * - Un estado es solución si la suma de piezas del camino actual es exactamente igual a piezasTotales.
     *   En ese caso, se compara la cantidad de máquinas usadas con la mejor solución encontrada hasta el momento.
     *
     * - Un estado no es válido si la suma de piezas ya supera el total requerido.
     * 
     * - Se aplica poda si:
     *
     * - La suma actual supera las piezasTotales.
     * - El camino actual ya tiene más máquinas que la mejor solución encontrada.
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

            if (suma < piezasTotales &&
                    (mejorSolucionBack.isEmpty() || camino.size() < mejorSolucionBack.size())) {

                for (Maquina m : maquinas) {
                    if (suma + m.getPiezas() <= piezasTotales) {
                        camino.add(m);
                        backtracking(camino, suma + m.getPiezas());
                        camino.removeLast();
                    }
                }

            }

        }
    }

    /*
     * Estrategia de resolución - Greedy:
     * - Se consideran todas las máquinas como opciones posibles (candidatos), y se
     * pueden usar más de una vez.
     * Se ordenan las máquinas de mayor de las que mas producen piezas a las que menos producen
     * - En cada paso, se elige la máquina que produce más piezas y que todavía
     * no se pase del total necesario, con el fin de que tenga que recurrir a
     * usar otras máquinas y asi optimizar la búsqueda.
     * - La idea es acercarse lo más rápido posible al total de piezas, eligiendo
     * siempre la mejor opción en ese momento.
     * - Esta estrategia no siempre garantiza que se llegue a una solución. Si con
     * las máquinas que elegimos no se puede alcanzar la cantidad de piezas que necesitamos,
     * entonces no se cumple el objetivo.
     * - Se cuenta cuántos candidatos se consideraron para medir cuánto costó
     * encontrar la solución.
     */

    public List<Maquina> solucionGreedy() {
        List<Maquina> solucion = new ArrayList<>();
        List<Maquina> disponibles = new ArrayList<>(this.maquinas);// copiamos el arreglo original de máquinas para no
                                                                   // modificarlo
        disponibles.sort((m1, m2) -> Integer.compare(m2.getPiezas(), m1.getPiezas()));// ordenamos las máquinas de mayor
                                                                                      // a menor capacidad de production
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
