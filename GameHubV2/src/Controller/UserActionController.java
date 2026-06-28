package Controller;
import Structures.*;

import Structures.*;
import Exceptions.ExceptionIsEmpty;
import Model.*;

public class UserActionController {
    
    private StackLink<Videojuego> historial;
    private QueueCustom<Videojuego> colaDescargas;
    private SesionManager sessionManager;

    public UserActionController() {
        this.historial = new StackLink<>();
        this.colaDescargas = new QueueCustom<>();
        this.sessionManager = new SesionManager();
    }

    public void cargarSesionUsuario() {
        Videojuego[] historialGuardado = sessionManager.cargarHistorial();
        for (int i = historialGuardado.length - 1; i >= 0; i--) {
            historial.push(historialGuardado[i]);
        }

        Videojuego[] descargasGuardadas = sessionManager.cargarDescargas();
        for (int i = 0; i < descargasGuardadas.length; i++) {
            colaDescargas.enqueue(descargasGuardadas[i]);
        }
    }

    public void guardarSesionUsuario() {
        sessionManager.guardarSesion(historial.toArray(), colaDescargas.toArray());
    }

    public void registrarJuegoJugado(Videojuego juego) {
        historial.push(juego);
    }

    public Videojuego obtenerUltimoJugado() throws ExceptionIsEmpty {
        return historial.top();
    }

    public void vaciarHistorial() {
        historial.clear();
    }

    public int obtenerTamanoHistorial() {
        return historial.size();
    }

    public Object[] obtenerHistorialAsArray() {
        return historial.toArray();
    }

    public void encolarDescarga(Videojuego juego) {
        colaDescargas.enqueue(juego);
    }

    public Videojuego procesarSiguienteDescarga() throws ExceptionIsEmpty {
        return colaDescargas.dequeue();
    }

    public Videojuego verSiguienteDescarga() throws ExceptionIsEmpty {
        return colaDescargas.front();
    }

    public int obtenerCantidadDescargas() {
        return colaDescargas.getSize();
    }
}