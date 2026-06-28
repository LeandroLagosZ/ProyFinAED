package Controller;
//Por Lwandro Lagos
import Model.Videojuego;
import Structures.GrafoGenerico;
import Structures.CircularDoubleLinkedList;
import Structures.DoubleLinkedList;
import Exceptions.ExceptionIsEmpty;

public class RecommendationController {
    
    private GrafoGenerico<Videojuego> grafoSimilitud;
    private CircularDoubleLinkedList<Videojuego> carruselRecomendados;

    public RecommendationController() {
        this.grafoSimilitud = new GrafoGenerico<>();
        this.carruselRecomendados = new CircularDoubleLinkedList<>();
    }

    public void construirGrafo(DoubleLinkedList<Videojuego> catalogo) {
        Object[] juegos = catalogo.toArray();
        for (Object obj : juegos) {
            grafoSimilitud.agregarVertice((Videojuego) obj);
        }

        for (int i = 0; i < juegos.length; i++) {
            Videojuego v1 = (Videojuego) juegos[i];
            for (int j = i + 1; j < juegos.length; j++) {
                Videojuego v2 = (Videojuego) juegos[j];
                
                double similitud = 0;
                if (v1.getGenero().equals(v2.getGenero())) 
                	similitud += 2.0;
                if (v1.getDesarrollador().equals(v2.getDesarrollador())) 
                	similitud += 1.5;

                if (similitud > 0) {
                    grafoSimilitud.agregarArista(v1, v2, similitud);
                }
            }
        }
    }

    public void generarCarruselParaJuego(Videojuego juegoBase) {
        carruselRecomendados.clear();
        Object[] similares = grafoSimilitud.obtenerAdyacentes(juegoBase);
        
        for (Object obj : similares) {
            carruselRecomendados.insert((Videojuego) obj);
        }
    }

    public Videojuego getSiguienteRecomendacion() throws ExceptionIsEmpty {
        return carruselRecomendados.getNextRecommended();
    }

    public Videojuego getAnteriorRecomendacion() throws ExceptionIsEmpty {
        return carruselRecomendados.getPrevRecommended();
    }
}