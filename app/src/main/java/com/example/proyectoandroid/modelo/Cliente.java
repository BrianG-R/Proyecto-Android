package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    public int id_cliente;
    public String nombre;
    public String email;
    public String telefono;
    public String fecha_nac;
    public String estado;
    public String creado_en;

    public Cliente(String nombre, String email, String telefono, String fecha_nac, String estado, String creado_en) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fecha_nac = fecha_nac;
        this.estado = estado;
        this.creado_en = creado_en;
    }
}
