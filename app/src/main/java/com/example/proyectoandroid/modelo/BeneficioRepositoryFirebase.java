package com.example.proyectoandroid.firebase;

import com.example.proyectoandroid.modelo.Beneficio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BeneficioRepositoryFirebase {

    private final DatabaseReference ref;

    public BeneficioRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("beneficios");
    }

    public void guardarBeneficio(Beneficio beneficio) {
        String id = ref.push().getKey();
        beneficio.setId(id.hashCode());

        ref.child(id).setValue(beneficio);
    }

    public void actualizarBeneficio(String id, Beneficio beneficio) {
        ref.child(id).setValue(beneficio);
    }

    public void eliminarBeneficio(String id) {
        ref.child(id).removeValue();
    }
}
