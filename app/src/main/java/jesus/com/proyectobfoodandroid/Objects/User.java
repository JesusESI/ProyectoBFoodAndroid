package jesus.com.proyectobfoodandroid.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import jesus.com.proyectobfoodandroid.DAO.UserDao;
import jesus.com.proyectobfoodandroid.Implementations.UserFirebaseImp;

public class User {

    // Atributos.
    private String nombre;
    private  String apodo;
    private String email;
    private String imagen;
    private String eventosAceptados;
    private String eventosNoAceptados;
    private UserDao userDao;

    public User(String email) {
        this.email = email;
        this.userDao  = new UserFirebaseImp();
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public String getApodo() {
        userDao.getApodo(email);
        return apodo;
    }

}
