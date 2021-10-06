package com.example.easykit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CatalogoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button tematicas;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void detalle(View view) {
        Intent intent = new Intent(this, DetallesProductoActivity.class);
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
            case R.id.salir:
                mAuth.signOut();
                intent = new Intent(this, IngresoActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void mostrarTematicas(View view){
        Intent intent=new Intent(this,DespliegueTematicasActivity.class);
        startActivity(intent);
    }
}