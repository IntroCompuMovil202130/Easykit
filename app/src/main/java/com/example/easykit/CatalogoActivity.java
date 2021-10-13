package com.example.easykit;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adapters.AdapterProducto;
import com.example.models.Kit;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;

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

        try {
            inicializarListaProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void inicializarListaProductos() throws IOException {
        Resources res = getResources();
        Drawable d = ResourcesCompat.getDrawable(res, R.drawable.kit, null);

        ArrayList<Kit> kits = new ArrayList<Kit>();
        kits.add(new Kit(0, "Kit 1", "Kit #1", d, 15400));
        kits.add(new Kit(0, "Kit 2", "Kit #2", d, 15400));
        kits.add(new Kit(0, "Kit 3", "Kit #3", d, 15400));

        AdapterProducto adapterKit = new AdapterProducto(this, kits);

        ListView lv = (ListView) findViewById(R.id.LVlistaKits);

        lv.setAdapter(adapterKit);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogoActivity.this, DetallesProductoActivity.class);
                startActivity(intent);
            }
        });
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.FiltroManualidades:
                // Aplicar filtro
                break;
            case R.id.FiltroColegio:
                // Aplicar filtro
                break;
            case R.id.FiltroArte:
                // Aplicar filtro
                break;
            case R.id.FiltroPinturas:
                // Aplicar filtro
                break;
            case R.id.FiltroDidacticos:
                // Aplicar filtro
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}