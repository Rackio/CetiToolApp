package pceti.a8c1.cetitoolapp.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import pceti.a8c1.cetitoolapp.R;

public class AddNoticia extends AppCompatActivity {

    private ImageButton IbSeleccionar;
    private EditText etTitulo, etDescrip;
    private Button btnAceptar;
    public Spinner spinner;
    private ProgressDialog progressDialog;

    private Uri ImagenUri = null;

    public String spinnerOpc;

    private static final int GALLERY_REQUEST=1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE = 100;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_noticia);

        //Para evitar un error al momento de subir el archivo y pedir permisos para poder hacerlo
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_STORAGE);

        etTitulo = findViewById(R.id.etTitulo_Add_Noticias);
        etDescrip = findViewById(R.id.etDescrip_Add_Noticias);
        btnAceptar = findViewById(R.id.btn_Aceptar_Add_Noticias);
        progressDialog = new ProgressDialog(this);
        IbSeleccionar = findViewById(R.id.ib_Image_Add_Noticias);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Noticias");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users");


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


        //Opciones en el spinner (En tipo lista
        spinner = findViewById(R.id.spinner_Add_Noticias);
        String[] letra = {"Comunidad","Deportes","Institucional", "Comedia"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));



        IbSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
                galeria.setType("image/*");
                startActivityForResult(galeria, GALLERY_REQUEST);
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });
    }

    private void publicar() {

        progressDialog.setMessage("Subiendo...");
        progressDialog.show();

        spinnerOpc=(String)spinner.getSelectedItem();
        final String titulo_val=etTitulo.getText().toString().trim();
        final String desc_val=etDescrip.getText().toString().trim();
        final String spinner_Val=spinnerOpc;

        if(!TextUtils.isEmpty(titulo_val) && !TextUtils.isEmpty(desc_val) && ImagenUri != null){


            StorageReference rutaArchivo = mStorage.child("Imagenes_noticias").child(ImagenUri.getLastPathSegment());
            rutaArchivo.putFile(ImagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newNoticia= mDatabase.push();


                    if(firebaseAuth.getCurrentUser()!=null){
                        mDatabaseUser.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                newNoticia.child("Titulo").setValue(titulo_val);
                                newNoticia.child("Desc").setValue(desc_val);
                                newNoticia.child("Filtro").setValue(spinner_Val);
                                newNoticia.child("Imagen").setValue(downloadUri.toString());
                                newNoticia.child("uid").setValue(currentUser.getUid());
                                newNoticia.child("userFacebook").setValue(String.valueOf(dataSnapshot.child("name").getValue()));


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(),"Publicado con éxito",Toast.LENGTH_SHORT).show();

                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Algunos campos se encuentran vacíos", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


    //Para poder seleccionar la imagen de galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            ImagenUri = data.getData();

            IbSeleccionar.setImageURI(ImagenUri);
        }
    }
}
