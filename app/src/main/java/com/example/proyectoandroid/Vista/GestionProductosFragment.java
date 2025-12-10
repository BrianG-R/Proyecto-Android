package com.example.proyectoandroid.Vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.Adaptadores.ProductoAdapter;
import com.example.proyectoandroid.controller.ProductoController;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class GestionProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ProductoController productoController;
    private List<Producto> listaProductos;

    private EditText etNombre, etPrecio, etStock, etBuscar;
    private CheckBox checkDisponible;
    private ImageView ivFoto;
    private Button btnSeleccionarFoto;

    private Button btnCrear, btnModificar, btnEliminar, btnBuscar, btnVolver;

    private Producto productoSeleccionado = null;
    private String imagenBase64 = "";

    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gestion_productos, container, false);

        // --- CAMBIO IMPORTANTE: Inicialización con Contexto para activar Sync ---
        productoController = new ProductoController(requireContext());

        recyclerView = view.findViewById(R.id.recyclerGestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProductos = productoController.obtenerProductos();
        adapter = new ProductoAdapter(listaProductos, true);
        recyclerView.setAdapter(adapter);

        etNombre = view.findViewById(R.id.etNombreProducto);
        etPrecio = view.findViewById(R.id.etPrecioProducto);
        etStock = view.findViewById(R.id.etStockProducto);
        checkDisponible = view.findViewById(R.id.etEstadoProducto);
        etBuscar = view.findViewById(R.id.etBuscar);

        ivFoto = view.findViewById(R.id.ivFotoProducto);
        btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);

        btnCrear = view.findViewById(R.id.btnCrear);
        btnModificar = view.findViewById(R.id.btnModificar);
        btnEliminar = view.findViewById(R.id.btnEliminar);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        btnVolver = view.findViewById(R.id.btnVolver);

        btnSeleccionarFoto.setOnClickListener(v -> abrirGaleria());

        adapter.setOnItemClickListener(producto -> {
            productoSeleccionado = producto;

            etNombre.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etStock.setText(String.valueOf(producto.getStock()));
            checkDisponible.setChecked(producto.isDisponible());

            if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                imagenBase64 = producto.getImagen();
                try {
                    byte[] decodedString = Base64.decode(imagenBase64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivFoto.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } else {
                imagenBase64 = "";
                ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            Toast.makeText(getContext(), "Seleccionado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        });

        btnCrear.setOnClickListener(v -> crearProducto());
        btnModificar.setOnClickListener(v -> modificarProducto());
        btnEliminar.setOnClickListener(v -> eliminarProducto());
        btnBuscar.setOnClickListener(v -> buscarProducto());

        btnVolver.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        return view;
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                ivFoto.setImageBitmap(resizedBitmap);
                imagenBase64 = convertirBase64(resizedBitmap);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertirBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void crearProducto() {
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        boolean disponible = checkDisponible.isChecked();

        if (!nombre.isEmpty() && !precioStr.isEmpty() && !stockStr.isEmpty()) {
            try {
                double precio = Double.parseDouble(precioStr);
                int stock = Integer.parseInt(stockStr);

                productoController.agregarProducto(nombre, precio, disponible, stock, imagenBase64);

                refrescarLista();
                limpiarCampos();
                Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
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
        String stockStr = etStock.getText().toString().trim();
        boolean disponible = checkDisponible.isChecked();

        if (!nombre.isEmpty() && !precioStr.isEmpty() && !stockStr.isEmpty()) {
            try {
                double precio = Double.parseDouble(precioStr);
                int stock = Integer.parseInt(stockStr);

                productoSeleccionado.setNombre(nombre);
                productoSeleccionado.setPrecio(precio);
                productoSeleccionado.setDisponible(disponible);
                productoSeleccionado.setStock(stock);
                productoSeleccionado.setImagen(imagenBase64);

                productoController.actualizarProducto(productoSeleccionado);
                refrescarLista();
                limpiarCampos();

                Toast.makeText(requireContext(), "Producto modificado", Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
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
        etStock.setText("");
        checkDisponible.setChecked(false);
        etBuscar.setText("");
        imagenBase64 = "";
        ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
        productoSeleccionado = null;
    }
}