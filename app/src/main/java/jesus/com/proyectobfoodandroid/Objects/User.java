package jesus.com.proyectobfoodandroid.Objects;

import android.support.annotation.NonNull;


public class User implements  Comparable<User>{

    // Atributos.
    private String key;
    private String nombre;
    private String apodo;
    private String posicion;
    private String email;
    private String eventosAceptados;
    private String eventosNoAceptados;
    private String puntos;


    public User(String email) {
        this.email = email;
    }

    // Contructor Ranking.
    public User(String apodo, String puntos, String email) {
        this.apodo = apodo;
        this.puntos = puntos;
        this.email = email;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public String getApodo() {
        return apodo;
    }

    public String getPuntos() {
        return puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public int compareTo(@NonNull User o) {

        User a = (User) o;
        Integer puntosA = Integer.parseInt(a.getPuntos());
        Integer puntosB =   Integer.parseInt(this.getPuntos());

        // Ordenamos a los usuarios según sus puntos en el ranking
        return new Integer(puntosB.compareTo(new Integer(puntosA)));
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEventosAceptados() {
        return eventosAceptados;
    }

    public void setEventosAceptados(String eventosAceptados) {
        this.eventosAceptados = eventosAceptados;
    }

    public String getEventosNoAceptados() {
        return eventosNoAceptados;
    }

    public void setEventosNoAceptados(String eventosNoAceptados) {
        this.eventosNoAceptados = eventosNoAceptados;
    }
}
