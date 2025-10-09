package com.example.proyectoandroid.controller;

import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Beneficio;
import java.util.List;

public class BeneficioController {

    private final AppDataBase db;

    public BeneficioController(AppDataBase db) {
        this.db = db;
    }

    public void agregarBeneficio(String nombre, String descripcion) {
        Beneficio nuevoBeneficio = new Beneficio(nombre, descripcion);
        db.beneficioDao().insert(nuevoBeneficio);
    }

    public void eliminarBeneficio(Beneficio beneficio) {
        db.beneficioDao().delete(beneficio);
    }

    public void actualizarBeneficio(Beneficio beneficio) {
        db.beneficioDao().update(beneficio);
    }

    public List<Beneficio> obtenerBeneficios() {
        return db.beneficioDao().getAllBeneficios();
    }
}
