package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.dao.ProductosDao;
import com.example.proyectoandroid.modelo.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductoController {
    private final ProductosDao productosDao;
    private final DatabaseReference mDatabase;

    public ProductoController(AppDataBase db) {
        this.productosDao = db.productoDao();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("productos");
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productosDao.buscarPorNombre(nombre);
    }

    // MÉTODO ACTUALIZADO: Ahora recibe 'String imagen'
    public void agregarProducto(String nombre, double precio, boolean disponible, int stock, String imagen) {

        // Creamos el producto con todos los datos, incluyendo la imagen
        Producto producto = new Producto(nombre, precio, disponible, stock, imagen);

        // 1. Guardar en Room (Local)
        productosDao.insert(producto);

        // 2. Guardar en Firebase (Nube)
        if (producto.getId() == 0) {
            String key = mDatabase.push().getKey();
            if (key != null) {
                mDatabase.child(key).setValue(producto);
            }
        } else {
            mDatabase.child(String.valueOf(producto.getId())).setValue(producto);
        }
    }

    public List<Producto> obtenerProductos() {
        return productosDao.getAll();
    }

    public void actualizarProducto(Producto producto) {
        // Actualizar en Local
        productosDao.update(producto);

        // Actualizar en Nube (Simulando actualización simple)
        // Nota: En un entorno real idealmente usarías la key de Firebase,
        // pero para esta demo asumimos que se sincroniza
        mDatabase.child(String.valueOf(producto.getId())).setValue(producto);
    }

    public void eliminarProducto(Producto producto) {
        productosDao.delete(producto);
        // Eliminar de Nube si fuera necesario
        mDatabase.child(String.valueOf(producto.getId())).removeValue();
    }
}