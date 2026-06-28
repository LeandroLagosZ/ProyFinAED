package Model;

public class Videojuego implements Comparable<Videojuego> {

    private String id;
    private String nombre;
    private String genero;
    private String plataforma;
    private double puntuacion;
    private int descargas;
    private String fechaLanzamiento;
    private String desarrollador;
    private String editor;
    private double precio;
    private String modoJuego;
    private boolean multijugador;   
    private double tamanoGb;


    public Videojuego() {}


    public Videojuego(String id, String nombre, String genero, String plataforma, double puntuacion,
                      int descargas, String fechaLanzamiento, String desarrollador, String editor,
                      double precio, String modoJuego, boolean multijugador, double tamanoGb) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.plataforma = plataforma;
        this.puntuacion = puntuacion;
        this.descargas = descargas;
        this.fechaLanzamiento = fechaLanzamiento;
        this.desarrollador = desarrollador;
        this.editor = editor;
        this.precio = precio;
        this.modoJuego = modoJuego;
        this.multijugador = multijugador;
        this.tamanoGb = tamanoGb;
    }
    
    public Videojuego(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String obtenerId()                        
    { return id; }
    public void asignarId(String id)                 
    { this.id = id; }

    public String obtenerNombre()                    
    { return nombre; }
    public void asignarNombre(String nombre)         
    { this.nombre = nombre; }

    public String obtenerGenero()                    
    { return genero; }
    public void asignarGenero(String genero)         
    { this.genero = genero; }

    public String obtenerPlataforma()                
    { return plataforma; }
    public void asignarPlataforma(String plataforma) 
    { this.plataforma = plataforma; }

    public double obtenerPuntuacion()                
    { return puntuacion; }
    public void asignarPuntuacion(double puntuacion) 
    { this.puntuacion = puntuacion; }

    public int obtenerDescargas()                    
    { return descargas; }
    public void asignarDescargas(int descargas)      
    { this.descargas = descargas; }

    public String obtenerFechaLanzamiento()                        
    { return fechaLanzamiento; }
    public void asignarFechaLanzamiento(String fechaLanzamiento)   
    { this.fechaLanzamiento = fechaLanzamiento; }

    public String obtenerDesarrollador()                           
    { return desarrollador; }
    public void asignarDesarrollador(String desarrollador)         
    { this.desarrollador = desarrollador; }

    public String obtenerEditor()                    
    { return editor; }
    public void asignarEditor(String editor)         
    { this.editor = editor; }

    public double obtenerPrecio()                    
    { return precio; }
    public void asignarPrecio(double precio)         
    { this.precio = precio; }

    public String obtenerModoJuego()                 
    { return modoJuego; }
    public void asignarModoJuego(String modoJuego)   
    { this.modoJuego = modoJuego; }

    public boolean esMultijugador()                  
    { return multijugador; }     
    public void asignarMultijugador(boolean multijugador) 
    { this.multijugador = multijugador; }

    public double obtenerTamanoGb()                  
    { return tamanoGb; }
    public void asignarTamanoGb(double tamanoGb)     
    { this.tamanoGb = tamanoGb; }

  
    public String getId()                            
    { return id; }
    public void setId(String id)                     
    { this.id = id; }

    public String getNombre()                        
    { return nombre; }
    public void setNombre(String nombre)             
    { this.nombre = nombre; }

    public String getGenero()                        
    { return genero; }
    public void setGenero(String genero)             
    { this.genero = genero; }

    public String getPlataforma()                    
    { return plataforma; }
    public void setPlataforma(String plataforma)     
    { this.plataforma = plataforma; }

    public double getPuntuacion()                    
    { return puntuacion; }
    public void setPuntuacion(double puntuacion)     
    { this.puntuacion = puntuacion; }

    public int getDescargas()                        
    { return descargas; }
    public void setDescargas(int descargas)          
    { this.descargas = descargas; }

    public String getFechaLanzamiento()                      
    { return fechaLanzamiento; }
    public void setFechaLanzamiento(String fechaLanzamiento) 
    { this.fechaLanzamiento = fechaLanzamiento; }

    public String getDesarrollador()                         
    { return desarrollador; }
    public void setDesarrollador(String desarrollador)       
    { this.desarrollador = desarrollador; }

    public String getEditor()                        
    { return editor; }
    public void setEditor(String editor)             
    { this.editor = editor; }

    public double getPrecio()                        
    { return precio; }
    public void setPrecio(double precio)             
    { this.precio = precio; }

    public String getModoJuego()                     
    { return modoJuego; }
    public void setModoJuego(String modoJuego)       
    { this.modoJuego = modoJuego; }

    public boolean isMultijugador()                  
    { return multijugador; }
    public void setMultijugador(boolean multijugador)
    { this.multijugador = multijugador; }

    public double getTamanoGb()                      
    { return tamanoGb; }
    public void setTamanoGb(double tamanoGb)         
    { this.tamanoGb = tamanoGb; }


    @Override
    public String toString() {
        return nombre + " ~" + plataforma + "~ -> " + genero;
    }


    @Override
    public int compareTo(Videojuego otro) {
        return this.id.compareTo(otro.getId());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Videojuego)) return false;
        Videojuego otro = (Videojuego) obj;
        return this.id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
