package jesus.com.proyectobfoodandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Notificacion;
import jesus.com.proyectobfoodandroid.Objects.User;

import static android.content.ContentValues.TAG;

public class ConfirmarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTitulo, textDescripcion;
    private Button siButton, noButton;

    private boolean respuesta;
    private String titulo;
    private String descripcion;
    private String emailNotificacion;
    private String tipo;
    private int puntosUsuario;
    private int aceptadasUsuario;
    private int noAceptadasUsuario;

    private Context instantanea;
    private SharedPreferences sharedVariables;
    private SharedPreferences.Editor editor;

    private Notificacion nuevaNotificacion;
    private User instaciaUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);

        // Obtenemos los datos del intent.
        sharedVariables = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
        titulo = sharedVariables.getString("titulo", "titulo");
        descripcion = sharedVariables.getString("descripcion", "descripcion");
        emailNotificacion = sharedVariables.getString("emailNotificacion", "emailNotificacion");
        tipo = sharedVariables.getString("tipo", "tipo");

        // Pasamos el nombre de la notificación para posteriormente cargar el evento al que pertenece.
        editor = sharedVariables.edit();
        editor.putString("nombreEvento", titulo);
        editor.commit();

        // Obtener usuario.
        instaciaUser = new User(emailNotificacion);
        obtenerDatosUsuario();

        // Inicialización de elementos.
        textTitulo = findViewById(R.id.textTitulo);
        textDescripcion = findViewById(R.id.textDescripcion);
        siButton = findViewById(R.id.siButton);
        noButton = findViewById(R.id.noButton);

        siButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        // Actualizamos el texto.
        textTitulo.setText(titulo);
        textDescripcion.setText(descripcion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.siButton:
                respuesta = true;
                // Crear la notificacion del usuario.
                nuevaNotificacion = new Notificacion(emailNotificacion,titulo, respuesta, descripcion);
                // Actualizamos las notificaciones del usuario.
                FirebaseManager.getFirebaseSingleton().addNotificationUser(nuevaNotificacion);
                // Damos los logros si se cumplen las condiciones de estos.
                darLogro();

                FirebaseManager.getFirebaseSingleton().updateUser(emailNotificacion,
                        String.valueOf(puntosUsuario),
                        String.valueOf(aceptadasUsuario),
                        String.valueOf(noAceptadasUsuario));

                finish();
                break;
            case R.id.noButton:
                respuesta = false;
                // Crear la notificacion del usuario.
                nuevaNotificacion = new Notificacion(emailNotificacion, titulo, respuesta, descripcion);
                FirebaseManager.getFirebaseSingleton().addNotificationUser(nuevaNotificacion);

                // Actualizamos al usuario.
                noAceptadasUsuario ++;

                FirebaseManager.getFirebaseSingleton().updateUser(emailNotificacion,
                        String.valueOf(puntosUsuario),
                        String.valueOf(aceptadasUsuario),
                        String.valueOf(noAceptadasUsuario));
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Al salir del Activity sin dar respuesta se considera que no se acepta el evento.
        respuesta = false;
        // Crear la notificacion del usuario.
        nuevaNotificacion = new Notificacion(emailNotificacion, titulo, respuesta, descripcion);
        FirebaseManager.getFirebaseSingleton().addNotificationUser(nuevaNotificacion);

        noAceptadasUsuario ++;

        FirebaseManager.getFirebaseSingleton().updateUser(emailNotificacion,
                String.valueOf(puntosUsuario),
                String.valueOf(aceptadasUsuario),
                String.valueOf(noAceptadasUsuario));
    }


    private void obtenerDatosUsuario() {
        DatabaseReference ref = FirebaseManager.getFirebaseSingleton().getmDatabase().getReference("Usuarios");
        // Listener.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, data.getValue().toString());

                    HashMap auxUser = (HashMap) data.getValue();

                    if (instaciaUser.getEmail().equals(auxUser.get("email"))) {
                        // Actualizamos el valor para comprobar logros.
                        puntosUsuario = Integer.parseInt((String)auxUser.get("puntos"));
                        aceptadasUsuario = Integer.parseInt((String)auxUser.get("notificaciones"));
                        noAceptadasUsuario = Integer.parseInt((String)auxUser.get("notificacionesNoAceptadas"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void darLogro() {
        //Comprobacion de logros y dar puntos

        aceptadasUsuario++;

        // Por aceptar la notificación
        puntosUsuario = puntosUsuario +2;
        Toast.makeText(ConfirmarActivity.this, "Ha obtenido 2 puntos por cumplir el logro Positivo", Toast.LENGTH_LONG).show();

        if (aceptadasUsuario % 10 == 0) {
            // Recibe 10 puntos por haber visitado 10 eventos.
            puntosUsuario = puntosUsuario +10;
            Toast.makeText(ConfirmarActivity.this, "Ha obtenido 10 puntos por cumplir el logro Comilón", Toast.LENGTH_LONG).show();
        }

        if (aceptadasUsuario % 50 == 0) {
            // Recibe 10 puntos por haber visitado 10 eventos.
            puntosUsuario = puntosUsuario +10;
            Toast.makeText(ConfirmarActivity.this, "Ha obtenido 50 puntos por cumplir el logro Explorador", Toast.LENGTH_LONG).show();
        }

        if (tipo.equals("Reto Comida")) {
            puntosUsuario = puntosUsuario +5;
            Toast.makeText(ConfirmarActivity.this, "Ha obtenido 5 puntos por cumplir el logro Challenger", Toast.LENGTH_LONG).show();
        }

        if (tipo.equals("Degustación gastronómica")) {
            puntosUsuario = puntosUsuario +5;
            Toast.makeText(ConfirmarActivity.this, "Ha obtenido 5 puntos por cumplir el logro Catador", Toast.LENGTH_LONG).show();
        }

        if (tipo.equals("Inauguración restaurante")) {
            puntosUsuario = puntosUsuario +5;
            Toast.makeText(ConfirmarActivity.this, "Ha obtenido 5 puntos por cumplir el logro Celebración", Toast.LENGTH_LONG).show();
        }
    }
}
