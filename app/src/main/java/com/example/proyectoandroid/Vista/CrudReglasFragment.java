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
import androidx.navigation.Navigation;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Adaptadores.ReglasAdapter;
import com.example.proyectoandroid.modelo.Regla;
import com.example.proyectoandroid.modelo.ReglaRepository;

import java.util.List;

public class CrudReglasFragment extends Fragment {

    private List<Regla> listaReglas;
    private ReglasAdapter adapter;
    private Regla reglaSeleccionada = null;

    private ReglaRepository repo;

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

        // OBJETO REPOSITORY (Firebase ↔ Room)
        repo = new ReglaRepository(requireContext());

        // Cargar desde Room (Firebase sincroniza solo)
        listaReglas = repo.obtenerReglasLocal();
        adapter = new ReglasAdapter(listaReglas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // ------------------- SELECCIÓN ITEM -------------------
        adapter.setOnItemClickListener(regla -> {
            reglaSeleccionada = regla;
            etNombre.setText(regla.getNombre());
            etDescripcion.setText(regla.getDescripcion());
            etOtros.setText(regla.getOtros());
        });

        // ------------------- GENERAR QR -------------------
        adapter.setOnQRClickListener(regla -> {
            String datosQR = "Beneficio: " + regla.getNombre() +
                    " | Descripción: " + regla.getDescripcion() +
                    " | Otros: " + regla.getOtros();

            GenerarQRFragment qrFragment = GenerarQRFragment.newInstance(datosQR);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, qrFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // ------------------- AGREGAR -------------------
        btnAgregar.setOnClickListener(v -> {

            if (etNombre.getText().toString().isEmpty()
                    || etDescripcion.getText().toString().isEmpty()
                    || etOtros.getText().toString().isEmpty())
                return;

            Regla nueva = new Regla(
                    etNombre.getText().toString(),
                    etDescripcion.getText().toString(),
                    etOtros.getText().toString()
            );

            repo.insertar(nueva);

            refrescarLista();

            limpiarCampos(etNombre, etDescripcion, etOtros);
        });

        // ------------------- MODIFICAR -------------------
        btnModificar.setOnClickListener(v -> {

            if (reglaSeleccionada == null) return;

            reglaSeleccionada.setNombre(etNombre.getText().toString());
            reglaSeleccionada.setDescripcion(etDescripcion.getText().toString());
            reglaSeleccionada.setOtros(etOtros.getText().toString());

            repo.actualizar(reglaSeleccionada);

            refrescarLista();
            limpiarCampos(etNombre, etDescripcion, etOtros);
        });

        // ------------------- ELIMINAR -------------------
        btnEliminar.setOnClickListener(v -> {

            if (reglaSeleccionada == null) return;

            repo.eliminar(reglaSeleccionada);

            refrescarLista();
            limpiarCampos(etNombre, etDescripcion, etOtros);
        });

        // ------------------- VOLVER -------------------
        btnVolver.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        return view;
    }


    // REFRESCAR LA LISTA DESDE ROOM (Firebase actualiza solo)
    private void refrescarLista() {
        listaReglas.clear();
        listaReglas.addAll(repo.obtenerReglasLocal());
        adapter.notifyDataSetChanged();
    }

    private void limpiarCampos(EditText n, EditText d, EditText o) {
        reglaSeleccionada = null;
        n.setText("Nombre");
        d.setText("Descripcion");
        o.setText("Otros");
    }
}
