package jesus.com.proyectobfoodandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageButton profileButton;
    private ImageButton notificatioButton;
    private ImageButton rankingButton ;
    private ImageButton achievementsButton;
    private ImageButton mapButton;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.options:
                Toast.makeText(this, "Has pulsado el botón desplegar las opciones", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Ha elegido ver su perfil", Toast.LENGTH_LONG).show();
                break;
            case R.id.notificationButton:
                Toast.makeText(this, "Ha elegido ver las notificaciones", Toast.LENGTH_LONG).show();
                break;
            case R.id.rankingButton:
                Toast.makeText(this, "Ha elegido ver el ranking", Toast.LENGTH_LONG).show();
                break;
            case R.id.achievementsButton:
                Toast.makeText(this, "Ha elegido ver sus logros", Toast.LENGTH_LONG).show();
                break;
            case R.id.mapButton:
                Toast.makeText(this, "Ha elegido ver la localizacion de los eventos", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
