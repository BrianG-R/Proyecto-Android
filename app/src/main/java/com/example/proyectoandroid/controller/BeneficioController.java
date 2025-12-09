package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Beneficio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class BeneficioController {
    private final AppDataBase db;
    private final DatabaseReference beneficioRef;

    public BeneficioController(AppDataBase db) {
        this.db = db;
        this.beneficioRef = FirebaseDatabase.getInstance().getReference("beneficios");
    }

    public void agregarBeneficio(String nombre, String descripcion, String urlFoto, int costo) {
        Beneficio nuevo = new Beneficio(nombre, descripcion, urlFoto, costo);
        db.beneficioDao().insert(nuevo);

        String key = beneficioRef.push().getKey();
        if (key != null) {
            nuevo.setId(Math.abs(key.hashCode()));
            beneficioRef.child(String.valueOf(nuevo.getId())).setValue(nuevo);
        }
    }

    public void actualizarBeneficio(Beneficio beneficio) {
        db.beneficioDao().update(beneficio);
        beneficioRef.child(String.valueOf(beneficio.getId())).setValue(beneficio);
    }

    public void eliminarBeneficio(Beneficio beneficio) {
        db.beneficioDao().delete(beneficio);
        beneficioRef.child(String.valueOf(beneficio.getId())).removeValue();
    }

    public List<Beneficio> obtenerBeneficios() {
        return db.beneficioDao().getAllBeneficios();
    }
}