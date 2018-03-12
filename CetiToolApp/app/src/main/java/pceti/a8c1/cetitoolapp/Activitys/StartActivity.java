package pceti.a8c1.cetitoolapp.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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

import pceti.a8c1.cetitoolapp.Fragments.ChatFragment;
import pceti.a8c1.cetitoolapp.Fragments.HorariosFragment;
import pceti.a8c1.cetitoolapp.Fragments.InicioFragment;
import pceti.a8c1.cetitoolapp.Fragments.MapaFragment;
import pceti.a8c1.cetitoolapp.Fragments.NoticiasFragment;
import pceti.a8c1.cetitoolapp.Modelos.Chat;
import pceti.a8c1.cetitoolapp.R;
import pceti.a8c1.cetitoolapp.constantes.variables;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvSlideNombre, tvSlideCorreo;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button btnLogOut;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private TextView textName;
    ImageView slideImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
   private  String Sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Mostrar nombres en el Slide Menú
        NavigationView navigationView1 = findViewById(R.id.nav_view);
        View headerView = navigationView1.getHeaderView(0);
        tvSlideNombre = headerView.findViewById(R.id.tvSlideNombre);
        tvSlideCorreo = headerView.findViewById(R.id.txtSlideCorreo);
        slideImage = headerView.findViewById(R.id.imageSLide);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        //Autentificacion de firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid=user.getUid().toString();
                variables.ARG_RECEIVER_UID=uid;
                if (user == null) {
                    LoginScreen();
                }else{
                     Sender = user.getUid();
                    final String email = user.getEmail();
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tvSlideNombre.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                            tvSlideCorreo.setText(email);
                            String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                            if (URLUtil.isValidUrl(imageUrl))
                                Picasso.with(StartActivity.this).load(Uri.parse(imageUrl)).into(slideImage);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Start_Layout, new InicioFragment()).commit();
        slideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarScreen();
            }
        });
    }



    private void LoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void EditarScreen(){
        Intent intent = new Intent(this, UpdateUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    //public void LogOut
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Llama al fragment manager
        FragmentManager fragmentManager=getSupportFragmentManager();

        if (id == R.id.menu_inicio) {

            fragmentManager.beginTransaction().replace(R.id.Start_Layout, new InicioFragment()).commit();

        } else if (id == R.id.menu_noticias) {

            fragmentManager.beginTransaction().replace(R.id.Start_Layout, new NoticiasFragment()).commit();

        } else if (id == R.id.menu_eventos) {
            Toast.makeText(getApplicationContext(), "Presionó eventos", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menu_chat) {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            Toast.makeText(getApplicationContext(), Sender, Toast.LENGTH_SHORT).show();
            intent.putExtra("senderuid", Sender);
            fragmentManager.beginTransaction().replace(R.id.Start_Layout, new ChatFragment()).commit();

        } else if (id == R.id.menu_perfil) {

            EditarScreen();

        } else if (id == R.id.menu_mapa) {

            //Se inicia el fragment y es reemplazado por el del mapa
            fragmentManager.beginTransaction().replace(R.id.Start_Layout, new MapaFragment()).commit();

        } else if (id == R.id.menu_salir) {
            variables.ARG_RECEIVER_UID = "";
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            LoginScreen();

        } else if (id == R.id.menu_horario) {

            fragmentManager.beginTransaction().replace(R.id.Start_Layout, new HorariosFragment()).commit();

        } else if (id == R.id.menu_cali) {
            Toast.makeText(getApplicationContext(), "Presionó calificaciones", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.menu_contacto) {
            Toast.makeText(getApplicationContext(), "Presionó contacto", Toast.LENGTH_SHORT).show();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
}