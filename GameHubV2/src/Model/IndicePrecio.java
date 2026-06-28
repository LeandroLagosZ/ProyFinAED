package Model;

public class IndicePrecio implements Comparable<IndicePrecio> {
    private double precio;
    private Videojuego videojuego;

    public IndicePrecio(double precio, Videojuego videojuego) {
        this.precio = precio;
        this.videojuego = videojuego;
    }

    public Videojuego getVideojuego() { return videojuego; }

    @Override
    public int compareTo(IndicePrecio otro) {
        int cmp = Double.compare(this.precio, otro.precio);
        if (cmp == 0 && this.videojuego != null && otro.videojuego != null) {
            return this.videojuego.getId().compareTo(otro.videojuego.getId());
        }
        return cmp;
    }
}
