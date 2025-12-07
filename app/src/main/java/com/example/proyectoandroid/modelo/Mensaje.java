package com.example.proyectoandroid.modelo;

public class Mensaje {

    private String id;
    private String autorUid;
    private String autorNombre;
    private String contenido;
    private long fecha;
    private String fotoAutor;
    private String textoRespuesta;

    public Mensaje() { }

    public Mensaje(String id, String autorUid, String autorNombre, String contenido,
                   long fecha, String fotoAutor, String textoRespuesta) {
        this.id = id;
        this.autorUid = autorUid;
        this.autorNombre = autorNombre;
        this.contenido = contenido;
        this.fecha = fecha;
        this.fotoAutor = fotoAutor;
        this.textoRespuesta = textoRespuesta;
    }

    public String getId() { return id; }
    public String getAutorUid() { return autorUid; }
    public String getAutorNombre() { return autorNombre; }
    public String getContenido() { return contenido; }
    public long getFecha() { return fecha; }
    public String getFotoAutor() { return fotoAutor; }
    public String getTextoRespuesta() { return textoRespuesta; }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }
}
