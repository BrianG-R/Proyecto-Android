package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.ClienteDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.database.*;

import java.util.List;
import java.util.concurrent.Executors;

public class ClienteRepository {

    private final ClienteDao clienteDao;
    private final DatabaseReference ref;

    public ClienteRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        clienteDao = db.clienteDao();

        ref = FirebaseDatabase.getInstance().getReference("clientes");

        iniciarListenerTiempoReal();
    }


    // =====================================================================
    // 🔥 1. Listener Real-Time → Firebase → ROOM
    // =====================================================================
    private void iniciarListenerTiempoReal() {

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    // Limpiar clientes locales
                    List<Cliente> actuales = clienteDao.getAllClientes();
                    for (Cliente c : actuales) {
                        clienteDao.delete(c);
                    }

                    // Insertar todo lo que está en Firebase
                    for (DataSnapshot child : snapshot.getChildren()) {

                        Cliente cliente = child.getValue(Cliente.class);

                        if (cliente != null) {
                            // Forzamos UID desde la key Firebase
                            cliente.setUid(child.getKey());
                            clienteDao.insert(cliente);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    // =====================================================================
    // 🔥 2. Agregar Cliente
    // =====================================================================
    public void insertar(Cliente cliente) {

        if (cliente.getUid() == null || cliente.getUid().isEmpty()) {
            throw new IllegalArgumentException("Cliente debe tener UID válido");
        }

        ref.child(cliente.getUid()).setValue(cliente);

        Executors.newSingleThreadExecutor().execute(() -> {
            clienteDao.insert(cliente);
        });
    }


    // =====================================================================
    // 🔥 3. Actualizar Cliente
    // =====================================================================
    public void actualizar(Cliente cliente) {

        ref.child(cliente.getUid()).setValue(cliente);

        Executors.newSingleThreadExecutor().execute(() -> {
            clienteDao.update(cliente);
        });
    }


    // =====================================================================
    // 🔥 4. Eliminar Cliente
    // =====================================================================
    public void eliminar(Cliente cliente) {

        ref.child(cliente.getUid()).removeValue();

        Executors.newSingleThreadExecutor().execute(() -> {
            clienteDao.delete(cliente);
        });
    }


    // =====================================================================
    // 🔥 5. Obtener desde Room
    // =====================================================================
    public List<Cliente> obtenerLocal() {
        return clienteDao.getAllClientes();
    }

    public Cliente obtenerPorUid(String uid) {
        return clienteDao.getByUid(uid);
    }
}
