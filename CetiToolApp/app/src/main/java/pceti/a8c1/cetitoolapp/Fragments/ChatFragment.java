package pceti.a8c1.cetitoolapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import pceti.a8c1.cetitoolapp.Activitys.ChatActivity;
import pceti.a8c1.cetitoolapp.Activitys.ComentarNoticiaActivity;
import pceti.a8c1.cetitoolapp.Activitys.MainActivity;
import pceti.a8c1.cetitoolapp.Activitys.StartActivity;
import pceti.a8c1.cetitoolapp.Modelos.Noticias;
import pceti.a8c1.cetitoolapp.Modelos.Users;
import pceti.a8c1.cetitoolapp.R;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ChatFragment extends Fragment {
    private StorageReference mStorage;
    private String sender;
    private RecyclerView UsersLista;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users");

        UsersLista= view.findViewById(R.id.UsersChatLista);
        UsersLista.setHasFixedSize(true);
        UsersLista.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        firebaseAuthListener  = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    LoginScreen();
                }else{
                    sender = firebaseAuth.getCurrentUser().getUid().toString();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            sender =String.valueOf(dataSnapshot.child("uid").getValue());
                            Toast.makeText(getApplicationContext(),"Id de usuario"+ sender, Toast.LENGTH_SHORT).show();


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Users, ChatFragment.ChatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, ChatFragment.ChatViewHolder>(
                Users.class,
                R.layout.chat_row,
                ChatFragment.ChatViewHolder.class,
                mDatabaseUsers
        ) {
            @Override
            protected void populateViewHolder(ChatFragment.ChatViewHolder viewHolder, Users model, int position) {

                final String post1_key=getRef(position).getKey();

                viewHolder.setUserName(model.getName());
                viewHolder.setUserChatImage(getApplicationContext(),model.getImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), post1_key, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        Toast.makeText(getApplicationContext(), sender, Toast.LENGTH_SHORT).show();
                        intent.putExtra("receiveruid", post1_key);
                        intent.putExtra("senderuid", sender);
                        startActivity(intent);
                    }
                });
            }
        };

        UsersLista.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public ChatViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUserName(String Titulo){

            TextView chat_user = mView.findViewById(R.id.UserChat);
            chat_user.setText(Titulo);
        }

        public void setUserChatImage(Context ctx, String Imagen){
            ImageView UserChatImage = mView.findViewById(R.id.ImageUserChat);
            Picasso.with(ctx).load(Imagen).into(UserChatImage);
        }
    }
    private void LoginScreen() {

    }
}
