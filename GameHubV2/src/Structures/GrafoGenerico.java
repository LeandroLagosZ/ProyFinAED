package Structures;

public class GrafoGenerico<T> {

    private ListaEnlazada<GrafoVertice<T>> vertices;

    public GrafoGenerico() {
        vertices = new ListaEnlazada<>();
    }

    public void agregarVertice(T dato) {
        if (buscarVertice(dato) == null) {
            vertices.insertar(new GrafoVertice<>(dato)); 
        }
    }

    public void agregarArista(T origen, T destino) {
        GrafoVertice<T> vOrigen = buscarVertice(origen);
        GrafoVertice<T> vDestino = buscarVertice(destino);

        if (vOrigen != null && vDestino != null) {
            vOrigen.getAdyacentes().insertar(vDestino); 
        }
    }

    public GrafoVertice<T> buscarVertice(T dato) {
        for (int i = 0; i < vertices.getSize(); i++) {
            GrafoVertice<T> v = vertices.buscar(i);
            if (v.getDato().equals(dato)) {
                return v;
            }
        }
        return null;
    }

    public void mostrar() {
        for (int i = 0; i < vertices.getSize(); i++) {
            GrafoVertice<T> v = vertices.buscar(i);
            System.out.print(v.getDato() + " -> ");

            ListaEnlazada<GrafoVertice<T>> ady = v.getAdyacentes();
            for (int j = 0; j < ady.getSize(); j++) {
                System.out.print(ady.buscar(j).getDato() + " ");
            }
            System.out.println();
        }
    }
    
    public void agregarArista(T origen, T destino, double peso) {
        GrafoVertice<T> vOrigen = buscarVertice(origen);
        GrafoVertice<T> vDestino = buscarVertice(destino);
        if (vOrigen != null && vDestino != null) {
            vOrigen.getAdyacentes().insertar(vDestino); 
        }
    }

    public Object[] obtenerAdyacentes(T dato) {
        GrafoVertice<T> v = buscarVertice(dato);
        if (v == null) return new Object[0];

        ListaEnlazada<GrafoVertice<T>> ady = v.getAdyacentes();
        Object[] resultados = new Object[ady.getSize()];
        for(int i = 0; i < ady.getSize(); i++) {
            resultados[i] = ady.buscar(i).getDato();
        }
        return resultados;
    }
}