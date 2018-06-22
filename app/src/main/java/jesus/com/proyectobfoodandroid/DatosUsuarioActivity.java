package jesus.com.proyectobfoodandroid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.Adapters.DatosUsuarioAdapter;
import jesus.com.proyectobfoodandroid.Adapters.RankingAdapter;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.User;

public class DatosUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatosUsuarioAdapter itemUserAdapter;
    private FirebaseDatabase firebase;
    private DatabaseReference dbReference;
    private List<User> usuarioSelected;

    private User userLogDatosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtenermos el email del usuario logueado.
        userLogDatosUsuario = new User(getIntent().getStringExtra("email"));

        usuarioSelected = new ArrayList<>();

        // Creamos la query para obtener el usuario.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Usuarios");

        recyclerView = (RecyclerView) findViewById(R.id.dataUserLayout);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearManager);

        obtainUser();

        // Creamos adaptador
        itemUserAdapter = new DatosUsuarioAdapter(usuarioSelected);
        recyclerView.setAdapter(itemUserAdapter);
    }

    public void obtainUser() {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap usuario = (HashMap) dataSnapshot.getValue();

                String email = (String) usuario.get("email");

                if (email.equals(userLogDatosUsuario.getEmail())) {
                    // TODO. Introducir todos los parametros.
                    usuarioSelected.add(new User());
                    itemUserAdapter.notifyDataSetChanged();
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
