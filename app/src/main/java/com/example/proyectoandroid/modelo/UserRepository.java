package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.dao.ClienteDao;
import com.example.proyectoandroid.modelo.Cliente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.concurrent.Executors;


public class UserRepository {

    private final ClienteDao clienteDao;
    private final DatabaseReference dbRef;
    private final String uid;

    public UserRepository(Context context) {

        this.uid = FirebaseAuth.getInstance().getUid();

        AppDataBase db = AppDataBase.getInstance(context);
        clienteDao = db.clienteDao();

        dbRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid);
    }

    // ============================================================
    // 🔹 1. Obtener primero desde ROOM (rápido)
    // ============================================================
    public Cliente getClienteLocal() {
        return clienteDao.getByUid(uid);
    }

    // ============================================================
    // 🔹 2. Obtener desde Firebase y sincronizar Room
    // ============================================================
    public void syncWithFirebase(UserSyncCallback callback) {

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onComplete(null);
                    return;
                }

                Cliente c = snapshot.getValue(Cliente.class);

                if (c != null) {

                    // 🔥 Firebase NO incluye el uid dentro del objeto → debemos asignarlo
                    c.setUid(uid);


                    Executors.newSingleThreadExecutor().execute(() -> {

                        if (clienteDao.exists(uid) == 0) {
                            clienteDao.insert(c);
                        } else {
                            clienteDao.update(c);
                        }

                        callback.onComplete(c);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onComplete(null);
            }
        });
    }



    // ============================================================
    // 🔹 3. Guardar cambios en Firebase + Room
    // ============================================================
    public void getClienteLocalAsync(LocalCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Cliente c = clienteDao.getByUid(uid);
            callback.onResult(c);
        });
    }

    public interface LocalCallback {
        void onResult(Cliente cliente);
    }



    // ============================================================
    // 🔹 Callback para sincronización
    // ============================================================
    public interface UserSyncCallback {
        void onComplete(Cliente cliente);
    }
    public void updateCliente(Cliente cliente) {
        // Actualizar en Firebase
        dbRef.setValue(cliente);

        // Actualizar en Room (en hilo secundario)
        Executors.newSingleThreadExecutor().execute(() -> {
            clienteDao.update(cliente);
        });
    }
}
