package com.example.proyectoandroid.firebase;

import com.example.proyectoandroid.modelo.Tienda;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TiendaRepositoryFirebase {

    private final DatabaseReference ref;

    public TiendaRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("tiendas");
    }

    // 🔥 Guardar o actualizar tienda usando su ID de Room
    public void guardarTienda(Tienda tienda) {
        String key = String.valueOf(tienda.getId_tienda());
        ref.child(key).setValue(tienda);
    }

    // 🔥 Eliminar tienda por ID
    public void eliminarTienda(Tienda tienda) {
        String key = String.valueOf(tienda.getId_tienda());
        ref.child(key).removeValue();
    }
}
