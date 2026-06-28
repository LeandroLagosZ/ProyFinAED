package Controller;

import Model.GestorCatalogosAVL;
import Model.Videojuego;
import Exceptions.ItemDuplicated;
import Exceptions.ItemNotFoundException;

//Eduardo Motta y Leandro Lagos
// Controlador del catalogo para comunicar la vista con el gestor de indices AVL y Hash
public class CatalogoController {

    private GestorCatalogosAVL gestor;
    private FiltroController filtroController;

    public CatalogoController(GestorCatalogosAVL gestor, FiltroController filtroController) {
        this.gestor = gestor;
        this.filtroController = filtroController;
    }

    public void registrarVideojuego(Videojuego v) throws ItemDuplicated {
        gestor.registrarVideojuego(v);
    }

    public void modificarVideojuego(String id, Videojuego datosNuevos) throws ItemNotFoundException {
        gestor.modificarVideojuego(id, datosNuevos);
    }

    public void eliminarVideojuego(String id) throws ItemNotFoundException {
        gestor.eliminarVideojuego(id);
    }

    public Videojuego[] mostrarTodos() {
        return gestor.mostrarTodos();
    }

    public int cantidadJuegos() {
        return gestor.cantidadJuegos();
    }

    public Videojuego buscarPorID(String id) {
        return gestor.buscarPorID(id);
    }

    public Videojuego buscarPorNombre(String nombre) {
        return gestor.buscarPorNombre(nombre);
    }

    public Videojuego[] buscarPorGenero(String genero) {
        return gestor.buscarPorGenero(genero);
    }

    public Videojuego[] buscarPorDesarrollador(String desarrollador) {
        return gestor.buscarPorDesarrollador(desarrollador);
    }

    public Videojuego[] buscarPorEditor(String editor) {
        return gestor.buscarPorEditor(editor);
    }

    // Filtros avanzados
    
    public Videojuego[] filtrarPorGenero(String genero) {
        return filtroController.filtrarPorGenero(gestor.getCatalogoCompleto(), genero);
    }

    public Videojuego[] filtrarPorPlataforma(String plataforma) {
        return filtroController.filtrarPorPlataforma(gestor.getCatalogoCompleto(), plataforma);
    }

    public Videojuego[] filtrarPorRangoPrecio(double min, double max) {
        return filtroController.filtrarPorRangoPrecio(gestor.getCatalogoCompleto(), min, max);
    }

    public Videojuego[] filtrarPorPuntuacionMinima(double minimo) {
        return filtroController.filtrarPorPuntuacionMinima(gestor.getCatalogoCompleto(), minimo);
    }

    public Videojuego[] filtrarPorMultijugador(boolean multijugador) {
        return filtroController.filtrarPorMultijugador(gestor.getCatalogoCompleto(), multijugador);
    }

    public Videojuego[] filtrarPorTamañoMaximo(double maxGb) {
        return filtroController.filtrarPorTamañoMaximo(gestor.getCatalogoCompleto(), maxGb);
    }

    public Videojuego[] filtrarPorFechaLanzamiento(String anio) {
        return filtroController.filtrarPorFechaLanzamiento(gestor.getCatalogoCompleto(), anio);
    }

    public Videojuego[] filtrarPorDesarrollador(String desarrollador) {
        return filtroController.filtrarPorDesarrollador(gestor.getCatalogoCompleto(), desarrollador);
    }

    public Videojuego[] filtrarPorEditor(String editor) {
        return filtroController.filtrarPorEditor(gestor.getCatalogoCompleto(), editor);
    }

    public GestorCatalogosAVL getGestor() {
        return gestor;
    }
}