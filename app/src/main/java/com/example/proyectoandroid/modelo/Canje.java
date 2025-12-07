package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "canjes",
        foreignKeys = {
                @ForeignKey(entity = Beneficio.class,
                        parentColumns = "id",
                        childColumns = "id_beneficio",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Cliente.class,
                        parentColumns = "uid",
                        childColumns = "uid",
                        onDelete = ForeignKey.CASCADE)
        })
public class Canje {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int id_beneficio;

    public String uid;

    public Date fecha_canje;

    public Canje(int id_beneficio, String uid, Date fecha_canje) {
        this.id_beneficio = id_beneficio;
        this.uid = uid;
        this.fecha_canje = fecha_canje;
    }
}
