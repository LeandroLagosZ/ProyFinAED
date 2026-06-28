package Controller;

import Model.Videojuego;
import Structures.DoubleLinkedList;
import Structures.QueueCustom;
import Exceptions.ExceptionIsEmpty;
//Eduardo Motta y Leandro Lagos
// Controlador para gestionar el filtrado de videojuegos usando estructuras de datos propias
public class FiltroController {

    public Videojuego[] filtrarPorGenero(DoubleLinkedList<Videojuego> catalogo, String genero) {
        return filtrar(catalogo, v -> v.getGenero() != null && v.getGenero().trim().equalsIgnoreCase(genero.trim()));
    }

    public Videojuego[] filtrarPorPlataforma(DoubleLinkedList<Videojuego> catalogo, String plataforma) {
        return filtrar(catalogo, v -> v.getPlataforma() != null && v.getPlataforma().trim().equalsIgnoreCase(plataforma.trim()));
    }

    public Videojuego[] filtrarPorRangoPrecio(DoubleLinkedList<Videojuego> catalogo, double min, double max) {
        return filtrar(catalogo, v -> v.getPrecio() >= min && v.getPrecio() <= max);
    }

    public Videojuego[] filtrarPorPuntuacionMinima(DoubleLinkedList<Videojuego> catalogo, double minimo) {
        return filtrar(catalogo, v -> v.getPuntuacion() >= minimo);
    }

    public Videojuego[] filtrarPorMultijugador(DoubleLinkedList<Videojuego> catalogo, boolean multijugador) {
        return filtrar(catalogo, v -> v.isMultijugador() == multijugador);
    }

    public Videojuego[] filtrarPorTamañoMaximo(DoubleLinkedList<Videojuego> catalogo, double maxGb) {
        return filtrar(catalogo, v -> v.getTamanoGb() <= maxGb);
    }

    public Videojuego[] filtrarPorFechaLanzamiento(DoubleLinkedList<Videojuego> catalogo, String anio) {
        return filtrar(catalogo, v -> v.getFechaLanzamiento() != null && v.getFechaLanzamiento().contains(anio.trim()));
    }

    public Videojuego[] filtrarPorDesarrollador(DoubleLinkedList<Videojuego> catalogo, String desarrollador) {
        return filtrar(catalogo, v -> v.getDesarrollador() != null && v.getDesarrollador().trim().equalsIgnoreCase(desarrollador.trim()));
    }

    public Videojuego[] filtrarPorEditor(DoubleLinkedList<Videojuego> catalogo, String editor) {
        return filtrar(catalogo, v -> v.getEditor() != null && v.getEditor().trim().equalsIgnoreCase(editor.trim()));
    }

    // Criterio de filtrado funcional
    private interface Criterio {
        boolean cumple(Videojuego v);
    }

    // Aplica el filtro recorriendo la lista propia y usando una cola personalizada como buffer
    private Videojuego[] filtrar(DoubleLinkedList<Videojuego> catalogo, Criterio criterio) {
        Object[] todos = catalogo.toArray();
        QueueCustom<Videojuego> colaResultados = new QueueCustom<>();

        // Recorrer catalogo y encolar resultados que cumplen el criterio
        for (Object o : todos) {
            Videojuego v = (Videojuego) o;
            if (criterio.cumple(v)) {
                colaResultados.enqueue(v);
            }
        }

        // Copiar los resultados de la cola a un arreglo final
        int count = colaResultados.getSize();
        Videojuego[] resultado = new Videojuego[count];
        try {
            for (int i = 0; i < count; i++) {
                resultado[i] = colaResultados.dequeue();
            }
        } catch (ExceptionIsEmpty e) {
            // Fin del recorrido
        }
        return resultado;
    }
}

