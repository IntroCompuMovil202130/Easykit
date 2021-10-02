package com.example.easykit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AutenticacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);
    }
    
    public void ingreso(View view) {
        Intent intent = new Intent(this, CatalogoActivity.class);
        startActivity(intent);
    }

}