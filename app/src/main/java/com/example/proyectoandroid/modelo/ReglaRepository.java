package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.ReglaDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.database.*;

import java.util.List;
import java.util.concurrent.Executors;

public class ReglaRepository {

    private final ReglaDao reglaDao;
    private final DatabaseReference ref;

    public ReglaRepository(Context context) {

        AppDataBase db = AppDataBase.getInstance(context);
        reglaDao = db.reglaDao();

        ref = FirebaseDatabase.getInstance().getReference("reglas");

        iniciarListenerTiempoReal();
    }

    // ============================================================================
    // 🔥 1) Listener en tiempo real → Firebase sincroniza Room automáticamente
    // ============================================================================
    private void iniciarListenerTiempoReal() {

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    // Limpiar reglas locales para evitar duplicados
                    reglaDao.eliminarTodas();

                    // Insertar reglas descargadas de Firebase
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Regla r = child.getValue(Regla.class);

                        if (r != null) {
                            reglaDao.insert(r);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    // ============================================================================
    // 🔥 2) Insertar regla en Firebase + Room
    // ============================================================================
    public void insertar(Regla regla) {

        String key = ref.push().getKey();

        if (key != null) {
            regla.setId_regla(key.hashCode());
            ref.child(key).setValue(regla);
        }

        Executors.newSingleThreadExecutor().execute(() -> reglaDao.insert(regla));
    }


    // ============================================================================
    // 🔥 3) Actualizar regla
    // ============================================================================
    public void actualizar(Regla regla) {

        String key = String.valueOf(regla.getId_regla());

        ref.child(key).setValue(regla);

        Executors.newSingleThreadExecutor().execute(() -> reglaDao.update(regla));
    }


    // ============================================================================
    // 🔥 4) Eliminar regla
    // ============================================================================
    public void eliminar(Regla regla) {

        String key = String.valueOf(regla.getId_regla());

        ref.child(key).removeValue();

        Executors.newSingleThreadExecutor().execute(() -> reglaDao.delete(regla));
    }


    // ============================================================================
    // 🔥 5) Obtener reglas locales (Room)
    // ============================================================================
    public List<Regla> obtenerReglasLocal() {
        return reglaDao.getAll();
    }
}
