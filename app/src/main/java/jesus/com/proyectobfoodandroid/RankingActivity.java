package jesus.com.proyectobfoodandroid;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.Adapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.Adapters.RankingAdapter;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.OrderByPuntos;
import jesus.com.proyectobfoodandroid.Objects.User;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> listaUsuariosRanking;
    private RankingAdapter rankingAdapter;
    //private DatabaseReference dbReference;
    private FirebaseDatabase firebase;
    private Query usersOrdered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Obtenemos las referencias a la base de datos.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        usersOrdered = firebase.getReference("Usuarios").orderByChild("puntos");
        // dbReference = firebase.getReference("Usuarios");


        listaUsuariosRanking = new ArrayList();
        recyclerView = (RecyclerView) findViewById(R.id.rankingList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        linearManager.setReverseLayout(true);
        linearManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearManager);

        obtainQuery();
        //obtainList();

        rankingAdapter = new RankingAdapter(listaUsuariosRanking);
        recyclerView.setAdapter(rankingAdapter);
    }

    private void obtainQuery() {
        usersOrdered.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap usuario = (HashMap) dataSnapshot.getValue();

                // Guardamos los datos del usuario obtenido en variables.
                String apodo = (String) usuario.get("apodo");
                String puntos = (String) usuario.get("puntos");
                String email = (String) usuario.get("email");

                // Meter todos los datos del usuario cambiar constructor.
                listaUsuariosRanking.add(new User( apodo, puntos, email));

                rankingAdapter.notifyDataSetChanged();
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