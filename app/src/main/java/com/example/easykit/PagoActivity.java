package com.example.easykit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class PagoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    String admin= "camicapi@gmail.com";

    Button continuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

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

        continuar = (Button) findViewById(R.id.boton2);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });
        
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

    private void mostrarDialogo(){
        new AlertDialog.Builder(this)
                .setTitle("Importante")
                .setMessage("El vendedor recibirá el pago cuando usted reciba el producto, de lo contrario usted recibirá el rembolso")
                .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}
