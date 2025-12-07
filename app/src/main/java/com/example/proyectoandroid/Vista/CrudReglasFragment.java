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
import androidx.room.Room;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Regla;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.controller.ReglaController;
import com.example.proyectoandroid.Adaptadores.ReglasAdapter;
import androidx.navigation.Navigation;

import java.util.List;

public class CrudReglasFragment extends Fragment {
    private List<Regla> listaReglas;
    private ReglasAdapter adapter;
    private ReglaController reglaController;
    private Regla reglaSeleccionada = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crud_reglas, container, false);

        EditText etNombre = view.findViewById(R.id.editTextText4);
        EditText etDescripcion = view.findViewById(R.id.editTextText3);
        EditText etOtros = view.findViewById(R.id.editTextText5);

        Button btnAgregar = view.findViewById(R.id.btnAgregar);
        Button btnEliminar = view.findViewById(R.id.btnEliminar);
        Button btnModificar = view.findViewById(R.id.btnModificar);
        Button btnVolver = view.findViewById(R.id.btnVolver);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerReglas);

        // Limpiar campos al enfocar
        View.OnFocusChangeListener clearOnFocus = (v, hasFocus) -> {
            if (hasFocus) ((EditText) v).setText("");
        };
        etNombre.setOnFocusChangeListener(clearOnFocus);
        etDescripcion.setOnFocusChangeListener(clearOnFocus);
        etOtros.setOnFocusChangeListener(clearOnFocus);

        // Inicializar base de datos y controlador
        AppDataBase db = Room.databaseBuilder(
                        getContext(),
                        AppDataBase.class,
                        "app_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        reglaController = new ReglaController(db);

        // Cargar datos desde Room
        listaReglas = reglaController.obtenerReglas();
        adapter = new ReglasAdapter(listaReglas);

        // ------------------- LISTENER PARA GENERAR QR -------------------
        adapter.setOnQRClickListener(regla -> {
            String datosQR = "Beneficio: " + regla.getNombre() +
                    " | Descripción: " + regla.getDescripcion() +
                    " | Otros: " + regla.getOtros();

            GenerarQRFragment qrFragment = GenerarQRFragment.newInstance(datosQR);

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, qrFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // ------------------- SELECCIÓN DEL ITEM -------------------
        adapter.setOnItemClickListener(regla -> {
            reglaSeleccionada = regla;
            etNombre.setText(regla.getNombre());
            etDescripcion.setText(regla.getDescripcion());
            etOtros.setText(regla.getOtros());
        });

        // ------------------- BOTONES -------------------

        // Agregar regla
        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String otros = etOtros.getText().toString();

            if (!nombre.isEmpty() && !descripcion.isEmpty() && !otros.isEmpty()) {
                reglaController.agregarRegla(nombre, descripcion, otros);

                listaReglas.clear();
                listaReglas.addAll(reglaController.obtenerReglas());
                adapter.notifyDataSetChanged();

                reglaSeleccionada = null;
                etNombre.setText("Nombre");
                etDescripcion.setText("Descripcion");
                etOtros.setText("Otros");
            }
        });

        // Eliminar regla seleccionada
        btnEliminar.setOnClickListener(v -> {
            if (reglaSeleccionada != null) {
                reglaController.eliminarRegla(reglaSeleccionada);

                listaReglas.clear();
                listaReglas.addAll(reglaController.obtenerReglas());
                adapter.notifyDataSetChanged();

                reglaSeleccionada = null;
                etNombre.setText("Nombre");
                etDescripcion.setText("Descripcion");
                etOtros.setText("Otros");
            }
        });

        // Modificar regla seleccionada
        btnModificar.setOnClickListener(v -> {
            if (reglaSeleccionada != null) {
                reglaSeleccionada.setNombre(etNombre.getText().toString());
                reglaSeleccionada.setDescripcion(etDescripcion.getText().toString());
                reglaSeleccionada.setOtros(etOtros.getText().toString());

                reglaController.actualizarRegla(reglaSeleccionada);

                listaReglas.clear();
                listaReglas.addAll(reglaController.obtenerReglas());
                adapter.notifyDataSetChanged();

                reglaSeleccionada = null;
                etNombre.setText("Nombre");
                etDescripcion.setText("Descripcion");
                etOtros.setText("Otros");
            }
        });

        // Volver al menú
        btnVolver.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );


        return view;
    }

}