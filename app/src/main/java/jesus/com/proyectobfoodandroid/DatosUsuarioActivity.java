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
    private TextView textApodo;
    private TextView textAchievements;
    private TextView textPosition;
    private TextView textPoints;
    private TextView textNotifications;

    private User userLogDatosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializamos los campos.
        textApodo = (TextView) findViewById(R.id.textApodoData);
        textAchievements = (TextView) findViewById(R.id.textAchievementsData);
        textPosition = (TextView) findViewById(R.id.textPositionData);
        textPoints = (TextView) findViewById(R.id.textPointsData);
        textNotifications = (TextView) findViewById(R.id.textNotificationsData);

        //Obtenermos el email del usuario logueado.
        ;
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
        this.textApodo.setText("Hola");


        setContentView(R.layout.activity_datos_usuario);

    }
}
