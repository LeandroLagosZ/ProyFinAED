package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
//Leandro Lagos
// Gestor de persistencia que almacena la sesión (historial, descargas) en archivos .txt locales
public class SesionManager {

    private final String HISTORIAL_FILE = "historial.txt";
    private final String DESCARGAS_FILE = "descargas.txt";

    // Guarda la sesión actual escribiendo en los archivos .txt locales
    public void guardarSesion(Object[] historial, Object[] descargas) {
        try {
            guardarArchivo(HISTORIAL_FILE, historial);
            guardarArchivo(DESCARGAS_FILE, descargas);
            System.out.println("[SesionManager] Sesión guardada con éxito.");
        } catch (IOException e) {
            System.err.println("[SesionManager] Error al guardar sesión: " + e.getMessage());
        }
    }

    // Carga los datos de juegos recientes (historial) desde historial.txt
    public Videojuego[] cargarHistorial() {
        return cargarArchivo(HISTORIAL_FILE);
    }

    // Carga los datos de la cola de descargas desde descargas.txt
    public Videojuego[] cargarDescargas() {
        return cargarArchivo(DESCARGAS_FILE);
    }

    // Método de asistencia para escribir un arreglo de videojuegos en disco en formato serializado
    private void guardarArchivo(String filename, Object[] arr) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Object obj : arr) {
                Videojuego v = (Videojuego) obj;
                if (v != null) {
                    // Guardar serializado por comas: id, nombre, genero, plataforma, etc.
                    bw.write(v.getId() + ";" + v.getNombre() + ";" + v.getGenero() + ";" + v.getPlataforma() + ";" +
                             v.getPuntuacion() + ";" + v.getDescargas() + ";" + v.getFechaLanzamiento() + ";" +
                             v.getDesarrollador() + ";" + v.getEditor() + ";" + v.getPrecio() + ";" +
                             v.getModoJuego() + ";" + v.isMultijugador() + ";" + v.getTamanoGb());
                    bw.newLine();
                }
            }
        }
    }

    // Método de asistencia para leer un archivo y reconstruir los objetos de tipo Videojuego
    private Videojuego[] cargarArchivo(String filename) {
        List<Videojuego> list = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return new Videojuego[0];
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 13) {
                    Videojuego v = new Videojuego(
                        parts[0], parts[1], parts[2], parts[3],
                        Double.parseDouble(parts[4]), Integer.parseInt(parts[5]), parts[6],
                        parts[7], parts[8], Double.parseDouble(parts[9]), parts[10],
                        Boolean.parseBoolean(parts[11]), Double.parseDouble(parts[12])
                    );
                    list.add(v);
                }
            }
        } catch (Exception e) {
            System.err.println("[SesionManager] Error al cargar archivo " + filename + ": " + e.getMessage());
        }
        return list.toArray(new Videojuego[0]);
    }
}
