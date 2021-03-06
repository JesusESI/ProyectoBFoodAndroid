package jesus.com.proyectobfoodandroid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.Adapters.LogroAdapter;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Logro;

public class LogrosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Logro> resultado;
    private LogroAdapter adaptador;
    private DatabaseReference dbReference;
    private FirebaseDatabase firebase;

    ArrayList logros = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Logros");

        // Obtenemos las referencias a la base de datos.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Logros");


        resultado = new ArrayList();
        recyclerView = (RecyclerView) findViewById(R.id.logrosList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearManager);

        // nObtenemos la lista de logros.
        obtainLogros();

        // Creamos el objeto adaptador y lo insertamos en el recycler view.
        adaptador = new LogroAdapter(resultado);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logros, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void obtainLogros() {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.

                HashMap logros = (HashMap) dataSnapshot.getValue();
                resultado.add(new Logro( logros.get("nombre").toString(), logros.get("descripcion").toString(), logros.get("puntos").toString()));

                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Logro logro = dataSnapshot.getValue(Logro.class);
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
