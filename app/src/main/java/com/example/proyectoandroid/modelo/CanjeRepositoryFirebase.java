package com.example.proyectoandroid.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CanjeRepositoryFirebase {

    private final DatabaseReference ref;

    public CanjeRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("canjes");
    }

    public void insertarCanje(Canje canje) {
        String id = ref.push().getKey();
        canje.id = 0; // El ID local de Room no se usa en Firebase
        ref.child(id).setValue(canje);
    }

    public void eliminarCanje(String keyFirebase) {
        ref.child(keyFirebase).removeValue();
    }
}
