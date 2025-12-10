package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Regla {

    @PrimaryKey(autoGenerate = true)
    private int id_regla;

    private String nombre;
    private String descripcion;
    private String otros;

    // 🔥 Constructor vacío obligatorio para Firebase
    public Regla() {}

    // Constructor normal (opcional)
    public Regla(String nombre, String descripcion, String otros) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.otros = otros;
    }

    // Getters y Setters
    public int getId_regla() { return id_regla; }
    public void setId_regla(int id_regla) { this.id_regla = id_regla; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getOtros() { return otros; }
    public void setOtros(String otros) { this.otros = otros; }
}
