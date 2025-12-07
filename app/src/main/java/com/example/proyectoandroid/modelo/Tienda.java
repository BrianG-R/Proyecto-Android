package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "tienda")
public class Tienda {

    @PrimaryKey(autoGenerate = true)
    private int id_tienda;

    private String nombre;
    private String direccion;
    private String horario;
    private String estado;

    private double lat;
    private double lon;

    public Tienda(String nombre, String direccion, String horario, String estado,
                  double lat, double lon) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.horario = horario;
        this.estado = estado;
        this.lat = lat;
        this.lon = lon;
    }

    @Ignore
    public Tienda(String nombre, String direccion, String horario, String estado) {
        this(nombre, direccion, horario, estado, 0.0, 0.0);
    }

    // Getters y Setters
    public int getId_tienda() { return id_tienda; }
    public void setId_tienda(int id_tienda) { this.id_tienda = id_tienda; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
}
