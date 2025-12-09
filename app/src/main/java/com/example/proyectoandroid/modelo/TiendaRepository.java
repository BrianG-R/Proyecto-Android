package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.TiendaDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.database.*;

import java.util.concurrent.Executors;

public class TiendaRepository {

    private final TiendaDao tiendaDao;
    private final DatabaseReference ref;

    public TiendaRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        tiendaDao = db.tiendaDao();

        ref = FirebaseDatabase.getInstance().getReference("tiendas");

        escucharCambiosFirebase();
    }

    // --------------------------------------------
    // 🔥 Leer Firebase en tiempo real → actualizar Room
    // --------------------------------------------
    private void escucharCambiosFirebase() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {
                    tiendaDao.eliminarTodas();

                    for (DataSnapshot s : snapshot.getChildren()) {
                        Tienda t = s.getValue(Tienda.class);
                        if (t != null) {
                            tiendaDao.insertar(t);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // --------------------------------------------
    // 🔥 Guardar tienda en Firebase
    // --------------------------------------------
    public void guardarTienda(Tienda tienda) {

        String key = String.valueOf(tienda.getId_tienda());

        ref.child(key).setValue(tienda);
    }

    // --------------------------------------------
    // 🔥 Eliminar tienda
    // --------------------------------------------
    public void eliminarTienda(Tienda tienda) {

        String key = String.valueOf(tienda.getId_tienda());
        ref.child(key).removeValue();
    }

    // --------------------------------------------
    // 🔄 Sincronizar manualmente (opcional)
    // --------------------------------------------
    public void sincronizarConRoom() {
        escucharCambiosFirebase();
    }
}
