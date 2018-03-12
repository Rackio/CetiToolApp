package pceti.a8c1.cetitoolapp.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pceti.a8c1.cetitoolapp.Fragments.NoticiasFragment;
import pceti.a8c1.cetitoolapp.Modelos.Coments;
import pceti.a8c1.cetitoolapp.Modelos.Noticias;
import pceti.a8c1.cetitoolapp.R;

public class ComentarNoticiaActivity extends AppCompatActivity {

    private EditText post_coment;
    private Button add_coment;
    private ProgressDialog progressDialog;
    private RecyclerView ComentsLista;

    public String NoticiaID;
    public String Nombre;

    private DatabaseReference mDatabaseComent;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentar_noticia);

        NoticiaID= getIntent().getExtras().getString("noticia_id");

        post_coment= findViewById(R.id.et_coment);
        add_coment= findViewById(R.id.add_coment);
        progressDialog = new ProgressDialog(this);

        ComentsLista= findViewById(R.id.Coment_list);
        ComentsLista.hasFixedSize();
        ComentsLista.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDatabaseComent = FirebaseDatabase.getInstance().getReference().child("Comentarios");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        add_coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

    }

    private void publicar() {

        progressDialog.setMessage("Publicando...");
        progressDialog.show();

        final String coment=post_coment.getText().toString().trim();
        final String noticiaID=NoticiaID.toString().trim();

        final DatabaseReference newComent= mDatabaseComent.push();

        if(firebaseAuth.getCurrentUser()!=null){
            mDatabaseUsers.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Nombre.valueOf(dataSnapshot.child("name").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        mDatabaseComent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newComent.child("noticia_id").setValue(noticiaID);
                newComent.child("uid").setValue(currentUser.getUid());
                newComent.child("comentario").setValue(coment);
                newComent.child("user_name").setValue(Nombre);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog.dismiss();

        Toast.makeText(getApplicationContext(),"Publicado con éxito",Toast.LENGTH_SHORT).show();

        finish();
    }


    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Coments, ComentsViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Coments, ComentsViewHolder>(
                Coments.class,
                R.layout.coment_row,
                ComentarNoticiaActivity.ComentsViewHolder.class,
                mDatabaseComent
        ) {
            @Override
            protected void populateViewHolder(final ComentsViewHolder viewHolder, final Coments model, int position) {

                final String post_key=getRef(position).getKey();


                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUid(model.getUid());
                viewHolder.setComent(model.getComentario());

              //Compara para que sea igual el ID de la noticia al id presionado y mostrar los comentarios (No sirve)
                mDatabaseComent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getValue().equals(NoticiaID)){

                            }else{

                                //No iguales

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        ComentsLista.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ComentsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ComentsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        final public void setUser_name(String user_name){

            TextView post_coment_user = mView.findViewById(R.id.post_coment_user);
            post_coment_user.setText(user_name);
        }

        final public void setUid(String uid){

            TextView post_coment_uid = mView.findViewById(R.id.post_coment_uid);
            post_coment_uid.setText(uid);
        }

        final public void setComent(String comentario){

            TextView post_coment = mView.findViewById(R.id.post_coment_coment);
            post_coment.setText(comentario);
        }

        //////////////////////////////////////////////////////////////////////
/*

        Para borrar los cardview (Mostrarlos en blanco) si es que no se encuentra comentarios en la noticia (Según en l if que se encurntra arriba)
        final public void setComent_empty(){

            TextView post_coment_empty = (TextView)mView.findViewById(R.id.post_coment_coment);
            post_coment_empty.setText("");
        }

        final public void setUid_empty(){

            TextView post_coment_uid_empty = (TextView)mView.findViewById(R.id.post_coment_uid);
            post_coment_uid_empty.setText("");
        }


        final public void setUser_name_empty(){

            TextView post_coment_user_empty = (TextView)mView.findViewById(R.id.post_coment_user);
            post_coment_user_empty.setText("");
        }*/
    }

}
