package pceti.a8c1.cetitoolapp.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import pceti.a8c1.cetitoolapp.Activitys.AddNoticia;
import pceti.a8c1.cetitoolapp.Activitys.ComentarNoticiaActivity;
import pceti.a8c1.cetitoolapp.Modelos.Noticias;
import pceti.a8c1.cetitoolapp.R;

import static com.facebook.FacebookSdk.getApplicationContext;


public class NoticiasFragment extends Fragment {

    private RecyclerView NoticiasLista;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Noticias");

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        LayoutManager.setReverseLayout(true);
        LayoutManager.setStackFromEnd(true);

        NoticiasLista= view.findViewById(R.id.Noticias_list);
        NoticiasLista.setHasFixedSize(true);
        NoticiasLista.setLayoutManager(LayoutManager);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerta(view);

            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Noticias, NoticiasViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Noticias, NoticiasViewHolder>(
                Noticias.class,
                R.layout.noticas_row,
                NoticiasViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(NoticiasViewHolder viewHolder, Noticias model, int position) {

                final String post_key=getRef(position).getKey();

                viewHolder.setTitulo(model.getTitulo());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setFiltro(model.getFiltro());
                viewHolder.setUserFacebook(model.getUserFacebook());
                viewHolder.setImagen(getApplicationContext(),model.getImagen());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(getApplicationContext(), ComentarNoticiaActivity.class);
                        intent.putExtra("noticia_id", post_key);
                        startActivity(intent);
                    }
                });

            }
        };

        NoticiasLista.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NoticiasViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public NoticiasViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitulo(String Titulo){

            TextView post_Titulo = mView.findViewById(R.id.post_titulo);
            post_Titulo.setText(Titulo);
        }

        public void setDesc(String desc){
            TextView post_Desc = mView.findViewById(R.id.post_desc);
            post_Desc.setText(desc);
        }

        public void setUserFacebook(String userFacebook){
            TextView post_username = mView.findViewById(R.id.post_user);
            post_username.setText(userFacebook);
        }

        public void setFiltro(String Filtro){
            TextView post_filtro = mView.findViewById(R.id.post_filtro);
            post_filtro.setText(Filtro);
        }

        public void setImagen(Context ctx, String Imagen){
            ImageView post_imagen = mView.findViewById(R.id.post_imagen);
            Picasso.with(ctx).load(Imagen).into(post_imagen);
        }
    }


    private void AddNoticiaScreen(){
        Intent intent = new Intent(getActivity(), AddNoticia.class);
        startActivity(intent);
    }

    public void Alerta(View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //Titulo para la Ventana de Diagolo
        builder.setTitle("Aviso");
        //Mensaje que Emitira la Ventada de Dialogo
        builder.setMessage("Queda estrictamente prohibido postear contenido inapropiado u ofensivo, asimismo recuerda que el consumo de datos se ve afectado dependiendo del tamaño de la imagen.");
        //Accion si presiona el boton Ok
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                AddNoticiaScreen();
            }
        });
        //se crea el objeto tipo Dialogo
        Dialog dialog= builder.create();
        //Se muestra el cuadro de Dialogo
        dialog.show();
    }
}
