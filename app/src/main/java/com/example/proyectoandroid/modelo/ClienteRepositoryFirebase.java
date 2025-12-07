package com.example.proyectoandroid.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClienteRepositoryFirebase {

    private final DatabaseReference ref;

    public ClienteRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("clientes");
    }

    public void actualizarCliente(Cliente cliente) {
        ref.child(cliente.getUid()).setValue(cliente);
    }

    public void insertarNuevoCliente(Cliente cliente) {
        ref.child(cliente.getUid()).setValue(cliente);
    }
}
