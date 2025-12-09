package com.example.proyectoandroid.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "beneficio")
public class Beneficio {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "url_foto")
    public String urlFoto; // <--- ESTO FALTABA

    public Beneficio() { this.urlFoto = ""; }

    public Beneficio(String nombre, String descripcion, String urlFoto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlFoto = urlFoto;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }
}