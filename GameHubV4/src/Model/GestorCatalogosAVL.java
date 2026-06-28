package Model;

import Structures.AVLTree;
import Structures.HashTable;
import Structures.DoubleLinkedList;
import Exceptions.ExceptionIsEmpty;
import Exceptions.ItemDuplicated;
import Exceptions.ItemNotFoundException;


public class GestorCatalogosAVL {

//Estructuras no lineales (árboles AVL)
    private AVLTree<Videojuego> arbolPorID;
    private AVLTree<IndiceNombre> arbolPorNombre;
    private AVLTree<IndicePuntuacion> arbolPorPuntuacion;
    private AVLTree<IndiceDescargas> arbolPorDescargas;
    private AVLTree<IndicePrecio> arbolPorPrecio;

    //Estructura no lineal (tabla hash) para búsquedas por campos no únicos
    private HashTable<String, DoubleLinkedList<Videojuego>> indicePorGenero;
    private HashTable<String, DoubleLinkedList<Videojuego>> indicePorDesarrollador;
    private HashTable<String, DoubleLinkedList<Videojuego>> indicePorEditor;

    // --- Estructura lineal: catálogo maestro (recorrido completo / filtros)
    private DoubleLinkedList<Videojuego> catalogoCompleto;

    private static final int CAPACIDAD_HASH = 211; // primo cercano a 200, reduce colisiones

    public GestorCatalogosAVL() {
        arbolPorID = new AVLTree<>();
        arbolPorNombre = new AVLTree<>();
        arbolPorPuntuacion = new AVLTree<>();
        arbolPorDescargas = new AVLTree<>();
        arbolPorPrecio = new AVLTree<>();

        indicePorGenero = new HashTable<>(CAPACIDAD_HASH);
        indicePorDesarrollador = new HashTable<>(CAPACIDAD_HASH);
        indicePorEditor = new HashTable<>(CAPACIDAD_HASH);

        catalogoCompleto = new DoubleLinkedList<>();
    }

    //Registrar / Modificar / Eliminar / Mostrar

    public void registrarVideojuego(Videojuego v) throws ItemDuplicated {
        if (buscarPorID(v.getId()) != null) {
            throw new ItemDuplicated("Ya existe un videojuego con el ID: " + v.getId());
        }
        insertarEnIndices(v);
    }

    private void insertarEnIndices(Videojuego v) {
        arbolPorID.insertar(v);
        arbolPorNombre.insertar(new IndiceNombre(v.getNombre(), v));
        arbolPorPuntuacion.insertar(new IndicePuntuacion(v.getPuntuacion(), v));
        arbolPorDescargas.insertar(new IndiceDescargas(v.getDescargas(), v));
        arbolPorPrecio.insertar(new IndicePrecio(v.getPrecio(), v));

        agregarAHash(indicePorGenero, v.getGenero(), v);
        agregarAHash(indicePorDesarrollador, v.getDesarrollador(), v);
        agregarAHash(indicePorEditor, v.getEditor(), v);

        catalogoCompleto.insertLast(v);
    }

    private void agregarAHash(HashTable<String, DoubleLinkedList<Videojuego>> tabla, String clave, Videojuego v) {
        if (clave == null) return;
        DoubleLinkedList<Videojuego> lista = tabla.get(clave);
        if (lista == null) {
            lista = new DoubleLinkedList<>();
            tabla.put(clave, lista);
        }
        lista.insertLast(v);
    }

    public void eliminarVideojuego(String id) throws ItemNotFoundException {
        Videojuego v = buscarPorID(id);
        if (v == null) {
            throw new ItemNotFoundException("No existe un videojuego con el ID: " + id);
        }
        eliminarDeIndices(v);
    }

    private void eliminarDeIndices(Videojuego v) {
        arbolPorID.eliminar(v);
        arbolPorNombre.eliminar(new IndiceNombre(v.getNombre(), v));
        arbolPorPuntuacion.eliminar(new IndicePuntuacion(v.getPuntuacion(), v));
        arbolPorDescargas.eliminar(new IndiceDescargas(v.getDescargas(), v));
        arbolPorPrecio.eliminar(new IndicePrecio(v.getPrecio(), v));

        quitarDeHash(indicePorGenero, v.getGenero(), v);
        quitarDeHash(indicePorDesarrollador, v.getDesarrollador(), v);
        quitarDeHash(indicePorEditor, v.getEditor(), v);

        try {
            catalogoCompleto.remove(v);
        } catch (Exception e) {
            // Si no estaba en la lista maestra no hay nada más que hacer
        }
    }

