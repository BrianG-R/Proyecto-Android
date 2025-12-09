package com.example.proyectoandroid.modelo;

import android.content.Context;
import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.BeneficioDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.database.*;

import java.util.concurrent.Executors;
import java.util.List;

public class BeneficioRepository {

    private final BeneficioDao beneficioDao;
    private final DatabaseReference ref;

    public BeneficioRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        beneficioDao = db.beneficioDao();

        ref = FirebaseDatabase.getInstance().getReference("beneficios");

        iniciarListenerTiempoReal();
    }

    // ==================================================================================
    // 🔥 Listener Firebase → sincroniza siempre Room
    // ==================================================================================
    private void iniciarListenerTiempoReal() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    // Borrar todo para sincronizar desde cero
                    beneficioDao.eliminarTodos();

                    for (DataSnapshot child : snapshot.getChildren()) {

                        Beneficio b = child.getValue(Beneficio.class);
                        if (b != null) {

                            // Detectar si la key es número o no
                            try {
                                b.setId(Integer.parseInt(child.getKey()));
                            } catch (Exception e) {
                                // Si la key NO es numérica, generar ID estable
                                b.setId(Math.abs(child.getKey().hashCode()));
                            }

                            beneficioDao.insert(b);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // ==================================================================================
    // 🔥 Insertar beneficio (local + Firebase)
    // ==================================================================================
    public void insertar(Beneficio b) {

        // Si no tiene ID → generar ID estable
        if (b.getId() == 0) {
            String key = ref.push().getKey();
            if (key == null) return;
            b.setId(Math.abs(key.hashCode()));
        }

        String key = String.valueOf(b.getId());

        ref.child(key).setValue(b);

        Executors.newSingleThreadExecutor().execute(() -> beneficioDao.insert(b));
    }

    // ==================================================================================
    // 🔥 Actualizar beneficio
    // ==================================================================================
    public void actualizar(Beneficio b) {
        String key = String.valueOf(b.getId());
        ref.child(key).setValue(b);

        Executors.newSingleThreadExecutor().execute(() -> beneficioDao.update(b));
    }

    // ==================================================================================
    // 🔥 Eliminar beneficio
    // ==================================================================================
    public void eliminar(Beneficio b) {
        String key = String.valueOf(b.getId());
        ref.child(key).removeValue();

        Executors.newSingleThreadExecutor().execute(() -> beneficioDao.delete(b));
    }

    // ==================================================================================
    // 🔥 Obtener desde Room
    // ==================================================================================
    public List<Beneficio> obtenerLocal() {
        return beneficioDao.getAllBeneficios();
    }
}
