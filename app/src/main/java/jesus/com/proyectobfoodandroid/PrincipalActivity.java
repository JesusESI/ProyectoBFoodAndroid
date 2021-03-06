package jesus.com.proyectobfoodandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.User;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageButton profileButton;
    private ImageButton notificatioButton;
    private ImageButton rankingButton ;
    private ImageButton achievementsButton;
    private ImageButton mapButton;

    // Variable de sesion.
    private User userLog;
    private String emailUserLog;

    // Shared contexto.
    SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializamos las preferencias para obtener el email del login del usuario logueado.
        preferencias = this.getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        emailUserLog = preferencias.getString("email", "email");

        // Inicializamos los botones.
        profileButton = (ImageButton) findViewById(R.id.profileButton);
        notificatioButton = (ImageButton) findViewById(R.id.notificationButton);
        rankingButton = (ImageButton) findViewById(R.id.rankingButton);
        achievementsButton = (ImageButton) findViewById(R.id.achievementsButton);
        mapButton = (ImageButton) findViewById(R.id.mapButton);

        // Manejador de eventos para pulsarlo.
        profileButton.setOnClickListener(this);
        notificatioButton.setOnClickListener(this);
        rankingButton.setOnClickListener(this);
        achievementsButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        userLog = new User(emailUserLog);
        emailUserLog = userLog.getEmail();

        // Prueba
        //FirebaseManager.getFirebaseSingleton().updateUser(emailUserLog, "10");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                //Toast.makeText(this, "Has pulsado el botón desplegar las opciones", Toast.LENGTH_LONG).show();
                Intent loginInstance = new Intent(PrincipalActivity.this, LoginActivity.class);
                FirebaseManager.getFirebaseSingleton().logOut();
                startActivity(loginInstance);
                finish();
                break;
            case R.id.info:
                Toast.makeText(this, "Has pulsado el botón desplegar la información de la app.", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileButton:
                //Toast.makeText(this, "Ha elegido ver su perfil", Toast.LENGTH_LONG).show();
                Intent intentUserProfile = new Intent(PrincipalActivity.this, DatosUsuarioActivity.class);
                intentUserProfile.putExtra("email", emailUserLog);
                startActivity(intentUserProfile);
                break;
            case R.id.notificationButton:
                //Toast.makeText(this, "Ha elegido ver las notificaciones", Toast.LENGTH_LONG).show();
                Intent intentNotificaciones = new Intent(PrincipalActivity.this, NotificacionesActivity.class);
                intentNotificaciones.putExtra("email", emailUserLog);
                startActivity(intentNotificaciones);
                break;
            case R.id.rankingButton:
                //Toast.makeText(this, "Ha elegido ver el ranking", Toast.LENGTH_LONG).show();
                Intent intentRanking = new Intent(PrincipalActivity.this, RankingActivity.class);
                startActivity(intentRanking);
                break;
            case R.id.achievementsButton:
                //Toast.makeText(this, "Ha elegido ver sus logros", Toast.LENGTH_LONG).show();
                Intent intentAchievements = new Intent(PrincipalActivity.this, LogrosActivity.class);
                startActivity(intentAchievements);
                break;
            case R.id.mapButton:
                //Toast.makeText(this, "Ha elegido ver la localizacion de los eventos", Toast.LENGTH_LONG).show();
                Intent intentMap = new Intent(PrincipalActivity.this, MapsActivity.class);
                startActivity(intentMap);
                break;
        }
    }
}
