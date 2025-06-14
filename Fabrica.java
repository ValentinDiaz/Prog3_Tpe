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

        List<Maquina> candidatos = new ArrayList<>(maquinas);
        candidatos.sort((m1, m2) -> Integer.compare(m2.getPiezas(), m1.getPiezas()));
        List<Maquina> camino = new ArrayList<>();
        backtracking(camino, 0, candidatos);

        return mejorSolucionBack;
    }

   private void backtracking(List<Maquina> camino, int suma, List<Maquina> candidatos) {
    estadosGeneradosBack++;

    if (suma == piezasTotales) {
        if (mejorSolucionBack == null || camino.size() < mejorSolucionBack.size()) {
            
        }
    } else{ 
        
        if(suma < piezasTotales &&
              (mejorSolucionBack == null || camino.size() < mejorSolucionBack.size())) {

        for (int i = 0; i < candidatos.size(); i++) {
            Maquina m = candidatos.get(i);

            if (suma + m.getPiezas() <= piezasTotales) {
                camino.add(m);
                suma += m.getPiezas();
                backtracking(camino, suma, candidatos);
                camino.remove(camino.size() - 1);
                suma -= m.getPiezas(); // backtrack
            }
        }
    }
    }
}

    public List<Maquina> solucionGreedy() {
        List<Maquina> solucion = new ArrayList<>();
        List<Maquina> disponibles = new ArrayList<>(this.maquinas);
        disponibles.sort((m1, m2) -> Integer.compare(m2.getPiezas(), m1.getPiezas()));
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
