package Controller;

import Exceptions.ExceptionIsEmpty;
import Model.Videojuego;
import Structures.Queue;
import Structures.DoubleLinkedList;
import Util.DataLoader;
import Util.CSVVideojuegoLoader; 
//Eduardo Motta
public class DataController {
    private DataLoader<Videojuego> dataLoader;
    private DoubleLinkedList<Videojuego> catalogoBase;

    public DataController() {
        this.dataLoader = new CSVVideojuegoLoader(); 
        this.catalogoBase = new DoubleLinkedList<>();
    }

    public void cargarDatos(String ruta) {
        Queue<Videojuego> queue = dataLoader.loadData(ruta);

        while (!queue.isEmpty()) {
            try {
                catalogoBase.insertLast(queue.dequeue()); 
            } catch (ExceptionIsEmpty e) {
                System.err.println("Error al procesar la cola: " + e.getMessage());
            }
        }
    }

    public DoubleLinkedList<Videojuego> getCatalogoBase() {
        return catalogoBase;
    }
}