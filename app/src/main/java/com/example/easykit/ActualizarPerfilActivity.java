package com.example.easykit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ActualizarPerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener listener;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    FirebaseFirestore db;


    public EditText nombre;
    public EditText apellido;
    public EditText correo;
    public EditText direccion;
    public EditText contrasena;
    public EditText verificarContrasena;

    FirebaseUser currentUser;
    String admin = "camicapi@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        db =  FirebaseFirestore.getInstance();

        nombre = findViewById(R.id.ETNombresActualizar);
        apellido = findViewById(R.id.ETApellidosActualizar);
        correo = findViewById(R.id.ETCorreoActualizar);
        direccion = findViewById(R.id.ETDireccionActualizar);
        contrasena = findViewById(R.id.ETContraActualizar);
        verificarContrasena= findViewById(R.id.ETConfirmarContraActualizar);

//        DocumentReference docRef = db.collection("usuarios")
//                                        .document(currentUser.getUid());

        DocumentReference docRef = db.collection("usuarios").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("HOLABUENAS", "DocumentSnapshot data: " + document.getData());
                        String[] result = document.getData().toString().split("\\,");
                        apellido.setText(result[0].substring(10,result[0].length()));
                        nombre.setText(result[3].substring(8,result[3].length()));
                        correo.setText(result[4].substring(7,result[4].length()-1));
//                        for (int x=0; x<result.length; x++){
//                            System.out.println(result[x]);
//                            Log.d("HOLABUENAS", result[x]);
//                        }

                    } else {
                        Log.d("HOLABUENAS", "No such document");
                    }
                } else {
                    Log.d("HOLABUENAS", "get failed with ", task.getException());
                }
            }
        });


    }

    public void cambiarDatos(View v){
        Usuario usuario = new Usuario();
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", currentUser.getUid());
        docData.put("nombre", nombre.getText().toString());
        docData.put("apellido", apellido.getText().toString());
        docData.put("email", correo.getText().toString());
        docData.put("isAdmin", false);

//        db.collection("usuarios").document(currentUser.getUid()).delete();
        db              .collection("usuarios")
                .document(currentUser.getUid())
                .update(docData);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.catalogoProductos:
                intent = new Intent(this, CatalogoActivity.class);
                startActivity(intent);
                break;
            case R.id.perfil:
                intent = new Intent(this, ActualizarPerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.pedidos:
                intent = new Intent(this, PedidosActivity.class);
                startActivity(intent);
                break;
            case R.id.ubicacion:
                intent = new Intent(this, UbicacionPedidoActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                intent = new Intent(this, ChatVendedoresActivity.class);
                startActivity(intent);
                break;
            case R.id.agregar:
                if (currentUser.getEmail().equals(admin)) {
                    intent = new Intent(this, AgregarProductoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "opción de administrador no permitida", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modificar:
                if (currentUser.getEmail().equals(admin)) {
                    intent = new Intent(this, ModificarProductoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "opción de administrador no permitida", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.salir:
                intent = new Intent(this, IngresoActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}