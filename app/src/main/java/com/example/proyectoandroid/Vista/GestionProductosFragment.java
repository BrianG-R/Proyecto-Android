package com.example.proyectoandroid.Vista;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.Adaptadores.ProductoAdapter;
import com.example.proyectoandroid.controller.ProductoController;
import com.example.proyectoandroid.database.AppDataBase;

import java.util.List;

public class GestionProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ProductoController productoController;
    private List<Producto> listaProductos;

    private EditText etNombre, etPrecio;
    private CheckBox checkDisponible;
    private Button btnCrear, btnModificar, btnEliminar, btnBuscar;
    private EditText etBuscar;

    private Producto productoSeleccionado = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_productos, container, false);

        AppDataBase db = Room.databaseBuilder(
                requireContext(),
                AppDataBase.class,
                "cafeteria-db"
        ).allowMainThreadQueries().build();

        productoController = new ProductoController(db);

        recyclerView = view.findViewById(R.id.recyclerGestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProductos = productoController.obtenerProductos();
        adapter = new ProductoAdapter(listaProductos, true);
        recyclerView.setAdapter(adapter);

        etNombre = view.findViewById(R.id.etNombreProducto);
        etPrecio = view.findViewById(R.id.etPrecioProducto);
        checkDisponible = view.findViewById(R.id.etEstadoProducto); // usa CheckBox en layout
        etBuscar = view.findViewById(R.id.etBuscar);

        btnCrear = view.findViewById(R.id.btnCrear);
        btnModificar = view.findViewById(R.id.btnModificar);
        btnEliminar = view.findViewById(R.id.btnEliminar);
        btnBuscar = view.findViewById(R.id.btnBuscar);

        btnCrear.setOnClickListener(v -> crearProducto());
        btnModificar.setOnClickListener(v -> modificarProducto());
        btnEliminar.setOnClickListener(v -> eliminarProducto());
        btnBuscar.setOnClickListener(v -> buscarProducto());

        adapter.setOnItemLongClickListener(producto -> {
            productoSeleccionado = producto;
            etNombre.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            checkDisponible.setChecked(producto.isDisponible());
            Toast.makeText(getContext(), "Producto seleccionado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void crearProducto() {
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        boolean disponible = checkDisponible.isChecked();

        if (!nombre.isEmpty() && !precioStr.isEmpty()) {
            try {
                double precio = Double.parseDouble(precioStr);
                productoController.agregarProducto(nombre, precio, disponible);
                refrescarLista();
                limpiarCampos();
                Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void modificarProducto() {
        if (productoSeleccionado == null) {
            Toast.makeText(requireContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        boolean disponible = checkDisponible.isChecked();

        if (!nombre.isEmpty() && !precioStr.isEmpty()) {
            try {
                double precio = Double.parseDouble(precioStr);
                productoSeleccionado.setNombre(nombre);
                productoSeleccionado.setPrecio(precio);
                productoSeleccionado.setDisponible(disponible);
                productoController.actualizarProducto(productoSeleccionado);
                refrescarLista();
                limpiarCampos();
                Toast.makeText(requireContext(), "Producto modificado", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarProducto() {
        if (productoSeleccionado == null) {
            Toast.makeText(requireContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro de eliminar " + productoSeleccionado.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    productoController.eliminarProducto(productoSeleccionado);
                    refrescarLista();
                    limpiarCampos();
                    Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void buscarProducto() {
        String nombreBuscado = etBuscar.getText().toString().trim();
        if (nombreBuscado.isEmpty()) {
            refrescarLista();
        } else {
            List<Producto> resultados = productoController.buscarPorNombre(nombreBuscado);
            adapter.actualizarLista(resultados);
        }
    }

    private void refrescarLista() {
        listaProductos = productoController.obtenerProductos();
        adapter.actualizarLista(listaProductos);
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etPrecio.setText("");
        checkDisponible.setChecked(false);
        productoSeleccionado = null;
    }
}
