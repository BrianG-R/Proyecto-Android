package com.example.proyectoandroid.Vista;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

// Importaciones necesarias
import com.bumptech.glide.Glide; // Para cargar la foto en el QR
import com.example.proyectoandroid.Adaptadores.BeneficiosAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Beneficio;
import com.example.proyectoandroid.controller.BeneficioController;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class CrudBeneficiosFragment extends Fragment {

    private List<Beneficio> listaBeneficios;
    private BeneficiosAdapter adapter;
    private BeneficioController beneficioController;
    private Beneficio beneficioSeleccionado = null;

    // Vistas globales
    private EditText etNombre, etDescripcion, etUrlFoto, etBuscar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 1. Inflamos el diseño de la pantalla principal
        View view = inflater.inflate(R.layout.fragment_beneficios, container, false);

        // 2. Vinculamos con los IDs del XML
        etNombre = view.findViewById(R.id.editTextNombreBeneficio);
        etDescripcion = view.findViewById(R.id.editTextDescripcionBeneficio);
        etUrlFoto = view.findViewById(R.id.editTextUrlFotoBeneficio);
        etBuscar = view.findViewById(R.id.editTextBuscar);

        Button btnAgregar = view.findViewById(R.id.buttonAgregarBeneficio);
        Button btnModificar = view.findViewById(R.id.btnModificar);
        Button btnEliminar = view.findViewById(R.id.buttonEliminarBeneficio);
        Button btnVolver = view.findViewById(R.id.buttonVolver);
        Button btnBuscar = view.findViewById(R.id.btnBuscar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBeneficios);

        // 3. Base de Datos
        AppDataBase db = Room.databaseBuilder(getContext(), AppDataBase.class, "app_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        beneficioController = new BeneficioController(db);

        // 4. Configuración del Adaptador
        listaBeneficios = beneficioController.obtenerBeneficios();
        adapter = new BeneficiosAdapter(listaBeneficios, getContext());

        // ------------------------------------------------------------------
        // 🔥 LÓGICA DEL QR MEJORADA (CON FOTO REAL Y DIALOG)
        // ------------------------------------------------------------------
        adapter.setOnQRClickListener(beneficio -> {
            try {
                // A. Texto del QR (Sin tildes para evitar errores)
                String qrContent = "Beneficio: " + beneficio.getNombre() + "\n" +
                        "Descripcion: " + beneficio.getDescripcion() + "\n" +
                        "ID: " + beneficio.getId();

                // B. Inflar diseño de la ventana
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_generar_qr_beneficios, null);

                // C. Vincular elementos de la ventana QR
                ImageView ivQr = dialogView.findViewById(R.id.ivQrBeneficios);
                ImageView ivFoto = dialogView.findViewById(R.id.ivFotoRealBeneficio); // Foto del beneficio
                Button btnCerrar = dialogView.findViewById(R.id.btnCerrarQrBeneficios);

                // D. CARGAR FOTO REAL (Si existe URL)
                if (beneficio.getUrlFoto() != null && !beneficio.getUrlFoto().isEmpty()) {
                    Glide.with(getContext())
                            .load(beneficio.getUrlFoto())
                            .circleCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivFoto);
                } else {
                    // Si no hay foto, ponemos una por defecto
                    ivFoto.setImageResource(R.drawable.ic_launcher_background);
                }

                // E. Generar QR
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 600, 600);
                ivQr.setImageBitmap(bitmap);

                // F. Mostrar Ventana
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                btnCerrar.setOnClickListener(v -> dialog.dismiss());
                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al generar QR", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener selección de ítem
        adapter.setOnItemClickListener(beneficio -> {
            beneficioSeleccionado = beneficio;
            etNombre.setText(beneficio.getNombre());
            etDescripcion.setText(beneficio.getDescripcion());
            etUrlFoto.setText(beneficio.getUrlFoto());
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // ------------------------------------------------------------------
        // BOTONES CRUD
        // ------------------------------------------------------------------

        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String desc = etDescripcion.getText().toString();
            String url = etUrlFoto.getText().toString();
            if (!nombre.isEmpty() && !desc.isEmpty()) {
                beneficioController.agregarBeneficio(nombre, desc, url);
                Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarLista();
            } else {
                Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_SHORT).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (beneficioSeleccionado != null) {
                beneficioSeleccionado.setNombre(etNombre.getText().toString());
                beneficioSeleccionado.setDescripcion(etDescripcion.getText().toString());
                beneficioSeleccionado.setUrlFoto(etUrlFoto.getText().toString());
                beneficioController.actualizarBeneficio(beneficioSeleccionado);
                Toast.makeText(getContext(), "Modificado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (beneficioSeleccionado != null) {
                beneficioController.eliminarBeneficio(beneficioSeleccionado);
                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarLista();
            }
        });

        // BOTÓN VOLVER (Al menú principal)
        btnVolver.setOnClickListener(v -> {
            MenuAdminFragment menuPrincipal = new MenuAdminFragment();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, menuPrincipal)
                        .commit();
            }
        });

        // BOTÓN BUSCAR
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

    private void limpiarCampos() {
        beneficioSeleccionado = null;
        etNombre.setText("");
        etDescripcion.setText("");
        etUrlFoto.setText("");
    }
}