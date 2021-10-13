package com.example.services;

import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    private FirebaseAuth mAuth;

    public AuthService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean userIsLogged() {
        return mAuth.getCurrentUser() != null;	
    }
}
