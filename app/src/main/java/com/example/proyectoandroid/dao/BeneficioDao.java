package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyectoandroid.modelo.Beneficio;
import java.util.List;

@Dao
public interface BeneficioDao {

    @Insert
    void insert(Beneficio beneficio);

    @Update
    void update(Beneficio beneficio);

    @Delete
    void delete(Beneficio beneficio);

    @Query("SELECT * FROM beneficio")
    List<Beneficio> getAllBeneficios();

    @Query("SELECT * FROM beneficio WHERE nombre LIKE :nombre")
    List<Beneficio> findBeneficioByNombre(String nombre);
}
