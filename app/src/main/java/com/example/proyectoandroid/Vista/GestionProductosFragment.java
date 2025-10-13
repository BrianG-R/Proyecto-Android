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

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.Adaptadores.ProductoAdapter;

import java.util.ArrayList;

public class GestionProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ArrayList<Producto> listaProductos;
    private Button btnCrear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_productos, container, false);

        recyclerView = view.findViewById(R.id.recyclerGestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("Té Verde", 1300, true));
        listaProductos.add(new Producto("Sándwich Vegano", 2500, false));

        adapter = new ProductoAdapter(listaProductos, true); // modo admin
        recyclerView.setAdapter(adapter);

        btnCrear = view.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nuevo Producto");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 30, 40, 10);

        EditText inputNombre = new EditText(requireContext());
        inputNombre.setHint("Nombre");
        layout.addView(inputNombre);

        EditText inputPrecio = new EditText(requireContext());
        inputPrecio.setHint("Precio");
        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputPrecio);

        CheckBox checkDisponible = new CheckBox(requireContext());
        checkDisponible.setText("Disponible");
        layout.addView(checkDisponible);

        builder.setView(layout);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nombre = inputNombre.getText().toString().trim();
            String precioStr = inputPrecio.getText().toString().trim();
            boolean disponible = checkDisponible.isChecked();

            if (!nombre.isEmpty() && !precioStr.isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    listaProductos.add(new Producto(nombre, precio, disponible));
                    adapter.notifyItemInserted(listaProductos.size() - 1);
                    Toast.makeText(requireContext(), "Producto agregado", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
