package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "canjes")
public class Canje {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int id_beneficio;
    public String uid;

    // 🔥 Guardar la fecha como timestamp LONG evita errores con Firebase
    public long fecha_canje;

    public Canje() {}

    public Canje(int id_beneficio, String uid, Date fecha) {
        this.id_beneficio = id_beneficio;
        this.uid = uid;
        this.fecha_canje = fecha.getTime();
    }

    public Canje(int id_beneficio, String uid, long fecha) {
        this.id_beneficio = id_beneficio;
        this.uid = uid;
        this.fecha_canje = fecha;
    }
}
