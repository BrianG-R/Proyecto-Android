package com.example.proyectoandroid.firebase;

import com.example.proyectoandroid.modelo.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductoRepositoryFirebase {

    private final DatabaseReference ref;

    public ProductoRepositoryFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("productos");
    }

    public void guardarProducto(Producto producto) {
        String id = ref.push().getKey();
        producto.id_producto = id.hashCode();

        ref.child(id).setValue(producto);
    }

    public void actualizarProducto(String id, Producto producto) {
        ref.child(id).setValue(producto);
    }

    public void eliminarProducto(String id) {
        ref.child(id).removeValue();
    }
}
