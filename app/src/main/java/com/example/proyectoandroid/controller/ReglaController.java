package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Regla;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReglaController {

    private final AppDataBase db;
    private final DatabaseReference reglasRef;

    public ReglaController(AppDataBase db) {
        this.db = db;
        this.reglasRef = FirebaseDatabase.getInstance().getReference("reglas");
    }

    // -------------------------------------------------------
    // 🔥 INSERTAR (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void agregarRegla(String nombre, String descripcion, String otros) {
        Regla nueva = new Regla(nombre, descripcion, otros);

        // Guardar en local
        db.reglaDao().insert(nueva);

        // El ID se genera después del insert
        int id = nueva.getId_regla();

        // Guardar en Firebase
        reglasRef.child(String.valueOf(id)).setValue(nueva);
    }

    // -------------------------------------------------------
    // 🔥 OBTENER DESDE ROOM
    // -------------------------------------------------------
    public List<Regla> obtenerReglas() {
        return db.reglaDao().getAll();
    }

    // -------------------------------------------------------
    // 🔥 ACTUALIZAR (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void actualizarRegla(Regla regla) {
        db.reglaDao().update(regla);

        reglasRef.child(String.valueOf(regla.getId_regla()))
                .setValue(regla);
    }

    // -------------------------------------------------------
    // 🔥 ELIMINAR (ROOM + FIREBASE)
    // -------------------------------------------------------
    public void eliminarRegla(Regla regla) {
        db.reglaDao().delete(regla);

        reglasRef.child(String.valueOf(regla.getId_regla()))
                .removeValue();
    }
}
