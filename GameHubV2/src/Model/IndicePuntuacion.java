package Model;

public class IndicePuntuacion implements Comparable<IndicePuntuacion> {
    private double puntuacion;
    private Videojuego videojuego;

    public IndicePuntuacion(double puntuacion, Videojuego videojuego) {
        this.puntuacion = puntuacion;
        this.videojuego = videojuego;
    }

    public Videojuego getVideojuego() { return videojuego; }

    @Override
    public int compareTo(IndicePuntuacion otro) {
        int cmp = Double.compare(this.puntuacion, otro.puntuacion);
        if (cmp == 0 && this.videojuego != null && otro.videojuego != null) {
            return this.videojuego.getId().compareTo(otro.videojuego.getId());
        }
        return cmp;
    }
}