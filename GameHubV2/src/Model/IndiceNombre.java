package Model;

public class IndiceNombre implements Comparable<IndiceNombre> {
    private String nombre;
    private Videojuego videojuego;

    public IndiceNombre(String nombre, Videojuego videojuego) {
        this.nombre = nombre;
        this.videojuego = videojuego;
    }

    public Videojuego getVideojuego() { return videojuego; }

    @Override
    public int compareTo(IndiceNombre otro) {
        int cmp = this.nombre.compareToIgnoreCase(otro.nombre);
        if (cmp == 0 && this.videojuego != null && otro.videojuego != null) {
            return this.videojuego.getId().compareTo(otro.videojuego.getId());
        }
        return cmp;
    }
}