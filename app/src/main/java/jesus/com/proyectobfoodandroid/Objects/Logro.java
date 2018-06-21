package jesus.com.proyectobfoodandroid.Objects;

public class Logro {

    private String nombre;
    private String descripcion;
    private String puntos;


    private String conseguido;

    public Logro(String nombre, String descripcion, String puntos){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPuntos() {
        return puntos;
    }


}
