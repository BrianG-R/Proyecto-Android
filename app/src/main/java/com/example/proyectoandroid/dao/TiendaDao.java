package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.proyectoandroid.modelo.Tienda;

import java.util.List;

@Dao
public interface TiendaDao {

    @Insert
    long insertar(Tienda tienda);

    @Update
    void actualizar(Tienda tienda);

    @Delete
    void eliminar(Tienda tienda);

    @Query("SELECT * FROM tienda ORDER BY id_tienda DESC")
    List<Tienda> obtenerTiendas();

    @Query("SELECT * FROM tienda WHERE nombre LIKE :busqueda")
    List<Tienda> buscarTiendas(String busqueda);

    // 🔥 NECESARIO PARA SINCRONIZAR ROOM ↔ FIREBASE
    @Query("DELETE FROM tienda")
    void eliminarTodas();
}
