package com.example.proyectoandroid.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VisitaRepositoryFirebase {

    private final DatabaseReference ref;

    public VisitaRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("visitas");
    }

    public void insertarVisita(Visita visita) {
        String id = ref.push().getKey();
        ref.child(id).setValue(visita);
    }
}
