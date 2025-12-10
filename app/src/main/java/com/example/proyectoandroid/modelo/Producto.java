package com.example.proyectoandroid.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Producto")
public class Producto {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_producto")
    private int id;
    private String nombre;
    private double precio;
    private boolean disponible;
    private int stock;

    // NUEVO CAMPO: Guardará la foto convertida a texto (Base64)
    private String imagen;

    // Constructor vacío
    public Producto() {
    }

    // Constructor actualizado (incluye imagen)
    public Producto(String nombre, double precio, boolean disponible, int stock, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
        this.stock = stock;
        this.imagen = imagen;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // Getters y Setters para Imagen
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}