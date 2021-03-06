package com.example.easykit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText nombre,apellido,email,password;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference mStorageRef;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    public static final String PATH_USERS = "usuarios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombre = findViewById(R.id.textNombre);
        apellido = findViewById(R.id.textApellido);
        email = findViewById(R.id.textCorreo);
        password = findViewById(R.id.textPassword);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();


    }

    public void registro(View view) {
        String nombreS = nombre.getText().toString();
        String apellidoS = apellido.getText().toString();
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
                        Usuario usuario = new Usuario();
                        usuario.setNombre(nombreS);
                        usuario.setApellido(apellidoS);
                        usuario.setEmail(correo);
                        usuario.setPassword(contra);

                        Map<String, Object> docData = new HashMap<>();
                        docData.put("id", user.getUid());
                        docData.put("nombre", usuario.getNombre());
                        docData.put("apellido", usuario.getApellido());
                        docData.put("email", usuario.getEmail());
                        docData.put("isAdmin", false);

                        db              .collection("usuarios")
                                        .document(user.getUid())
                                        .set(docData, SetOptions.merge());


                        UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                        upcrb.setDisplayName(correo);
                        user.updateProfile(upcrb.build());
                        updateUI(user);
                    }
                }
            }
        });

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