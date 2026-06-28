package Structures;

public class GrafoVertice<T> {

    private T dato;
    private ListaEnlazada<GrafoVertice<T>> adyacentes;

    public GrafoVertice(T dato) {
        this.dato = dato;
        this.adyacentes = new ListaEnlazada<>();
    }

    public T getDato() {
        return dato;
    }

    public ListaEnlazada<GrafoVertice<T>> getAdyacentes() {
        return adyacentes;
    }
}