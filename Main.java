
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String config = "config.txt";

        Fabrica fabrica = new Fabrica();
        fabrica.cargarDesdeArchivo(config);
        System.out.println("Backtracking");
        List<Maquina> back = fabrica.solucionBacktracking();
        if (!back.isEmpty()) {
            System.out.println("Solución obtenida: " );
            for (Maquina m : back) {
                System.out.print(m.getNombre() + "(" + m.getPiezas() + ")" + " ");
            }
            System.out.println();

            System.out.println("Piezas producidas: " + back.stream().mapToInt(Maquina::getPiezas).sum());
            System.out.println("Puestas en funcionamiento: " + back.size());
            System.out.println("Estados generados: " + fabrica.getEstadosGeneradosBack());
        } else {
            System.out.println("No se pudo generar la cantidad de piezas exacta.");
        }

        System.out.println(" ");



         System.out.println("Greedy");
         List<Maquina> greedy = fabrica.solucionGreedy();

         if (!greedy.isEmpty()) {
             // Mostrar secuencia de máquinas
             System.out.print("Solución obtenida: ");
             for (Maquina m : greedy) {
                 System.out.print(m.getNombre() + "(" + m.getPiezas() + ")" + " ");
             }
             System.out.println();

             // Calcular cantidad de piezas producidas y puestas en funcionamiento
             int totalPiezas = greedy.stream().mapToInt(Maquina::getPiezas).sum();
             int puestasEnFuncionamiento = greedy.size();

             System.out.println("Cantidad de piezas producidas: " + totalPiezas);
             System.out.println("Cantidad de puestas en funcionamiento: " + puestasEnFuncionamiento);

             // Métrica de costo (estados generados)
             System.out.println("Candidatos considerados: " + fabrica.getEstadosGeneradosGreddy());
         } else {
             System.out.println("No se pudo generar la cantidad de piezas.");
         }
    }
}
