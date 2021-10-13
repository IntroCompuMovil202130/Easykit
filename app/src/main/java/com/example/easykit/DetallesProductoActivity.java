package com.example.easykit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetallesProductoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Button verPreguntas;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    String admin= "camicapi@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        verPreguntas= findViewById(R.id.BPreguntas);
    }

    public void verPreguntas(View view){
        Intent intent = new Intent(this, PreguntasProductoActivity.class);
        startActivity(intent);
    }

    public void pago(View view) {
        Intent intent = new Intent(this, PagoActivity.class);
        startActivity(intent);
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
                intent = new Intent(this, UbicacionTiendaActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                intent = new Intent(this, ChatVendedoresActivity.class);
                startActivity(intent);
                break;
            case R.id.agregar:
                if(currentUser.getEmail().equals(admin)){
                    intent = new Intent(this, AgregarProductoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"opción de administrador no permitida",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modificar:
                if(currentUser.getEmail().equals(admin)){
                    intent = new Intent(this, ModificarProductoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"opción de administrador no permitida",Toast.LENGTH_SHORT).show();
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