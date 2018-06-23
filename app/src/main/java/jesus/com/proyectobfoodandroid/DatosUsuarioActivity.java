package jesus.com.proyectobfoodandroid;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.User;

import static android.content.ContentValues.TAG;

public class DatosUsuarioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseDatabase firebase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference dbReference;

    private User emailDatosUsuario;
    private Uri imagenUserUri;

    private ImageView imagenUsuario;
    private TextView apodoTexto, posiciontexto, puntosTexto, logrosTexto, notificacionesTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtenermos el email del usuario logueado.
        emailDatosUsuario = new User(getIntent().getStringExtra("email"));

        // Creamos la query para obtener el usuario.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Usuarios");
        firebaseStorage = FirebaseManager.getFirebaseSingleton().getmStorage();

        apodoTexto = findViewById(R.id.textApodo);
        posiciontexto = findViewById(R.id.textPosicion);
        puntosTexto = findViewById(R.id.textPuntos);
        logrosTexto = findViewById(R.id.textLogros);
        notificacionesTexto = findViewById(R.id.textNotificaciones);
        imagenUsuario = findViewById(R.id.imageUser);


        obtainUser();
    }

    public void obtainUser() {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, data.getValue().toString());

                    HashMap usuario = (HashMap) data.getValue();

                    if (emailDatosUsuario.getEmail().equals(usuario.get("email"))) {

                        String apodo = (String) usuario.get("apodo");
                        String puntos = (String) usuario.get("puntos");
                        String imagen = (String) usuario.get("imagen");

                        // Obtener URI de la imagen del usuario.
                        obtainUri(imagen);

                        apodoTexto.setText(apodo);
                        puntosTexto.setText(puntos);

                    }
                }
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
                        imagenUserUri = uri;
                        // Libreria externa que carga los datos direcatmente desde la url.
                        Glide.with(DatosUsuarioActivity.this).load(imagenUserUri).into(imagenUsuario);

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
