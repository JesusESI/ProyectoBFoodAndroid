package jesus.com.proyectobfoodandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import jesus.com.proyectobfoodandroid.BLE.BLEService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        // Creamos el objeto Usuario del usuario logeado, si es la primera vez que se conecta.
        userLog = new User(getIntent().getStringExtra("email"));
        emailUserLog = userLog.getEmail();


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
                break;
            case R.id.info:
                Toast.makeText(this, "Has pulsado el botón desplegar la información de la app.", Toast.LENGTH_LONG).show();
                break;
            case R.id.beacon:
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
                intentUserProfile.putExtra("userLog", emailUserLog);
                startActivity(intentUserProfile);
                break;
            case R.id.notificationButton:
                Toast.makeText(this, "Ha elegido ver las notificaciones", Toast.LENGTH_LONG).show();
                break;
            case R.id.rankingButton:
                Toast.makeText(this, "Ha elegido ver el ranking", Toast.LENGTH_LONG).show();
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
