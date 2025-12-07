package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.dao.ProductosDao;
import com.example.proyectoandroid.modelo.Producto;

import java.util.List;

public class ProductoController {
    private final ProductosDao productosDao;

    public ProductoController(AppDataBase db) {
        this.productosDao = db.productoDao();
    }
    public List<Producto> buscarPorNombre(String nombre) {
        return productosDao.buscarPorNombre(nombre);
    }
    public void agregarProducto(String nombre, double precio, boolean disponible) {
        Producto producto = new Producto(nombre, precio, disponible);
        productosDao.insert(producto);
    }

    public List<Producto> obtenerProductos() {
        return productosDao.getAll();
    }

    public void actualizarProducto(Producto producto) {
        productosDao.update(producto);
    }

    public void eliminarProducto(Producto producto) {
        productosDao.delete(producto);
    }
}
