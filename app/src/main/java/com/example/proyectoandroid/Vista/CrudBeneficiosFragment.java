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

import com.example.proyectoandroid.Adaptadores.BeneficiosAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Beneficio;
import com.example.proyectoandroid.controller.BeneficioController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CrudBeneficiosFragment extends Fragment {

    private List<Beneficio> listaBeneficios;
    private BeneficiosAdapter adapter;
    private BeneficioController beneficioController;
    private Beneficio beneficioSeleccionado = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beneficios, container, false);

        EditText etNombre = view.findViewById(R.id.editTextNombreBeneficio);
        EditText etDescripcion = view.findViewById(R.id.editTextDescripcionBeneficio);
        EditText etBuscar = view.findViewById(R.id.editTextBuscar);

        Button btnAgregar = view.findViewById(R.id.buttonAgregarBeneficio);
        Button btnEliminar = view.findViewById(R.id.buttonEliminarBeneficio);
        Button btnVolver = view.findViewById(R.id.buttonVolver);
        Button btnBuscar = view.findViewById(R.id.btnBuscar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBeneficios);

        AppDataBase db = Room.databaseBuilder(
                        getContext(),
                        AppDataBase.class,
                        "app_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        beneficioController = new BeneficioController(db);

        listaBeneficios = beneficioController.obtenerBeneficios();
        adapter = new BeneficiosAdapter(listaBeneficios);

        // ------------------- LISTENER PARA GENERAR QR -------------------
        adapter.setOnQRClickListener(beneficio -> {
            String qrContent = "Beneficio: " + beneficio.getNombre() + "\n" +
                    "Descripción: " + beneficio.getDescripcion() + "\n" +
                    "ID: " + UUID.randomUUID();

            GenerarQRFragment qrFragment = GenerarQRFragment.newInstance(qrContent);

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
        adapter.setOnItemClickListener(beneficio -> {
            beneficioSeleccionado = beneficio;
            etNombre.setText(beneficio.getNombre());
            etDescripcion.setText(beneficio.getDescripcion());
        });

        // ------------------- BOTONES -------------------

        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();

            if (!nombre.isEmpty() && !descripcion.isEmpty()) {
                beneficioController.agregarBeneficio(nombre, descripcion);

                actualizarLista();
                beneficioSeleccionado = null;
                etNombre.setText("");
                etDescripcion.setText("");
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (beneficioSeleccionado != null) {
                beneficioController.eliminarBeneficio(beneficioSeleccionado);

                actualizarLista();
                beneficioSeleccionado = null;
                etNombre.setText("");
                etDescripcion.setText("");
            }
        });

        btnVolver.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnBuscar.setOnClickListener(v -> {
            String textoBuscar = etBuscar.getText().toString().trim().toLowerCase();

            if (textoBuscar.isEmpty()) {
                actualizarLista();
            } else {
                List<Beneficio> filtrados = beneficioController.obtenerBeneficios().stream()
                        .filter(b -> b.getNombre().toLowerCase().contains(textoBuscar))
                        .collect(Collectors.toList());

                listaBeneficios.clear();
                listaBeneficios.addAll(filtrados);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void actualizarLista() {
        listaBeneficios.clear();
        listaBeneficios.addAll(beneficioController.obtenerBeneficios());
        adapter.notifyDataSetChanged();
    }
}
