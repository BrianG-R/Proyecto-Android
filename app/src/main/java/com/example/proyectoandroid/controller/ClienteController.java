package com.example.proyectoandroid.controller;

import android.content.Context;

import com.example.proyectoandroid.dao.ClienteDao;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Cliente;

import java.util.List;

public class ClienteController {

    private final ClienteDao clienteDao;

    public ClienteController(Context context) {
        // Usamos la instancia única de AppDataBase
        AppDataBase db = AppDataBase.getInstance(context);
        clienteDao = db.clienteDao();
    }

    // CREATE
    public void agregarCliente(Cliente cliente) {
        if (clienteDao.exists(cliente.getUid()) == 0) {
            clienteDao.insert(cliente);
        } else {
            throw new IllegalArgumentException("El cliente con UID " + cliente.getUid() + " ya existe.");
        }
    }

    // READ
    public List<Cliente> obtenerClientes() {
        return clienteDao.getAll();
    }

    public Cliente obtenerClientePorUid(String uid) {
        return clienteDao.getByUid(uid);
    }

    // UPDATE
    public void actualizarCliente(Cliente cliente) {
        clienteDao.update(cliente);
    }

    // DELETE
    public void eliminarCliente(Cliente cliente) {
        clienteDao.delete(cliente);
    }

    // EXISTS
    public boolean existeCliente(String uid) {
        return clienteDao.exists(uid) > 0;
    }
}