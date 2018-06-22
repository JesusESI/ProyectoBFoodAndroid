package jesus.com.proyectobfoodandroid.Objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

import jesus.com.proyectobfoodandroid.DAO.UserDao;
import jesus.com.proyectobfoodandroid.Implementations.UserFirebaseImp;

public class User implements  Comparable<User>{

    // Atributos.
    private String nombre;
    private String apodo;
    private String posicion;
    private String email;
    private String imagen;
    private String eventosAceptados;
    private String eventosNoAceptados;
    private String puntos;
    private String logros;
    private String notificaciones;
    private UserDao userDao;
    private Uri uriImage;

    public User(String email) {
        this.email = email;
        this.userDao  = new UserFirebaseImp();
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

    public String getImagen() {
        return imagen;
    }

    public Uri getUriImage() {
        return uriImage;
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
        return new Integer(puntosA.compareTo(new Integer(puntosB)));
    }
}
