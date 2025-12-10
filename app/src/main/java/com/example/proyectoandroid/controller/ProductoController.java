package com.example.proyectoandroid.controller;

import android.content.Context; // IMPORTANTE

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.dao.ProductosDao;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.modelo.ProductoRepository; // IMPORTANTE
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductoController {
    private final ProductosDao productosDao;
    private final DatabaseReference mDatabase;
    private final ProductoRepository repository; // Declarar repositorio para activar el listener

    // CAMBIO: El constructor ahora pide Context para iniciar el repositorio
    public ProductoController(Context context) {
        // Usamos la instancia Singleton de la base de datos
        AppDataBase db = AppDataBase.getInstance(context);
        this.productosDao = db.productoDao();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("productos");

        // ¡ESTA ES LA CLAVE! Al instanciar el repositorio, arranca el Listener de Firebase
        this.repository = new ProductoRepository(context);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productosDao.buscarPorNombre(nombre);
    }

    public void agregarProducto(String nombre, double precio, boolean disponible, int stock, String imagen) {
        Producto producto = new Producto(nombre, precio, disponible, stock, imagen);

        // Guardamos en local (Room)
        productosDao.insert(producto);

        // Guardamos en Nube (Firebase)
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
        productosDao.update(producto);
        mDatabase.child(String.valueOf(producto.getId())).setValue(producto);
    }

    public void eliminarProducto(Producto producto) {
        productosDao.delete(producto);
        mDatabase.child(String.valueOf(producto.getId())).removeValue();
    }
}