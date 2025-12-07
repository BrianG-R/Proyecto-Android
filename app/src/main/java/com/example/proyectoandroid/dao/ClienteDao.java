package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import java.util.List;

import com.example.proyectoandroid.modelo.Cliente;

@Dao
public interface ClienteDao {

    @Query("SELECT * FROM Cliente")
    List<Cliente> getAll();

    @Query("SELECT * FROM Cliente WHERE uid = :uid LIMIT 1")
    Cliente getByUid(String uid);

    @Insert
    void insert(Cliente cliente);

    @Update
    void update(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Query("SELECT COUNT(*) FROM Cliente WHERE uid = :uid")
    int exists(String uid);
}