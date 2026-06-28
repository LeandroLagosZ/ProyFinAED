package Controller;

import Model.Videojuego;
import Structures.DoubleLinkedList;
import Exceptions.ItemDuplicated;
import Exceptions.ExceptionIsEmpty;


public class MainController {

    private DataController dataController;
    private CatalogoController catalogoController;
    private UserActionController userActionController;
    private RecommendationController recommendationController;
    private RankingController rankingController;

    public MainController() {
        this.dataController = new DataController();
        this.catalogoController = new CatalogoController(new Model.GestorCatalogosAVL(), new FiltroController());
        this.userActionController = new UserActionController();
        this.recommendationController = new RecommendationController();
        this.rankingController = new RankingController();
    }

    //Carga el CSV, indexa todos los videojuegos en el catálogo y construye el grafo de recomendaciones (RF07) con esos mismos datos.
    public void inicializarSistema(String rutaCSV) {
        dataController.cargarDatos(rutaCSV);
        DoubleLinkedList<Videojuego> catalogo = dataController.getCatalogoBase();

        Object[] juegos = catalogo.toArray();
        int duplicados = 0;
        for (Object obj : juegos) {
            try {
                catalogoController.registrarVideojuego((Videojuego) obj);
            } catch (ItemDuplicated e) {
                duplicados++;
            }
        }
        if (duplicados > 0) {
            System.out.println("Aviso: " + duplicados + " videojuego(s) con ID duplicado fueron omitidos.");
        }

        recommendationController.construirGrafo(catalogo);
        userActionController.cargarSesionUsuario();
    }

    //Top globales: usan el catálogo ya indexado, no el CSV crudo
    public Videojuego[] topPuntuacion(int cantidad) throws ExceptionIsEmpty {
        return rankingController.obtenerTopPuntuacion(catalogoController.mostrarTodos(), cantidad);
    }

    public Videojuego[] topDescargas(int cantidad) throws ExceptionIsEmpty {
        return rankingController.obtenerTopDescargas(catalogoController.mostrarTodos(), cantidad);
    }

    public Videojuego[] topPrecio(int cantidad) throws ExceptionIsEmpty {
        return rankingController.obtenerTopPrecio(catalogoController.mostrarTodos(), cantidad);
    }

    public DataController getDataController() {
        return dataController;
    }

    public CatalogoController getCatalogoController() {
        return catalogoController;
    }

    public UserActionController getUserActionController() {
        return userActionController;
    }

    public RecommendationController getRecommendationController() {
        return recommendationController;
    }

    public RankingController getRankingController() {
        return rankingController;
    }

    public void cerrarSistema() {
        userActionController.guardarSesionUsuario();
    }
}
