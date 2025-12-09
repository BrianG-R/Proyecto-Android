package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.proyectoandroid.dao.CanjeDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CanjeRepository {

    private final CanjeDao canjeDao;
    private final DatabaseReference ref;
    private final String uid;

    public CanjeRepository(Context context) {

        AppDataBase db = AppDataBase.getInstance(context);
        canjeDao = db.canjeDao();

        uid = FirebaseAuth.getInstance().getUid();

        ref = FirebaseDatabase.getInstance()
                .getReference("canjes")
                .child(uid);

        iniciarSincronizacionTiempoReal();
    }

    // =========================================================================
    // 🔥 SINCRONIZACIÓN EN TIEMPO REAL → Firebase → Room
    // =========================================================================
    private void iniciarSincronizacionTiempoReal() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    for (DataSnapshot child : snapshot.getChildren()) {

                        Canje c = child.getValue(Canje.class);

                        if (c != null) {
                            canjeDao.insert(c);
                        }
                    }


                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // =========================================================================
    // 🔥 Guardar canje en Firebase + Room (FORMATO CORRECTO)
    // =========================================================================
    public void insertarCanje(Canje canje) {

        if (uid == null) return;

        // 🔥 Convertir el objeto a un mapa para enviar bien la fecha
        Map<String, Object> map = new HashMap<>();
        map.put("id_beneficio", canje.id_beneficio);
        map.put("uid", canje.uid);
        map.put("fecha_canje", new Date().getTime());  // TIMESTAMP REAL ✔

        String key = ref.push().getKey();
        ref.child(key).setValue(map);

        // Guardar en Room (usando Date)
        Canje local = new Canje(canje.id_beneficio, canje.uid, new Date());

        Executors.newSingleThreadExecutor().execute(() -> {
            canjeDao.insert(local);
        });
    }

    // =========================================================================
    // 🔥 Obtener historial como LiveData
    // =========================================================================
    public LiveData<List<Canje>> observarHistorial() {
        return canjeDao.observarCanjesPorUid(uid);
    }

    // =========================================================================
    // 🔥 Obtener historial una vez
    // =========================================================================
    public List<Canje> obtenerHistorial() {
        return canjeDao.obtenerCanjesPorUid(uid);
    }

    // =========================================================================
    // 🔥 Puntos gastados
    // =========================================================================
    public int puntosGastados() {
        Integer total = canjeDao.totalPuntosCanjeados(uid);
        return total == null ? 0 : total;
    }
}
