package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.Adaptadores.ClienteAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.controller.ClienteController;
import com.example.proyectoandroid.modelo.Cliente;

import java.util.List;

public class ClienteFragment extends Fragment {

    private ClienteController controller;
    private ClienteAdapter adapter;

    private EditText etUid, etNombre, etTelefono, etFechaNacimiento, etEstado, etFoto, etBuscar;
    private Button btnCrear, btnModificar, btnEliminar, btnBuscar;

    private RecyclerView recyclerClientes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cliente, container, false);

        controller = new ClienteController(requireContext());

        // Referencias UI
        etUid = v.findViewById(R.id.etUid);
        etNombre = v.findViewById(R.id.etNombre);
        etTelefono = v.findViewById(R.id.etTelefono);
        etFechaNacimiento = v.findViewById(R.id.etFechaNacimiento);
        etEstado = v.findViewById(R.id.etEstado);
        etFoto = v.findViewById(R.id.etFoto);
        etBuscar = v.findViewById(R.id.etBuscarCliente);

        btnCrear = v.findViewById(R.id.btnCrearCliente);
        btnModificar = v.findViewById(R.id.btnModificarCliente);
        btnEliminar = v.findViewById(R.id.btnEliminarCliente);
        btnBuscar = v.findViewById(R.id.btnBuscarCliente);

        recyclerClientes = v.findViewById(R.id.recyclerClientes);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarClientes();

        // Botón Crear
        btnCrear.setOnClickListener(view -> {
            Cliente nuevo = new Cliente(
                    etUid.getText().toString(),
                    etNombre.getText().toString(),
                    etTelefono.getText().toString(),
                    etFechaNacimiento.getText().toString(),
                    etEstado.getText().toString(),
                    etFoto.getText().toString()
            );
            controller.agregarCliente(nuevo);
            cargarClientes();
        });

        // Botón Modificar
        btnModificar.setOnClickListener(view -> {
            Cliente modificado = new Cliente(
                    etUid.getText().toString(),
                    etNombre.getText().toString(),
                    etTelefono.getText().toString(),
                    etFechaNacimiento.getText().toString(),
                    etEstado.getText().toString(),
                    etFoto.getText().toString()
            );
            controller.actualizarCliente(modificado);
            cargarClientes();
        });

        // Botón Eliminar
        btnEliminar.setOnClickListener(view -> {
            Cliente eliminar = controller.obtenerClientePorUid(etUid.getText().toString());
            if (eliminar != null) {
                controller.eliminarCliente(eliminar);
                cargarClientes();
            }
        });

        // Botón Buscar
        btnBuscar.setOnClickListener(view -> {
            String uid = etBuscar.getText().toString();
            Cliente encontrado = controller.obtenerClientePorUid(uid);
            if (encontrado != null) {
                adapter = new ClienteAdapter(List.of(encontrado), cliente -> {
                    // Acción al pulsar detalles
                });
                recyclerClientes.setAdapter(adapter);
            }
        });

        return v;
    }

    private void cargarClientes() {
        List<Cliente> lista = controller.obtenerClientes();
        adapter = new ClienteAdapter(lista, cliente -> {
            // Acción al pulsar detalles
        });
        recyclerClientes.setAdapter(adapter);
    }
}
