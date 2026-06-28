package Model;

public class IndiceDescargas implements Comparable<IndiceDescargas> {
    private int descargas;
    private Videojuego videojuego;

    public IndiceDescargas(int descargas, Videojuego videojuego) {
        this.descargas = descargas;
        this.videojuego = videojuego;
    }

    public Videojuego getVideojuego() { return videojuego; }

    @Override
    public int compareTo(IndiceDescargas otro) {
        int cmp = Integer.compare(this.descargas, otro.descargas);
        if (cmp == 0 && this.videojuego != null && otro.videojuego != null) {
            return this.videojuego.getId().compareTo(otro.videojuego.getId());
        }
        return cmp;
    }
}