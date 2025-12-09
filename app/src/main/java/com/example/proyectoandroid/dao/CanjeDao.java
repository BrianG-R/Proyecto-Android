package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;


import com.example.proyectoandroid.modelo.Canje;

import java.util.List;

@Dao
public interface CanjeDao {

    @Insert
    long insert(Canje canje);

    @Query("SELECT SUM(b.costo) FROM canjes c INNER JOIN beneficio b ON c.id_beneficio = b.id WHERE c.uid = :uid")
    Integer totalPuntosCanjeados(String uid);

    @Query("SELECT * FROM canjes WHERE uid = :uid ORDER BY fecha_canje DESC")
    List<Canje> historialCanjes(String uid);

    // 🔥 Necesario para sincronización
    @Query("DELETE FROM canjes WHERE uid = :uid")
    void eliminarCanjesPorUid(String uid);

    // 🔥 Nuevo: usado en CanjeRepository
    @Query("SELECT * FROM canjes WHERE uid = :uid ORDER BY fecha_canje DESC")
    List<Canje> obtenerCanjesPorUid(String uid);

    // Opcional pero útil
    @Query("DELETE FROM canjes")
    void eliminarTodos();
    @Query("SELECT * FROM canjes WHERE uid = :uid ORDER BY fecha_canje DESC")
    LiveData<List<Canje>> observarCanjesPorUid(String uid);

}
