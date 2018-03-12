package pceti.a8c1.cetitoolapp.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import pceti.a8c1.cetitoolapp.R;

public class UpdateUserActivity extends AppCompatActivity {
    private ImageButton mSetupImageBtn;
    private  EditText txtRegistro;
    private  EditText txtContraseña;
    private  EditText txtContraR;
    private EditText txtNombre;
    private Spinner spGrado;
    private Spinner spGrupo;

    private  Uri mimageUri = null;

    private DatabaseReference mDatabaseusers;
    private StorageReference mStorageImage;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    FirebaseAuth mAuth;
    private Button btnset;
    private  static  final int GALLERY_REQUEST=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseusers = FirebaseDatabase.getInstance().getReference().child("users");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        mProgress = new ProgressDialog(this);
        txtNombre = findViewById(R.id.txtName);
        txtRegistro = findViewById(R.id.txtRegistro);
        txtContraseña = findViewById(R.id.txtNC);
        txtContraR = findViewById(R.id.txtNCR);
        mSetupImageBtn = findViewById(R.id.imagebtn_update);
        btnset = findViewById(R.id.Btnenviar_update);

        spGrupo = findViewById(R.id.spGrupo);
        spGrado = findViewById(R.id.spGrado);

        String[] letra = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
        String[] numero = {"1","2","3","4","5","6","7","8"};
        spGrado.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numero));
        spGrupo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarUsuario();
            }
        });

        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        if(mAuth.getCurrentUser()!=null){
            mDatabaseusers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtNombre.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                    txtRegistro.setText(String.valueOf(dataSnapshot.child("Registro").getValue()));

                    String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                    if (URLUtil.isValidUrl(imageUrl))
                        Picasso.with(UpdateUserActivity.this).load(Uri.parse(imageUrl)).into(mSetupImageBtn);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri= result.getUri();
                mSetupImageBtn.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void ActualizarUsuario(){
        final   String nombre = txtNombre.getText().toString().trim();
        final   String Registro = txtRegistro.getText().toString().trim();
        final String Contraseña = txtContraseña.getText().toString().trim();
        final   String ContraseñaR = txtContraR.getText().toString().trim();
        final String Userid =mAuth.getCurrentUser().getUid();
        final String Grado =spGrado.getSelectedItem().toString();
        final String grupo = spGrupo.getSelectedItem().toString();

        if(!TextUtils.isEmpty(nombre)&&!TextUtils.isEmpty(Registro)&&!TextUtils.isEmpty(Contraseña)&&!TextUtils.isEmpty(ContraseñaR)) {
            if (Contraseña.equals(ContraseñaR)) {
                mProgress.setMessage("Modificando Perfil...");
                mProgress.show();
                final String GradoyGrupo = Grado + "" + grupo;
                StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUri = taskSnapshot.getDownloadUrl().toString();
                        mDatabaseusers.child(Userid).child("name").setValue(nombre);
                        mDatabaseusers.child(Userid).child("Registro").setValue(Registro);
                        mDatabaseusers.child(Userid).child("Grupo").setValue(GradoyGrupo);
                        mDatabaseusers.child(Userid).child("image").setValue(downloadUri);
                        Intent intent = new Intent(UpdateUserActivity.this, StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "Contraseñas diferentes", Toast.LENGTH_SHORT).show();
            }
        }
        else { Toast.makeText(getApplicationContext(), "Campos vacios", Toast.LENGTH_SHORT).show();}
    }

}
