package pceti.a8c1.cetitoolapp.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pceti.a8c1.cetitoolapp.R;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button Iniciar;
    private EditText Email;
    private EditText Password;
    ConstraintLayout animation;
    AnimationDrawable animationDrawable;

    private ProgressBar progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        progreso= findViewById(R.id.progressBar);
        Email = findViewById(R.id.et_correo);
        Password = findViewById(R.id.et_contra);
        Iniciar = findViewById(R.id.btn_iniciar_sesion);

        animation = findViewById(R.id.animation);
        animationDrawable = (AnimationDrawable) animation.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //Compara la instancia de la Autentificación de Firabase (Para comprobar si ya hay un usuario con sesión iniciada o no)

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    MainScreen();
                }
            }
        };

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void MainScreen() {
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void RegisterScreen(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);

    }


    private void Login() {
        progreso.setVisibility(View.VISIBLE);
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    MainScreen();
                                    Toast.makeText(MainActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                                    progreso.setVisibility(View.GONE);
                                }else{
                                    Toast.makeText(MainActivity.this, "No se ha verificado el correo", Toast.LENGTH_SHORT).show();
                                    progreso.setVisibility(View.GONE);
                                }

                            } else{
                                Toast.makeText(MainActivity.this, "Error, contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show();
                            }
                                progreso.setVisibility(View.GONE);
                        }
            });
        }else {
            progreso.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Algunos campos están vacíos", Toast.LENGTH_SHORT).show();
        }
    }
}
