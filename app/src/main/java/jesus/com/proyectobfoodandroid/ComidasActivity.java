package jesus.com.proyectobfoodandroid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.Adapters.ComidasAdapter;
import jesus.com.proyectobfoodandroid.Adapters.LogroAdapter;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Comida;

import static android.content.ContentValues.TAG;

public class ComidasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Comida> listaComidas;
    private ComidasAdapter adaptador;
    private DatabaseReference dbReference;
    private FirebaseDatabase firebase;
    private String evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtenemos las referencias a la base de datos.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Eventos");

        // Obtenemos el evento al que pertenece dicho plato.
        evento = getIntent().getStringExtra("evento");

        listaComidas = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.listComidas);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearManager);

        // Obtenemos las comidas del evento.
        obtenerComidasEvento();

        adaptador = new ComidasAdapter(listaComidas, evento);
        recyclerView.setAdapter(adaptador);

    }

    private void obtenerComidasEvento() {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(TAG, dataSnapshot.getValue().toString());

                    HashMap aux = (HashMap) dataSnapshot.getValue();

                    String nombreEvento = (String) aux.get("nombre");

                    if (nombreEvento.equals(evento)) {
                        // Cada evento tiene un HashMap de comidas.
                        ArrayList comidas = (ArrayList) aux.get("comidas");

                        for (Object comida: comidas) {

                            HashMap auxComida = (HashMap) comida;

                            // Creamos el objeto comida y lo metemos a la lista.
                            String nombre = (String) auxComida.get("nombre");
                            String descripcion = (String) auxComida.get("descripcion");
                            String ingredientes = (String) auxComida.get("ingredientes");
                            String tipo = (String) auxComida.get("tipo");
                            String imagen = (String) auxComida.get("imagen");

                            Comida nueva = new Comida(nombre, descripcion, ingredientes, tipo, imagen);

                            listaComidas.add(nueva);

                            adaptador.notifyDataSetChanged();
                        }
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
