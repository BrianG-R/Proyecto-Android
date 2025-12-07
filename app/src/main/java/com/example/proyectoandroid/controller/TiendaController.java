package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.dao.TiendaDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.firebase.TiendaRepositoryFirebase;
import com.example.proyectoandroid.modelo.Tienda;

import java.util.List;

public class TiendaController {

    private final TiendaDao tiendaDao;
    private final TiendaRepositoryFirebase firebaseRepo;

    public TiendaController(AppDataBase db) {
        this.tiendaDao = db.tiendaDao();
        this.firebaseRepo = new TiendaRepositoryFirebase();
    }

    // Crear tienda
    public void agregarTienda(String nombre, String direccion, String horario,
                             String estado, float lat, float lon) {

        Tienda tienda = new Tienda(
                nombre,
                direccion,
                horario,
                estado,
                (double) lat,
                (double) lon
        );

        tiendaDao.insertar(tienda);
    }

    // Obtener todas las tiendas
    public List<Tienda> obtenerTiendas() {
        return tiendaDao.obtenerTiendas();
    }

    // Actualizar tienda
    public void actualizarTienda(Tienda tienda) {
        tiendaDao.actualizar(tienda);
        firebaseRepo.guardarTienda(tienda); // actualizar también online
    }

    // Eliminar tienda
    public void eliminarTienda(Tienda tienda) {
        tiendaDao.eliminar(tienda);
        firebaseRepo.eliminarTienda(tienda);
    }

    // Buscar tiendas por nombre
    public List<Tienda> buscarTiendas(String nombre) {
        return tiendaDao.buscarTiendas("%" + nombre + "%");
    }
}
