package com.example.proyectoandroid.firebase;

import com.example.proyectoandroid.modelo.Regla;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReglaRepositoryFirebase {

    private final DatabaseReference ref;

    public ReglaRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("reglas");
    }

    public void guardarRegla(Regla regla) {
        String id = ref.push().getKey();
        regla.setId_regla(id.hashCode());

        ref.child(id).setValue(regla);
    }

    public void actualizarRegla(String id, Regla regla) {
        ref.child(id).setValue(regla);
    }

    public void eliminarRegla(String id) {
        ref.child(id).removeValue();
    }
}
