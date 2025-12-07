package com.example.proyectoandroid.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import java.util.List;
import com.example.proyectoandroid.modelo.Producto;
@Dao

public interface
ProductosDao {
    @Insert
    void insert(Producto producto);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);

    @Query("SELECT * FROM Producto")
    List<Producto> getAll();

    @Query("SELECT * FROM Producto WHERE nombre LIKE '%' || :nombre || '%'")
    List<Producto> buscarPorNombre(String nombre);
}
