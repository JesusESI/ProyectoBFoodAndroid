package jesus.com.proyectobfoodandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;

import static android.content.ContentValues.TAG;

public class EventoInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTituloEvento, textDescripcionEvento, textPlatosEvento;
    private Button detallesButton;
    private FirebaseDatabase mDatabase;
    private Toolbar toolbar;
    // Nombre del evento mostrado.
    private String evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info);

        // Cargamos la toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Información evento");

        // Guardamos el nombre del evento.
        evento = getIntent().getStringExtra("titulo");

        // Inicializar los campos.
        textTituloEvento = findViewById(R.id.textTituloEvento);
        textDescripcionEvento = findViewById(R.id.textDescripcionEvento);
        textPlatosEvento = findViewById(R.id.textPlatosEvento);
        detallesButton = findViewById(R.id.detallesButton);

        detallesButton.setOnClickListener(this);

        // Obtenemos la instancia de la base de datos
        mDatabase = FirebaseManager.getFirebaseSingleton().getmDatabase();

        //Obtener evento
        readEventoNotificacion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detallesButton:
                Intent listadoComidasIntent = new Intent(EventoInfoActivity.this, ComidasActivity.class);
                listadoComidasIntent.putExtra("evento", evento);
                startActivity(listadoComidasIntent);
                break;
        }
    }

    public void readEventoNotificacion() {
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

                    if (nombreEvento.equals(getIntent().getStringExtra("titulo"))) {
                        String descripcion = (String) aux.get("descripcion");

                        textTituloEvento.setText((String) aux.get("nombre"));
                        textDescripcionEvento.setText(descripcion);

                        // Cada evento tiene un HashMap de comidas.
                        ArrayList comidas = (ArrayList) aux.get("comidas");
                        // Vaciamos el campo
                        textPlatosEvento.setText("");

                        for (Object comida: comidas) {
                            HashMap auxComida = (HashMap) comida;
                            textPlatosEvento.append("* "+ auxComida.get("descripcion") + "\n");
                        }
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
}
