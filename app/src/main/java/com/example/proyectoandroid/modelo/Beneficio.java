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
    public String urlFoto;

    @ColumnInfo(name = "costo")
    public int costo;

    public Beneficio() {
        this.urlFoto = "";
        this.costo = 0;
    }

    public Beneficio(String nombre, String descripcion, String urlFoto, int costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlFoto = urlFoto;
        this.costo = costo;
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

    public int getCosto() { return costo; }
    public void setCosto(int costo) { this.costo = costo; }
}
