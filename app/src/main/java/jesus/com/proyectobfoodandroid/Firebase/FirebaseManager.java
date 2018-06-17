package jesus.com.proyectobfoodandroid.Firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirebaseManager {

    // Singleton
    static FirebaseManager firebaseSingleton = null;

    // Definimos las variables que contienen los servicios de Firebase.
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;


    // Constructor.
    private FirebaseManager() {
        // Inicializamos los servicios.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
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
    public FirebaseAuth signUp(String nombre, String apellidos , String email, String password, String repeatPassword, final Context context) {
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

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(context, "Enter nombre!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        if (TextUtils.isEmpty(apellidos)) {
            Toast.makeText(context, "Enter nombre!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        if (TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(context, "Enter nombre!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        // COntraseñas distintas.
        if (!password.equals(repeatPassword)) {
            Toast.makeText(context, "Password and RepeatPassword fields must be equals.!", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        return auth;
    }

    public void saveUser(String nombre, String surname, String email, String usuario, String imagen) {
        // Método que guardará a los usuarios registrados en la base de datos además de autenticarlos.
        DatabaseReference ref = mDatabase.getReference("Usuarios");
        // Soporta el tipo de datos Map <calve, valor>.
        Map nuevo = new HashMap();

        nuevo.put("nombre", nombre);
        nuevo.put("apellidos", surname);
        nuevo.put("email", email);
        nuevo.put("apodo", usuario);
        nuevo.put("imagen", imagen);

        // Guardamos dentro de usuarios el objeto.
        ref.push().setValue(nuevo);

    }

    public void readUsers() {
        // Crearemos un eventListener para tener los datos continuamente actualizados en tiempo real.
        DatabaseReference ref = mDatabase.getReference("Usuarios");
        // Listener.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Log.d(TAG, data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

}
