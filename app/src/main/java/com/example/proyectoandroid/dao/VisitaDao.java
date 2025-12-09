package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proyectoandroid.modelo.Visita;   //  ← FALTA ESTE IMPORT

import java.util.List;

@Dao
public interface VisitaDao {

    @Insert
    void insertarVisita(Visita visita);

    @Query("SELECT * FROM visita WHERE uid = :uid ORDER BY fecha_hora DESC")
    List<Visita> obtenerHistorialVisitas(String uid);

    @Query("SELECT * FROM visita WHERE uid = :uid AND diaScan = :dia LIMIT 1")
    Visita obtenerVisitaDeHoy(String uid, int dia);

    @Query("SELECT COUNT(*) FROM visita WHERE uid = :uid")
    int totalVisitasPorUsuario(String uid);

    @Query("DELETE FROM visita WHERE uid = :uid")
    void eliminarVisitasPorUid(String uid);
}