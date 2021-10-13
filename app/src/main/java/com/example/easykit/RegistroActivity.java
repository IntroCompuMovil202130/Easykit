package com.example.easykit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroActivity extends AppCompatActivity {

    private EditText nombre,apellido,email,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombre = findViewById(R.id.textNombre);
        apellido = findViewById(R.id.textApellido);
        email = findViewById(R.id.textCorreo);
        password = findViewById(R.id.textPassword);
        mAuth = FirebaseAuth.getInstance();


    }

    public void registro(View view) {
        String correo = email.getText().toString();
        String contra = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(correo ,contra).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                        UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                        upcrb.setDisplayName(correo);
                        user.updateProfile(upcrb.build());
                        updateUI(user);
                    }
                }
            }
        });


//        Intent intent = new Intent(this, AutenticacionActivity.class);
//        startActivity(intent);
    }

    private void updateUI(FirebaseUser currentUser){
        mAuth.signOut();
        nombre.setText("");
        apellido.setText("");
        email.setText("");
        password.setText("");
        Intent intent = new Intent(getBaseContext(),AutenticacionActivity.class);
//        intent.putExtra("user",currentUser.getEmail());
        startActivity(intent);
    }
}