package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.proyectoandroid.modelo.Canje;
import java.util.List;

@Dao
public interface CanjeDao {

    @Insert
    void insertCanje(Canje canje);

    @Delete
    void deleteCanje(Canje canje);

    @Query("SELECT * FROM canjes")
    List<Canje> getAllCanjes();

    @Query("SELECT * FROM canjes WHERE id_beneficio = :beneficioId")
    List<Canje> getCanjesForBeneficio(int beneficioId);
}
