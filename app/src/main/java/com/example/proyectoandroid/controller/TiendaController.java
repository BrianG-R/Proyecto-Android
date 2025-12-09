package com.example.proyectoandroid.controller;

import android.content.Context;

import com.example.proyectoandroid.dao.TiendaDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Tienda;
import com.example.proyectoandroid.modelo.TiendaRepository;

import java.util.List;

public class TiendaController {

    private final TiendaDao tiendaDao;
    private final TiendaRepository firebaseRepo;

    public TiendaController(AppDataBase db, Context context) {
        this.tiendaDao = db.tiendaDao();
        this.firebaseRepo = new TiendaRepository(context);  // ✔ ahora requiere contexto
    }

    // --------------------------------------------
    // 🔥 CREAR tienda → Room + Firebase
    // --------------------------------------------
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

        long idLocal = tiendaDao.insertar(tienda);
        tienda.setId_tienda((int) idLocal);

        // 🔥 Subir a Firebase
        firebaseRepo.guardarTienda(tienda);
    }

    // --------------------------------------------
    // 🔥 Obtener tiendas desde ROOM (rápido)
    // --------------------------------------------
    public List<Tienda> obtenerTiendas() {
        return tiendaDao.obtenerTiendas();
    }

    // --------------------------------------------
    // 🔥 Actualizar → Room + Firebase
    // --------------------------------------------
    public void actualizarTienda(Tienda tienda) {
        tiendaDao.actualizar(tienda);
        firebaseRepo.guardarTienda(tienda);
    }

    // --------------------------------------------
    // 🔥 Eliminar → Room + Firebase
    // --------------------------------------------
    public void eliminarTienda(Tienda tienda) {
        tiendaDao.eliminar(tienda);
        firebaseRepo.eliminarTienda(tienda);
    }

    // --------------------------------------------
    // 🔎 Buscar por nombre
    // --------------------------------------------
    public List<Tienda> buscarTiendas(String nombre) {
        return tiendaDao.buscarTiendas("%" + nombre + "%");
    }

    // --------------------------------------------
    // 🔥 Forzar sincronización Room ↔ Firebase
    // --------------------------------------------
    public void sincronizar() {
        firebaseRepo.sincronizarConRoom();
    }
}
