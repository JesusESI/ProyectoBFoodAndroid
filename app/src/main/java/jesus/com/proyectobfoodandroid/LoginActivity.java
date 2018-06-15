package jesus.com.proyectobfoodandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos los elementos de la ventana.
        inputEmail = (EditText) findViewById(R.id.textEmail);
        inputPassword = (EditText) findViewById(R.id.textPassword);
        signInButton = (Button) findViewById(R.id.signInButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Si el usuario se ha logueado con anterioridad pasamos directamente al actiovity principal.
        if (FirebaseManager.getFirebaseSingleton().comprobarUsuario() == true) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        // Manejador del evento para el boton entrar.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                // Logueamos al usuario
                FirebaseAuth auth = FirebaseManager.getFirebaseSingleton().signIn(email, password, LoginActivity.this);

                if(auth != null) {
                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimumPassword));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.authFailed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
