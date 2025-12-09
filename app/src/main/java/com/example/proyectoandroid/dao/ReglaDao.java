package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;
import com.example.proyectoandroid.modelo.Regla;

@Dao
public interface ReglaDao {

    @Insert
    void insert(Regla regla);

    @Update
    void update(Regla regla);

    @Delete
    void delete(Regla regla);

    @Query("SELECT * FROM Regla")
    List<Regla> getAll();

    @Query("DELETE FROM Regla")
    void eliminarTodas();
}
