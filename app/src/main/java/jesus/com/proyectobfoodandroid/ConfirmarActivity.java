package jesus.com.proyectobfoodandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Notificacion;

public class ConfirmarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTitulo, textDescripcion;
    private Button siButton, noButton;

    private boolean respuesta;
    private String titulo;
    private String descripcion;
    private String emailNotificacion;

    private Context instantanea;
    private SharedPreferences sharedVariables;
    private SharedPreferences.Editor editor;

    private Notificacion nuevaNotificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);

        // Obtenemos los datos del intent.
        sharedVariables = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
        titulo = sharedVariables.getString("titulo", "titulo");
        descripcion = sharedVariables.getString("descripcion", "descripcion");
        emailNotificacion = sharedVariables.getString("emailNotificacion", "emailNotificacion");

        // Pasamos el nombre de la notificación para posteriormente cargar el evento al que pertenece.
        editor = sharedVariables.edit();
        editor.putString("nombreEvento", titulo);
        editor.commit();

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
                finish();
                break;
            case R.id.noButton:
                respuesta = false;
                // Crear la notificacion del usuario.
                nuevaNotificacion = new Notificacion(emailNotificacion, titulo, respuesta, descripcion);
                FirebaseManager.getFirebaseSingleton().addNotificationUser(nuevaNotificacion);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Al salir del Activity sin dar respuesta se considera que no se acepta el evento.
        respuesta = false;
        // Crear la notificacion del usuario.
        nuevaNotificacion = new Notificacion(emailNotificacion, titulo, respuesta, descripcion);
        FirebaseManager.getFirebaseSingleton().addNotificationUser(nuevaNotificacion);
    }
}
