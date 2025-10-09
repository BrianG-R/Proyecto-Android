package com.example.proyectoandroid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import java.util.List;

// Importa la clase Cliente
import com.example.proyectoandroid.modelo.Cliente;

@Dao
public interface ClienteDao {
    @Query("SELECT * FROM clientes") // 👈 aquí en minúscula y plural
    List<Cliente> getAll();

    @Insert
    void insert(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Update
    void update(Cliente cliente);
}
