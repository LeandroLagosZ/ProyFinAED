package Controller;
//Leandro Lagos
import Model.Videojuego;
import Structures.StackLink;
import Structures.QueueCustom;
import Structures.DoubleLinkedList;
import Model.SesionManager;
import Exceptions.ExceptionIsEmpty;

public class UserActionController {
    
    private StackLink<Videojuego> historial;
    private QueueCustom<Videojuego> colaDescargas;
    private DoubleLinkedList<Videojuego> favoritos;
    private SesionManager sessionManager;

    public UserActionController() {
        this.historial = new StackLink<>();
        this.colaDescargas = new QueueCustom<>();
        this.favoritos = new DoubleLinkedList<>();
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

        Videojuego[] favoritosGuardados = sessionManager.cargarFavoritos();
        for (int i = 0; i < favoritosGuardados.length; i++) {
            favoritos.insertLast(favoritosGuardados[i]);
        }
    }

    public void guardarSesionUsuario() {
        sessionManager.guardarSesion(historial.toArray(), colaDescargas.toArray(), favoritos.toArray());
    }

    public void registrarJuegoJugado(Videojuego juego) {
        historial.push(juego);
        guardarSesionUsuario();
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
        guardarSesionUsuario();
    }

    public Videojuego procesarSiguienteDescarga() throws ExceptionIsEmpty {
        Videojuego v = colaDescargas.dequeue();
        guardarSesionUsuario();
        return v;
    }

    public Videojuego verSiguienteDescarga() throws ExceptionIsEmpty {
        return colaDescargas.front();
    }

    public int obtenerCantidadDescargas() {
        return colaDescargas.getSize();
    }

    // Gestión de favoritos usando DoubleLinkedList propia (RF-FAVORITOS)
    public void agregarFavorito(Videojuego juego) {
        if (juego == null) return;
        if (!esFavorito(juego.getId())) {
            favoritos.insertLast(juego);
            guardarSesionUsuario();
        }
    }

    public void eliminarFavorito(Videojuego juego) {
        if (juego == null) return;
        try {
            favoritos.remove(juego);
            guardarSesionUsuario();
        } catch (Exception e) {
            // No se encontró o ya fue removido
        }
    }

    public boolean esFavorito(String juegoId) {
        if (juegoId == null) return false;
        Object[] arr = favoritos.toArray();
        for (Object obj : arr) {
            Videojuego v = (Videojuego) obj;
            if (v != null && v.getId().equals(juegoId)) {
                return true;
            }
        }
        return false;
    }

    public Videojuego[] obtenerFavoritos() {
        Object[] arr = favoritos.toArray();
        Videojuego[] res = new Videojuego[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = (Videojuego) arr[i];
        }
        return res;
    }
}