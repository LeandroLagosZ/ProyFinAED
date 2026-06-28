package Structures;

public class ListaEnlazada<T> {
    private Node<T> cabeza; 
    private int tamaño;

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public void insertar(T dato) { 
        Node<T> nuevo = new Node<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Node<T> actual = cabeza;
            while (actual.getNext() != null) { 
                actual = actual.getNext();
            }
            actual.setNext(nuevo);
        }
        tamaño++;
    }

    public T buscar(int indice) { 
        if (indice < 0 || indice >= tamaño) return null;
        Node<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getNext();
        }
        return actual.getData();
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.getData().equals(dato)) {
            cabeza = cabeza.getNext();
            tamaño--;
            return true;
        }

        Node<T> anterior = cabeza;
        Node<T> actual = cabeza.getNext();
        while (actual != null) {
            if (actual.getData().equals(dato)) {
                anterior.setNext(actual.getNext());
                tamaño--;
                return true;
            }
            anterior = actual;
            actual = actual.getNext();
        }
        return false;
    }

    public int getSize() { 
        return tamaño;
    }

    public Object[] toArray() { 
        Object[] array = new Object[tamaño];
        Node<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            array[i] = actual.getData();
            actual = actual.getNext();
        }
        return array;
    }
}