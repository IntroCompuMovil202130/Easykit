package com.example.easykit;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AgregarProductoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    String admin = "camicapi@gmail.com";
    ImageView imagen;
    String permisoCamara = Manifest.permission.CAMERA;
    String permisoGaleria = Manifest.permission.READ_EXTERNAL_STORAGE;
    int idPermisoCamara = 2;
    int idPermisoGaleria = 3;

    private static final int SELECCIONAR_IMAGEN_PERMISO = 0;
    private static final int PERMISO_CAMARA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
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

        imagen = findViewById(R.id.IVBuscarTematicas);

    }

    public void tomarFotos(View view) {
        if (ActivityCompat.checkSelfPermission(this, permisoCamara) != PackageManager.PERMISSION_GRANTED) {
            pedirPermiso(this, permisoCamara, "", idPermisoCamara);
        } else {
            imagenCamara();
        }
    }

    private void imagenCamara() {
        if (ActivityCompat.checkSelfPermission(this, permisoCamara) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permiso_APP", "Dentro del método imagenCamara");
            Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(tomarFotoIntent, PERMISO_CAMARA);
            } catch (ActivityNotFoundException exception) {
                Log.e("Permiso-App", exception.getMessage());
            }
        }
    }

    public void elegirFotos(View view) {
        if (ActivityCompat.checkSelfPermission(this, permisoGaleria) != PackageManager.PERMISSION_GRANTED) {
            pedirPermiso(this, permisoGaleria, "", idPermisoGaleria);
        } else {
            imagenGaleria();
        }
    }

    private void imagenGaleria() {
        if (ActivityCompat.checkSelfPermission(this, permisoGaleria) == PackageManager.PERMISSION_GRANTED) {
            Intent seleccionarImagen = new Intent(Intent.ACTION_PICK);
            seleccionarImagen.setType("image/*");
            startActivityForResult(seleccionarImagen, SELECCIONAR_IMAGEN_PERMISO);
        }
    }

    private void pedirPermiso(Activity contexto, String permiso, String justificacion, int id) {
        if (ActivityCompat.checkSelfPermission(contexto, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(contexto, permiso)) {
                Toast.makeText(contexto, justificacion, Toast.LENGTH_SHORT).show();
            }
            //Pedir permiso
            ActivityCompat.requestPermissions(contexto, new String[]{permiso}, id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == idPermisoCamara) {
            imagenCamara();
        } else if (requestCode == idPermisoGaleria) {
            imagenGaleria();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECCIONAR_IMAGEN_PERMISO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap imagenSeleccionada = BitmapFactory.decodeStream(imageStream);
                        imagen.setImageBitmap(imagenSeleccionada);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PERMISO_CAMARA:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imagen.setImageBitmap(imageBitmap);
                }
        }
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