package pceti.a8c1.cetitoolapp.Activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import pceti.a8c1.cetitoolapp.AdapterMensajes;
import pceti.a8c1.cetitoolapp.Entidades.Mensaje;
import pceti.a8c1.cetitoolapp.Entidades.MensajeEnviar;
import pceti.a8c1.cetitoolapp.Entidades.MensajeRecibir;
import pceti.a8c1.cetitoolapp.Modelos.Chat;
import pceti.a8c1.cetitoolapp.Modelos.User;
import pceti.a8c1.cetitoolapp.Modelos.Users;
import pceti.a8c1.cetitoolapp.R;
import pceti.a8c1.cetitoolapp.constantes.variables;


public class ChatActivity extends AppCompatActivity {
private String receiver;
    private String receiverem;
    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar, cerrarSesion;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;
private String sender;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCadena;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabase;
    private String room_type_1;
    private  String room_type_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombre = findViewById(R.id.nombre);
        rvMensajes = findViewById(R.id.rvMensajes);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(variables.ARG_CHAT_ROOMS);
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        adapter = new AdapterMensajes(this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setStackFromEnd(false);
        rvMensajes.setLayoutManager(LayoutManager);
        rvMensajes.setAdapter(adapter);
        rvMensajes.setHasFixedSize(true);
       receiver= getIntent().getExtras().getString("receiveruid");
        sender= getIntent().getExtras().getString("senderui");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        room_type_1 = "CKCpdeYA5kNERETLIaA7Z0JUP4e2" + "_" + receiver;
        room_type_2 = receiver + "_" + "CKCpdeYA5kNERETLIaA7Z0JUP4e2";


        btnEnviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(txtMensaje.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "No haz escrito nada", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), room_type_1, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), room_type_2, Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(room_type_1)) {

                                    databaseReference.child(room_type_1).push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), fotoPerfilCadena,  NOMBRE_USUARIO, fotoPerfilCadena, "1",ServerValue.TIMESTAMP));
                                    txtMensaje.setText("");

                                } else if (dataSnapshot.hasChild(room_type_2)) {
                                    databaseReference.child(room_type_2).push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), fotoPerfilCadena,  NOMBRE_USUARIO, fotoPerfilCadena, "1",ServerValue.TIMESTAMP));
                                    txtMensaje.setText("");

                                } else {

                                    databaseReference.child(room_type_1).push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), fotoPerfilCadena,  NOMBRE_USUARIO, fotoPerfilCadena, "1",ServerValue.TIMESTAMP));
                                    txtMensaje.setText("");


                                }


                            }




                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND);
            }
        });

 databaseReference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    database
                            .getReference(variables.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String PEPE1= String.valueOf(dataSnapshot.child("image").getValue());
                           String PEPE2= String.valueOf(dataSnapshot.child("mensaje").getValue());
                            String PEPE3= String.valueOf(dataSnapshot.child("nombre").getValue());
                            String PEPE4= String.valueOf(dataSnapshot.child("type_mensaje").getValue());
                            String PEPE5= String.valueOf(dataSnapshot.child("urlFoto").getValue());
                            Toast.makeText(getApplicationContext(),PEPE1 , Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),PEPE2 , Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),PEPE3 , Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),PEPE4 , Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),PEPE5 , Toast.LENGTH_SHORT).show();
                             MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        verifyStoragePermissions(this);
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }
    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }


    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        databaseReference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
                    if (dataSnapshot.hasChild(room_type_1)) {

                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + txtMensaje, "", NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.child(room_type_1).push().setValue(m);

                    } else if (dataSnapshot.hasChild(room_type_2)) {

                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + txtMensaje, "", NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.child(room_type_2).push().setValue(m);

                    } else {

                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + txtMensaje, "", NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.child(variables.ARG_CHAT_ROOMS).child(room_type_1).push().setValue(m);


                    }


                } else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
   /* public void sendMensaje(){
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO+" te ha enviado una foto",u.toString(),NOMBRE_USUARIO,fotoPerfilCadena,"2",ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                }
            });
        }else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){
            Uri u = data.getData();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            DatabaseReference reference = database.getReference("users/"+currentUser.getUid());
            mStorage = FirebaseStorage.getInstance().getReference();
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri u = taskSnapshot.getDownloadUrl();
                    fotoPerfilCadena = u.toString();
                    MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO+" ha actualizado su foto de perfil",u.toString(),NOMBRE_USUARIO,fotoPerfilCadena,"2",ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);

                    Picasso.with(ChatActivity.this).load(u.toString()).into(fotoPerfil);
                }
            });
        }
    }
    */
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            btnEnviar.setEnabled(false);
            DatabaseReference reference = database.getReference("users/"+currentUser.getUid());
            mStorage = FirebaseStorage.getInstance().getReference();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users usuario = dataSnapshot.getValue(Users.class);
                    NOMBRE_USUARIO = usuario.getName();
                    nombre.setText(NOMBRE_USUARIO);
                    String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                    fotoPerfilCadena = imageUrl;
                    if (URLUtil.isValidUrl(imageUrl))
                        Picasso.with(ChatActivity.this).load(Uri.parse(imageUrl)).into(fotoPerfil);
                    btnEnviar.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            returnLogin();
        }
    }
    private void returnLogin(){
        startActivity(new Intent(ChatActivity.this, MainActivity.class));
        finish();
    }
    private void LoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {





    }

}
