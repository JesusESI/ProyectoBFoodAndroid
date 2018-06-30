package jesus.com.proyectobfoodandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;

public class RegisterActivity extends AppCompatActivity {

    // Elementos gráficos;
    private Button signUpButton;
    private EditText textNombre;
    private EditText textApellidos;
    private EditText textEmail;
    private EditText textUsuario;
    private EditText textPassword;
    private EditText textRepeatPassword;

    // Variables.
    private String nombre;
    private String apellidos;
    private String email;
    private String usuario;
    private String password;
    private String repeatPassword;
    private String notificaciones = "0";
    private String notificacionesNoAceptadas = "0";
    private String puntos = "0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicialización.
        signUpButton = (Button) findViewById(R.id.signUpButton);
        textNombre = (EditText) findViewById(R.id.textNombre);
        textApellidos = (EditText) findViewById(R.id.textApellidos);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textUsuario = (EditText)  findViewById(R.id.textUsuario);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textRepeatPassword = (EditText) findViewById(R.id.textRepeatPassword);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegisterActivity.this, "Se cumplen las condiciones", Toast.LENGTH_SHORT).show();

                nombre = textNombre.getText().toString();
                apellidos = textApellidos.getText().toString();
                email = textEmail.getText().toString();
                usuario = textUsuario.getText().toString();
                password = textPassword.getText().toString();
                repeatPassword = textRepeatPassword.getText().toString();

                //create user
                FirebaseAuth auth = FirebaseManager.getFirebaseSingleton().signUp(nombre, apellidos, email, password, repeatPassword, RegisterActivity.this);

                if (auth != null) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Authentication successs.", Toast.LENGTH_SHORT).show();
                                        // reguistramos el usuario como dato en firebaseDatabase.
                                        FirebaseManager.getFirebaseSingleton().saveUser(nombre, apellidos, email, usuario, "IconoUsuario.png",
                                                notificaciones, notificacionesNoAceptadas, puntos);
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });

                }
            }
        });
    }
}
