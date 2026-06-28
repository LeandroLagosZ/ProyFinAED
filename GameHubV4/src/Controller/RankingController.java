package Controller;
//Por Leandro
import Model.Videojuego;
import Structures.MaxHeap;
import Exceptions.ExceptionIsEmpty;

public class RankingController {

    private class NodoPuntuacion implements Comparable<NodoPuntuacion> {
        Videojuego juego;
        public NodoPuntuacion(Videojuego juego) { this.juego = juego; }
        @Override
        public int compareTo(NodoPuntuacion otro) {
            return Double.compare(this.juego.getPuntuacion(), otro.juego.getPuntuacion());
        }
    }

    private class NodoDescargas implements Comparable<NodoDescargas> {
        Videojuego juego;
        public NodoDescargas(Videojuego juego) { this.juego = juego; }
        @Override
        public int compareTo(NodoDescargas otro) {
            return Integer.compare(this.juego.getDescargas(), otro.juego.getDescargas());
        }
    }

    private class NodoPrecio implements Comparable<NodoPrecio> {
        Videojuego juego;
        public NodoPrecio(Videojuego juego) { this.juego = juego; }
        @Override
        public int compareTo(NodoPrecio otro) {
            return Double.compare(this.juego.getPrecio(), otro.juego.getPrecio());
        }
    }

    public Videojuego[] obtenerTopPrecio(Object[] catalogoEntero, int cantidad) throws ExceptionIsEmpty {
        MaxHeap<NodoPrecio> heapPrecio = new MaxHeap<>();
        for (Object obj : catalogoEntero) {
            heapPrecio.insert(new NodoPrecio((Videojuego) obj));
        }

        int tamanoResult = Math.min(cantidad, heapPrecio.size());
        Videojuego[] top = new Videojuego[tamanoResult];
        for (int i = 0; i < tamanoResult; i++) {
            top[i] = heapPrecio.extractMax().juego;
        }
        return top;
    }

    public Videojuego[] obtenerTopPuntuacion(Object[] catalogoEntero, int cantidad) throws ExceptionIsEmpty {
        MaxHeap<NodoPuntuacion> heapPuntuacion = new MaxHeap<>();
        for (Object obj : catalogoEntero) {
            heapPuntuacion.insert(new NodoPuntuacion((Videojuego) obj));
        }

        int tamanoResult = Math.min(cantidad, heapPuntuacion.size());
        Videojuego[] top = new Videojuego[tamanoResult];
        for (int i = 0; i < tamanoResult; i++) {
            top[i] = heapPuntuacion.extractMax().juego;
        }
        return top;
    }

    public Videojuego[] obtenerTopDescargas(Object[] catalogoEntero, int cantidad) throws ExceptionIsEmpty {
        MaxHeap<NodoDescargas> heapDescargas = new MaxHeap<>();
        for (Object obj : catalogoEntero) {
            heapDescargas.insert(new NodoDescargas((Videojuego) obj));
        }

        int tamanoResult = Math.min(cantidad, heapDescargas.size());
        Videojuego[] top = new Videojuego[tamanoResult];
        for (int i = 0; i < tamanoResult; i++) {
            top[i] = heapDescargas.extractMax().juego;
        }
        return top;
    }
}