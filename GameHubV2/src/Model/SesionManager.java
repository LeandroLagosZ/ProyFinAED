package Model;

import java.io.*;

public class SesionManager {
    private static final String HISTORIAL_FILE = "historial.txt";
    private static final String DESCARGAS_FILE = "descargas.txt";
    private static final String SEPARADOR = "|"; 

    public void guardarSesion(Object[] historial, Object[] descargas) {
        guardarArchivo(HISTORIAL_FILE, historial);
        guardarArchivo(DESCARGAS_FILE, descargas);
    }

    private void guardarArchivo(String ruta, Object[] juegos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Object obj : juegos) {
                if (obj != null) {
                    Videojuego vj = (Videojuego) obj;
                    bw.write(vj.getId() + SEPARADOR + vj.getNombre() + SEPARADOR + vj.getGenero() + 
                             SEPARADOR + vj.getPlataforma() + SEPARADOR + vj.getPuntuacion() + 
                             SEPARADOR + vj.getDescargas() + SEPARADOR + vj.getDesarrollador());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Videojuego[] cargarArchivo(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new Videojuego[0];
        }

        int lineas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.readLine() != null) {
                lineas++;
            }
        } catch (IOException e) {
            return new Videojuego[0];
        }

        Videojuego[] juegos = new Videojuego[lineas];
        
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|"); 
                if (partes.length >= 7) {
                    juegos[i++] = new Videojuego(
                        partes[0], partes[1], partes[2], partes[3], 
                        Double.parseDouble(partes[4]), Integer.parseInt(partes[5]), 
                        "N/A", partes[6], "N/A", 0.0, "N/A", false, 0.0
                    );
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error procesando datos: " + e.getMessage());
        }
        return juegos;
    }
    
    public Videojuego[] cargarHistorial() {
        return cargarArchivo(HISTORIAL_FILE);
    }

    public Videojuego[] cargarDescargas() {
        return cargarArchivo(DESCARGAS_FILE);
    }
}