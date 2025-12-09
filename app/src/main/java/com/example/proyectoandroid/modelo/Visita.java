package com.example.proyectoandroid.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity
public class Visita {

    @PrimaryKey(autoGenerate = true)
    public int id_visita;

    public String uid;
    public int id_tienda;

    public Date fecha_hora;

    public int diaScan;

    public String estado_sync;

    public String hash_qr;
}