    private void quitarDeHash(HashTable<String, DoubleLinkedList<Videojuego>> tabla, String clave, Videojuego v) {
        if (clave == null) return;
        DoubleLinkedList<Videojuego> lista = tabla.get(clave);
        if (lista != null) {
            try {
                lista.remove(v);
            } catch (Exception e) {
                // El juego ya no estaba en esa lista, se ignora
            }
        }
    }

    public void modificarVideojuego(String id, Videojuego datosNuevos) throws ItemNotFoundException {
        Videojuego actual = buscarPorID(id);
        if (actual == null) {
            throw new ItemNotFoundException("No existe un videojuego con el ID: " + id);
        }
        eliminarDeIndices(actual);
        datosNuevos.setId(id); // el ID nunca cambia
        insertarEnIndices(datosNuevos);
    }

    public Videojuego[] mostrarTodos() {
        Object[] arr = catalogoCompleto.toArray();
        Videojuego[] resultado = new Videojuego[arr.length];
        for (int i = 0; i < arr.length; i++) resultado[i] = (Videojuego) arr[i];
        return resultado;
    }

    public int cantidadJuegos() {
        return catalogoCompleto.size();
    }

    public DoubleLinkedList<Videojuego> getCatalogoCompleto() {
        return catalogoCompleto;
    }

    // Búsquedas

    public Videojuego buscarPorID(String id) {
        Videojuego dummy = new Videojuego();
        dummy.setId(id);
        return arbolPorID.buscarObjeto(dummy);
    }

    public Videojuego buscarPorNombre(String nombre) {
        IndiceNombre busqueda = new IndiceNombre(nombre, null);
        IndiceNombre resultado = arbolPorNombre.buscarObjeto(busqueda);
        return (resultado != null) ? resultado.getVideojuego() : null;
    }

    public Videojuego[] buscarPorGenero(String genero) {
        return listaAArray(indicePorGenero.get(genero));
    }

    public Videojuego[] buscarPorDesarrollador(String desarrollador) {
        return listaAArray(indicePorDesarrollador.get(desarrollador));
    }

    public Videojuego[] buscarPorEditor(String editor) {
        return listaAArray(indicePorEditor.get(editor));
    }

    private Videojuego[] listaAArray(DoubleLinkedList<Videojuego> lista) {
        if (lista == null) return new Videojuego[0];
        Object[] arr = lista.toArray();
        Videojuego[] resultado = new Videojuego[arr.length];
        for (int i = 0; i < arr.length; i++) resultado[i] = (Videojuego) arr[i];
        return resultado;
    }

    // ================= RF06: Rankings (apoyo para RankingController) =================

    public Videojuego[] obtenerTopPuntuacion(int limite) {
        return limpiar(arbolPorPuntuacion.extraerTopDescendente(limite), TipoIndice.PUNTUACION);
    }

    public Videojuego[] obtenerTopDescargas(int limite) {
        return limpiar(arbolPorDescargas.extraerTopDescendente(limite), TipoIndice.DESCARGAS);
    }

    public Videojuego[] obtenerTopPrecio(int limite) {
        return limpiar(arbolPorPrecio.extraerTopDescendente(limite), TipoIndice.PRECIO);
    }

    private enum TipoIndice { PUNTUACION, DESCARGAS, PRECIO }

    private Videojuego[] limpiar(Object[] arreglo, TipoIndice tipo) {
        int count = 0;
        for (Object o : arreglo) if (o != null) count++;
        Videojuego[] resultado = new Videojuego[count];
        for (int i = 0; i < count; i++) {
            switch (tipo) {
                case PUNTUACION: resultado[i] = ((IndicePuntuacion) arreglo[i]).getVideojuego(); break;
                case DESCARGAS:  resultado[i] = ((IndiceDescargas) arreglo[i]).getVideojuego(); break;
                case PRECIO:     resultado[i] = ((IndicePrecio) arreglo[i]).getVideojuego(); break;
            }
        }
        return resultado;
    }
}
