package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Model.Videojuego;
import Structures.Queue;
import Structures.QueueCustom;

//Eduardo Motta
public class CSVVideojuegoLoader implements DataLoader<Videojuego> {

    @Override
    public Queue<Videojuego> loadData(String filePath) {
        Queue<Videojuego> bufferQueue = new QueueCustom<>();
        // RegEx: Divide por comas, excepto si la coma está entre comillas dobles
        String separador = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Saltamos cabecera

            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = linea.split(separador);
                    
                    // Sanitización total con trim() y limpieza de comillas extra
                    String id = datos[0].trim().replace("\"", "");
                    String nombre = datos[1].trim().replace("\"", "");
                    String genero = datos[2].trim().replace("\"", "");
                    String plataforma = datos[3].trim().replace("\"", "");
                    double puntuacion = Double.parseDouble(datos[4].trim());
                    int descargas = Integer.parseInt(datos[5].trim());
                    String fecha = datos[6].trim().replace("\"", "");
                    String desarrollador = datos[7].trim().replace("\"", "");
                    String editor = datos[8].trim().replace("\"", "");
                    double precio = Double.parseDouble(datos[9].trim());
                    String modo = datos[10].trim().replace("\"", "");
                    boolean multijugador = datos[11].trim().compareToIgnoreCase("Si") == 0;
                    double tamano = Double.parseDouble(datos[12].trim());

                    Videojuego juego = new Videojuego(id, nombre, genero, plataforma, puntuacion, 
                                                      descargas, fecha, desarrollador, editor, 
                                                      precio, modo, multijugador, tamano);
                    
                    bufferQueue.enqueue(juego);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Datos corruptos en la línea (se omitirá): " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error crítico de E/S al leer el dataset: " + e.getMessage());
        }
        
        return bufferQueue;
    }
}