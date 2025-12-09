package com.example.proyectoandroid.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.dao.ProductosDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.google.firebase.database.*;

import java.util.List;
import java.util.concurrent.Executors;

public class ProductoRepository {

    private final ProductosDao productosDao;
    private final DatabaseReference ref;

    public ProductoRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        productosDao = db.productoDao();

        ref = FirebaseDatabase.getInstance().getReference("productos");

        iniciarListenerTiempoReal();
    }

    // ============================================================
    // 🔥 LISTENER EN TIEMPO REAL — SINCRONIZA FIREBASE → ROOM
    // ============================================================
    private void iniciarListenerTiempoReal() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Executors.newSingleThreadExecutor().execute(() -> {

                    // 🔥 Limpiar Room y volver a llenarlo con Firebase
                    List<Producto> actuales = productosDao.getAll();
                    for (Producto p : actuales) {
                        productosDao.delete(p);
                    }

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Producto p = child.getValue(Producto.class);
                        if (p != null) {
                            try {
                                p.setId(Integer.parseInt(child.getKey()));
                            } catch (Exception ignored) {}

                            productosDao.insert(p);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // ============================================================
    // 🔹 INSERTAR PRODUCTO
    // ============================================================
    public void insertar(Producto p) {

        if (p.getId() == 0) {
            // Generar ID basado en la clave push de Firebase
            String key = ref.push().getKey();
            if (key == null) return;

            p.setId(Math.abs(key.hashCode()));
        }

        String key = String.valueOf(p.getId());

        // Actualizar Firebase
        ref.child(key).setValue(p);

        // Guardar en Room
        Executors.newSingleThreadExecutor().execute(() -> {
            productosDao.insert(p);
        });
    }

    // ============================================================
    // 🔹 ACTUALIZAR PRODUCTO
    // ============================================================
    public void actualizar(Producto p) {
        String key = String.valueOf(p.getId());

        ref.child(key).setValue(p);

        Executors.newSingleThreadExecutor().execute(() -> {
            productosDao.update(p);
        });
    }

    // ============================================================
    // 🔹 ELIMINAR PRODUCTO
    // ============================================================
    public void eliminar(Producto p) {
        String key = String.valueOf(p.getId());

        ref.child(key).removeValue();

        Executors.newSingleThreadExecutor().execute(() -> {
            productosDao.delete(p);
        });
    }

    // ============================================================
    // 🔹 Obtener productos desde ROOM
    // ============================================================
    public List<Producto> obtenerLocal() {
        return productosDao.getAll();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productosDao.buscarPorNombre(nombre);
    }
}
