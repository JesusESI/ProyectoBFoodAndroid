package jesus.com.proyectobfoodandroid;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Comida;

import static android.content.ContentValues.TAG;

public class DatosComidaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseDatabase firebase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference dbReference;

    private Comida comida;
    private Uri imagenComidaUri;

    private ImageView imagenComida;
    private TextView textNombreComida, textDescripcionComida, textTipoComida, textIngredientesComida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_comida);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Plato");

        // Obtenemos el nombre de la comida seleccionada.
        comida = new Comida(getIntent().getExtras().getString("comida"));

        // Creamos la query para obtener el usuario.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Eventos");
        firebaseStorage = FirebaseManager.getFirebaseSingleton().getmStorage();

        // Inicializamos los elementos gráficos.
        imagenComida = findViewById(R.id.imageComida);
        textNombreComida = findViewById(R.id.textNombreComidaDatos);
        textDescripcionComida = findViewById(R.id.textDescripcionComidaDatos);
        textTipoComida = findViewById(R.id.textTipoComidaDatos);
        textIngredientesComida = findViewById(R.id.textIngredientesComidaDatos);

        obtainComida();

    }

    private void obtainComida() {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, dataSnapshot.getValue().toString());

                HashMap aux = (HashMap) dataSnapshot.getValue();

                String nombreEvento = (String) aux.get("nombre");

                if (nombreEvento.equals(getIntent().getStringExtra("evento"))) {
                    // Cada evento tiene un HashMap de comidas.
                    ArrayList comidas = (ArrayList) aux.get("comidas");

                    for (Object comida: comidas) {

                        HashMap auxComida = (HashMap) comida;

                        if (auxComida.get("nombre").equals(getIntent().getStringExtra("comida"))) {
                            // Creamos el objeto comida y lo metemos a la lista.
                            String nombre = (String) auxComida.get("nombre");
                            String descripcion = (String) auxComida.get("descripcion");
                            String ingredientes = (String) auxComida.get("ingredientes");
                            String tipo = (String) auxComida.get("tipo");
                            String imagen = (String) auxComida.get("imagen");

                            textNombreComida.setText(nombre);
                            textDescripcionComida.setText(descripcion);
                            textIngredientesComida.setText(ingredientes);
                            textTipoComida.setText("Tipo: " + tipo);

                            // Obtenemois imagen
                            obtainUri(imagen);

                            Comida nueva = new Comida(nombre, descripcion, ingredientes, tipo, imagen);
                        }
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

    private void obtainUri(String nombreImagen) {

        firebaseStorage.getReference("Imagenes").child(nombreImagen).getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Obtenemos la Uri y actializamos la imagen.
                        imagenComidaUri = uri;
                        // Libreria externa que carga los datos direcatmente desde la url.
                        Glide.with(DatosComidaActivity.this).load(imagenComidaUri).into(imagenComida);

                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Tratar el error.
            }
        });
    }
}
