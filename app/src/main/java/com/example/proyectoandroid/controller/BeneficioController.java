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

    // -------------------------------------------------------
    // 🔥 AGREGAR BENEFICIO (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void agregarBeneficio(String nombre, String descripcion) {
        Beneficio nuevo = new Beneficio(nombre, descripcion);

        // Guardar en Room
        db.beneficioDao().insert(nuevo);

        // Guardar en Firebase (usamos "push" porque ID autogenerado no se conoce antes)
        String key = beneficioRef.push().getKey();
        nuevo.setId(Integer.parseInt(key.hashCode() + ""));  // generamos ID consistente

        beneficioRef.child(String.valueOf(nuevo.getId())).setValue(nuevo);
    }

    // -------------------------------------------------------
    // 🔥 ACTUALIZAR BENEFICIO (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void actualizarBeneficio(Beneficio beneficio) {
        db.beneficioDao().update(beneficio);

        beneficioRef.child(String.valueOf(beneficio.getId()))
                .setValue(beneficio);
    }

    // -------------------------------------------------------
    // 🔥 ELIMINAR BENEFICIO (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void eliminarBeneficio(Beneficio beneficio) {
        db.beneficioDao().delete(beneficio);

        beneficioRef.child(String.valueOf(beneficio.getId()))
                .removeValue();
    }

    // -------------------------------------------------------
    // 🔥 LISTAR DESDE ROOM
    // -------------------------------------------------------
    public List<Beneficio> obtenerBeneficios() {
        return db.beneficioDao().getAllBeneficios();
    }
}
