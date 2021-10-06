package com.example.easykit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutenticacionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.ETCorreoAutenticacion);
        password = findViewById(R.id.ETContrasenaAutenticacion);
    }

    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);


    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(),CatalogoActivity.class);
            intent.putExtra("user",currentUser.getEmail());
            startActivity(intent);
        }else{
            email.setText("");
            password.setText("");
        }
    }

    public void ingreso(View v){
        String correo = email.getText().toString();
        String contra = password.getText().toString();

        if(validar(correo,contra)){
            mAuth.signInWithEmailAndPassword(correo,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(AutenticacionActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                        email.setText("");
                        password.setText("");
                    }else{
                        Intent intent = new Intent(AutenticacionActivity.this, CatalogoActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    /*
    * Validacion de correo y contraseña
    *
    *
    * */

    public boolean validateEmailId(String emailId) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailId);
        return matcher.find();
    }

    public boolean validar(String correo, String contra) {

        if (correo.isEmpty() || contra.isEmpty()) {
            return false;
        }

        //Email invalido
        if(!validateEmailId(correo)){
            return false;
        }

        //Password no puede tener espacios
        else if(!Pattern.matches("[^ ]*", contra)){
            return false;
        }

        return true;
    }

}