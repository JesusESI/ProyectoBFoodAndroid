package jesus.com.proyectobfoodandroid.Firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import jesus.com.proyectobfoodandroid.LoginActivity;
import jesus.com.proyectobfoodandroid.MapsActivity;
import jesus.com.proyectobfoodandroid.Objects.Notificacion;
import jesus.com.proyectobfoodandroid.Objects.User;
import jesus.com.proyectobfoodandroid.R;
import jesus.com.proyectobfoodandroid.RegisterActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirebaseManager {

    // Singleton
    static FirebaseManager firebaseSingleton = null;

    // Definimos las variables que contienen los servicios de Firebase.
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;

    // UserLog.
    private String userLog;

    // Listado de datos.
    ArrayList usuarios = new ArrayList();
    ArrayList logros = new ArrayList();
    ArrayList eventos = new ArrayList();
    ArrayList notificacionesUser = new ArrayList();

    public FirebaseDatabase getmDatabase() {
        return mDatabase;
    }

    // Constructor.
    private FirebaseManager() {
        // Inicializamos los servicios.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
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

    // Método que da la información del usuario logueado.
    public String getLogUser() {
        return this.userLog;
    }

    public void setUserLog(String email) {
        this.userLog = email;
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

        if (password.length() < 6) {
            Toast.makeText(context, "El tamaño de la contraseña debe ser de al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            auth = null;
        }

        // Usuario logueado.
        this.userLog = email;

        return auth;
    }

    public void logOut() {
        this.userLog = null;
        mAuth.signOut();
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

    public HashMap getUser(String email) {
        List usuarios = readUsers();

        HashMap user = null;

        for (Object usuario : usuarios) {
            HashMap aux = (HashMap) usuario;

            if (aux.get("email").equals(email)) {
                user = aux;
            }
        }

        return user;
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

    public void addNotificationUser(Notificacion noti) {
        DatabaseReference ref = mDatabase.getReference("Notificaciones");

        Map nuevaNotificacion = new HashMap();

        nuevaNotificacion.put("emailUsuario", noti.getUsuarioNotificacion());
        nuevaNotificacion.put("titulo", noti.getTitulo());
        nuevaNotificacion.put("aceptado", noti.isAceptado());
        nuevaNotificacion.put("contenido", noti.getContenido());

        ref.push().setValue(nuevaNotificacion);

    }

    public List readUsers() {
            // Crearemos un eventListener para tener los datos continuamente actualizados en tiempo real.
            DatabaseReference ref = mDatabase.getReference("Usuarios");
            // Listener.
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d(TAG, data.getValue().toString());
                        usuarios.add(data.getValue());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        return usuarios;
    }

    public List readNotificationsUser(final String email) {
        DatabaseReference ref = mDatabase.getReference("Notificaciones");
        // Listener.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, data.getValue().toString());

                    // Comprobamos el email del usuario
                    HashMap aux = (HashMap) data.getValue();

                    String emailNotificacion = (String) aux.get("emailUsuario");

                    if (emailNotificacion.equals(email)) {
                        notificacionesUser.add(data.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return notificacionesUser;
    }



    public ArrayList readLogros() {
        if (logros.isEmpty()) {
            DatabaseReference ref = mDatabase.getReference("Logros");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d(TAG, data.getValue().toString());
                        logros.add(data.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }
        return logros;
    }

    public List readEventos() {
            // Crearemos un eventListener para tener los datos continuamente actualizados en tiempo real.
            DatabaseReference ref = mDatabase.getReference("Eventos");
            // Listener.
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d(TAG, data.getValue().toString());
                        eventos.add(data.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        return eventos;
    }

    public ArrayList readEventoNotificacion(final String titulo) {
        DatabaseReference ref = mDatabase.getReference("Eventos");

        // Listener.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, data.getValue().toString());
                    HashMap aux = (HashMap) data.getValue();

                    String nombreEvento = (String) aux.get("nombre");

                    if (nombreEvento.equals(titulo)) {
                        eventos.add(data.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return eventos;
    }

    public FirebaseStorage getmStorage() {
        return mStorage;
    }
}
