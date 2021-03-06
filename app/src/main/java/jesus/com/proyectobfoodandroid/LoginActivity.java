package jesus.com.proyectobfoodandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    // Declaramos las variables para los elementos del activity.
    private EditText inputEmail;
    private EditText inputPassword;
    private Button signInButton;
    private Button signUpButton;

    // CONTEXTO control.
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // String emailLogUser.
    private String emailLogUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos la shared preferences.
        sharedPreferences = this.getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Inicializamos los elementos de la ventana.
        inputEmail = (EditText) findViewById(R.id.textEmail);
        inputPassword = (EditText) findViewById(R.id.textPassword);
        signInButton = (Button) findViewById(R.id.signInButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Si el usuario se ha logueado con anterioridad pasamos directamente al actiovity principal.
        if (FirebaseManager.getFirebaseSingleton().comprobarUsuario() == true) {
            Intent instance = new Intent(LoginActivity.this, PrincipalActivity.class);
            instance.putExtra("email", FirebaseManager.getFirebaseSingleton().getLogUser());
            startActivity(instance);
            finish();
        }


        // Manejador del evento para el boton entrar.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                // Logueamos al usuario
                FirebaseAuth auth = FirebaseManager.getFirebaseSingleton().signIn(email, password, LoginActivity.this);

                if(auth != null) {
                    // Modificamos el email del usuario logeado en la persistencia.
                    FirebaseManager.getFirebaseSingleton().setUserLog(email);

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(LoginActivity.this, getString(R.string.authFailed), Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                        // Hacemos que el email del usuario logueado sea un shared preference.
                                        editor.putString("email", email);
                                        editor.commit();
                                        //intent.putExtra("email", email);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(LoginActivity.this, "No se cumplen las condiciones", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Manejador del evento para el boton registrarse.
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
