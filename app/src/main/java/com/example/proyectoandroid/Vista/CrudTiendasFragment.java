package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.Adaptadores.TiendasAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Tienda;

import java.util.List;

public class CrudTiendasFragment extends Fragment {

    private EditText etNombre, etDireccion, etHorario, etEstado, etBuscar;
    private Button btnCrear, btnModificar, btnEliminar, btnVolver, btnSeleccionarUbicacion;
    private RecyclerView recyclerTiendas;

    private float latSeleccion = 0f;
    private float lonSeleccion = 0f;

    private AppDataBase db;
    private List<Tienda> lista;
    private TiendasAdapter adapter;

    private Tienda tiendaSeleccionada = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crud_tiendas, container, false);

        etNombre = view.findViewById(R.id.etNombre);
        etDireccion = view.findViewById(R.id.etDireccion);
        etHorario = view.findViewById(R.id.etHorario);
        etEstado = view.findViewById(R.id.etEstado);
        etBuscar = view.findViewById(R.id.etBuscar);

        btnCrear = view.findViewById(R.id.btnCrear);
        btnModificar = view.findViewById(R.id.btnModificar);
        btnEliminar = view.findViewById(R.id.btnEliminar);
        btnVolver = view.findViewById(R.id.btnVolver);
        btnSeleccionarUbicacion = view.findViewById(R.id.btnSeleccionarUbicacion);

        recyclerTiendas = view.findViewById(R.id.recyclerTiendas);

        db = AppDataBase.getInstance(requireContext());

        cargarLista();

        // Seleccionar ubicación
        btnSeleccionarUbicacion.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putFloat("lat", latSeleccion);
            args.putFloat("lon", lonSeleccion);
            args.putBoolean("modoSeleccion", true);

            Navigation.findNavController(v).navigate(R.id.mapsFragment, args);
        });

        btnCrear.setOnClickListener(v -> crearTienda());
        btnModificar.setOnClickListener(v -> modificarTienda());
        btnEliminar.setOnClickListener(v -> eliminarTienda());

        etBuscar.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                filtrarTiendas(etBuscar.getText().toString());
            }
            return false;
        });
        getParentFragmentManager().setFragmentResultListener(
                "ubicacionSeleccionada",
                this,
                (key, bundle) -> {
                    latSeleccion = bundle.getFloat("lat");
                    lonSeleccion = bundle.getFloat("lon");

                    Toast.makeText(getContext(),
                            "Ubicación seleccionada:\nLat: " + latSeleccion + "\nLon: " + lonSeleccion,
                            Toast.LENGTH_SHORT).show();
                }
        );

        btnVolver.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        return view;
    }

    private void cargarLista() {
        lista = db.tiendaDao().obtenerTiendas();
        adapter = new TiendasAdapter(lista);

        recyclerTiendas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerTiendas.setAdapter(adapter);

        adapter.setOnItemClickListener(tienda -> {
            tiendaSeleccionada = tienda;

            etNombre.setText(tienda.getNombre());
            etDireccion.setText(tienda.getDireccion());
            etHorario.setText(tienda.getHorario());
            etEstado.setText(tienda.getEstado());

            latSeleccion = (float) tienda.getLat();
            lonSeleccion = (float) tienda.getLon();
        });

        adapter.setOnMapClickListener(tienda -> {
            float lat = (float) tienda.getLat();
            float lon = (float) tienda.getLon();

            if (lat == 0f && lon == 0f) {
                Toast.makeText(getContext(), "Esta tienda no tiene ubicación guardada", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle args = new Bundle();
            args.putFloat("lat", lat);
            args.putFloat("lon", lon);
            args.putBoolean("modoSeleccion", false);

            Navigation.findNavController(requireView())
                    .navigate(R.id.mapsFragment, args);
        });
    }

    private void crearTienda() {
        String nombre = etNombre.getText().toString();
        String direccion = etDireccion.getText().toString();
        String horario = etHorario.getText().toString();
        String estado = etEstado.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Tienda nueva = new Tienda(
                nombre, direccion, horario, estado,
                (double) latSeleccion, (double) lonSeleccion
        );

        db.tiendaDao().insertar(nueva);

        limpiarCampos();
        cargarLista();
        Toast.makeText(getContext(), "Tienda creada", Toast.LENGTH_SHORT).show();
    }

    private void modificarTienda() {
        if (tiendaSeleccionada == null) {
            Toast.makeText(getContext(), "Seleccione una tienda", Toast.LENGTH_SHORT).show();
            return;
        }

        tiendaSeleccionada.setNombre(etNombre.getText().toString());
        tiendaSeleccionada.setDireccion(etDireccion.getText().toString());
        tiendaSeleccionada.setHorario(etHorario.getText().toString());
        tiendaSeleccionada.setEstado(etEstado.getText().toString());
        tiendaSeleccionada.setLat((double) latSeleccion);
        tiendaSeleccionada.setLon((double) lonSeleccion);

        db.tiendaDao().actualizar(tiendaSeleccionada);

        limpiarCampos();
        cargarLista();
        Toast.makeText(getContext(), "Modificado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void eliminarTienda() {
        if (tiendaSeleccionada == null) {
            Toast.makeText(getContext(), "Seleccione una tienda", Toast.LENGTH_SHORT).show();
            return;
        }

        db.tiendaDao().eliminar(tiendaSeleccionada);

        limpiarCampos();
        cargarLista();
        Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
    }

    private void filtrarTiendas(String query) {
        lista.clear();
        lista.addAll(db.tiendaDao().buscarTiendas(query));
        adapter.notifyDataSetChanged();
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etDireccion.setText("");
        etHorario.setText("");
        etEstado.setText("");
        etBuscar.setText("");
        latSeleccion = 0f;
        lonSeleccion = 0f;
        tiendaSeleccionada = null;
    }
}

