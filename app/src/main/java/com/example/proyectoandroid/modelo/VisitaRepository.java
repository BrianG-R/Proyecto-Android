package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.VisitaDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;
import java.util.concurrent.Executors;

public class VisitaRepository {

    private final VisitaDao visitaDao;
    private final DatabaseReference ref;
    private final String uid;

    public VisitaRepository(Context context) {

        AppDataBase db = AppDataBase.getInstance(context);
        visitaDao = db.visitaDao();

        uid = FirebaseAuth.getInstance().getUid();
        ref = FirebaseDatabase.getInstance().getReference("visitas").child(uid);

        iniciarListenerTiempoReal();
    }


    // ============================================================================
    // 🔥 1) Listener Real-Time → Firebase sincroniza Room
    // ============================================================================
    private void iniciarListenerTiempoReal() {

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    // ✔ BORRAR TODAS LAS VISITAS EXISTENTES DEL USUARIO
                    visitaDao.eliminarVisitasPorUid(uid);

                    // ✔ INSERTAR LAS VISITAS DESDE FIREBASE
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Visita v = child.getValue(Visita.class);
                        if (v != null) {
                            visitaDao.insertarVisita(v);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }



    // ============================================================================
    // 🔥 2) Insertar visita local y en Firebase
    // ============================================================================
    public void insertarVisita(Visita visita) {

        if (uid == null) return;

        // Subir a Firebase dentro de /visitas/UID/pushID
        String key = ref.push().getKey();
        ref.child(key).setValue(visita);

        Executors.newSingleThreadExecutor().execute(() -> {
            visitaDao.insertarVisita(visita);
        });
    }


    // ============================================================================
    // 🔥 3) Obtener historial local
    // ============================================================================
    public List<Visita> historialLocal() {
        return visitaDao.obtenerHistorialVisitas(uid);
    }

    // ============================================================================
    // 🔥 4) Obtener el conteo de puntos (visitas - canjes)
    // ============================================================================
    public int totalVisitas() {
        return visitaDao.totalVisitasPorUsuario(uid);
    }

    // ============================================================================
    // 🔥 5) Verificar si ya escaneó en el día
    // ============================================================================
    public Visita obtenerVisitaDeHoy(int dia) {
        return visitaDao.obtenerVisitaDeHoy(uid, dia);
    }
}
