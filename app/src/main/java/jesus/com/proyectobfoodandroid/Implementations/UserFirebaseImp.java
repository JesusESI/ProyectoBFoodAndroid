package jesus.com.proyectobfoodandroid.Implementations;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.DAO.UserDao;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.User;

public class UserFirebaseImp implements UserDao {

    private List usuarios;

    public UserFirebaseImp() {
        this.usuarios = FirebaseManager.getFirebaseSingleton().readUsers();
    }

    @Override
    public String getApodo(String email) {
        String apodo = "";
        HashMap aux = null;

        for (Object user: usuarios) {
            aux = (HashMap) user;
            if (aux.get("email").equals(email)){
                apodo = (String )aux.get("apodo");
            }
            //Log.d("Dato", apodo);
        }
        return apodo;
    }

    @Override
    public String getNumeroPuntos(String email) {
        String puntos = "";
        HashMap aux = null;

        for (Object user: usuarios) {
            aux = (HashMap) user;
            if (aux.get("email").equals(email)){
                puntos = (String )aux.get("puntos");
            }
        }
        return puntos;
    }

    @Override
    public String getNumeroLogoros(String email) {
        return null;
    }

    @Override
    public String getPosicion(String email) {
        return null;
    }

    @Override
    public String getNotificaciones(String email) {
        return null;
    }
}
