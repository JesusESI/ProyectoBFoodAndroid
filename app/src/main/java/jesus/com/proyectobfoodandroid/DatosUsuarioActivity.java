package jesus.com.proyectobfoodandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jesus.com.proyectobfoodandroid.Objects.User;

public class DatosUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText textApodo;
    private EditText textAchievements;
    private EditText textPosition;
    private EditText textPoints;
    private EditText textNotifications;

    private User userLogDatosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializamos los campos.
        textApodo = (EditText) findViewById(R.id.textApodo);
        textAchievements = (EditText) findViewById(R.id.textAchievements);
        textPosition = (EditText) findViewById(R.id.textPosition);
        textPoints = (EditText) findViewById(R.id.textPoints);
        textNotifications = (EditText) findViewById(R.id.textNotifications);

        //Obtenermos el email del usuario logueado.
        userLogDatosUsuario = new User(getIntent().getStringExtra("userLog"));


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(userLogDatosUsuario != null) {
            // Cargamos los datos del usuario.
            loadUserData(userLogDatosUsuario);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                Toast.makeText(this, "Has pulsado el botón desplegar las opciones", Toast.LENGTH_LONG).show();
                break;
            case R.id.info:
                Toast.makeText(this, "Has pulsado el botón desplegar la información de la app.", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserData(User userLogEmail) {
         Toast.makeText(DatosUsuarioActivity.this , userLogDatosUsuario.getEmail(), Toast.LENGTH_LONG).show();
        // Mostrar los datos en las distintas etiquetas.
        textApodo.setText(userLogDatosUsuario.getApodo());


        //setContentView(R.layout.activity_datos_usuario);

    }
}
