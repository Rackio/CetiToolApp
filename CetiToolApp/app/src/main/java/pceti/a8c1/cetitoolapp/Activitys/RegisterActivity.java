package pceti.a8c1.cetitoolapp.Activitys;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pceti.a8c1.cetitoolapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailFiedl;
    private EditText mPasswordField;
   private EditText mPasswordField2;
    private Button mRegisterButton;
  private Button btnregreso;
   private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private TextView mTextLogin;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
       mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mNameField = findViewById(R.id.etname);
        mEmailFiedl = findViewById(R.id.etCorreoNuevo);
        mPasswordField = findViewById(R.id.etContraNueva);
        mPasswordField2 = findViewById(R.id.etRepetirNueva);
        mRegisterButton = findViewById(R.id.btnAceptarNuevo);
        mProgress = new ProgressDialog(this);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startRegister();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }
   public void Regresar(View view){
       Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
   }
    private void startRegister() {
        final String name = mNameField.getText().toString().trim();
        final String email = mEmailFiedl.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();
        final String password2 = mPasswordField2.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2) ) {
            if (password.equals(password2)){
            mProgress.setMessage("Registrando, Espere por favor...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()) {
                                //mAuth.signInWithEmailAndPassword(email, password);
                                Toast.makeText(getApplicationContext(), "Te has registrado correctamente", Toast.LENGTH_SHORT).show();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("name").setValue(name);
                                currentUserDB.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/cetitoolapp.appspot.com/o/Ceti.png?alt=media&token=9b51fa1d-b501-4f88-b237-a68a1cf1dd9b");

                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Hemos enviado un correo de verificación", Toast.LENGTH_SHORT).show();
                                finish();

                            } else
                                Toast.makeText(RegisterActivity.this, "error registrando usuario", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else Toast.makeText(RegisterActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(RegisterActivity.this, "Rellena los datos", Toast.LENGTH_SHORT).show();
    }

}