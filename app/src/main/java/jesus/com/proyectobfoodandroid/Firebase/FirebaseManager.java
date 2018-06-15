package jesus.com.proyectobfoodandroid.Firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import jesus.com.proyectobfoodandroid.LoginActivity;
import jesus.com.proyectobfoodandroid.R;
import jesus.com.proyectobfoodandroid.RegisterActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseManager {

    // Singleton
    static FirebaseManager firebaseSingleton = null;

    // Definimos las variables que contienen los servicios de Firebase.
    private FirebaseAuth mAuth;


    // Constructor.
    private FirebaseManager() {
        // Inicializamos los servicios.
        mAuth = FirebaseAuth.getInstance();

    }

    // Método que devuelve la única intancia que el servicio puede tener.
    static public FirebaseManager getFirebaseSingleton() {
        if( firebaseSingleton == null){
            firebaseSingleton = new FirebaseManager();
        }
        return firebaseSingleton;
    }

    // Método que comprueba ya hay un usuario autenticado.
    public boolean comprobarUsuario() {
        boolean userAuthenticated = false;

        if (mAuth.getCurrentUser() != null) {
            userAuthenticated = true;
        }
        return userAuthenticated;
    }

    // Método que permite entrar a la aplicación
    public FirebaseAuth signIn(String email, String password, final Context context) {

        // Comprobamos que los campos esten llenos para poder iniciar.
        FirebaseAuth auth = mAuth;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, R.string.enterEmailMessage , Toast.LENGTH_SHORT).show();
            auth = null;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.enterPasswordMessage, Toast.LENGTH_SHORT).show();
            auth = null;
        }

        return auth;
    }

    // Método para registrar nuevos usuarios.
    public FirebaseAuth signUp(String email, String password, final Context context) {

        // Comprobaciones de los campos.
        FirebaseAuth auth = mAuth;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        return auth;
    }


}
