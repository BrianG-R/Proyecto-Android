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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.Adaptadores.BeneficiosAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Beneficio;
import com.example.proyectoandroid.modelo.BeneficioRepository;
import com.example.proyectoandroid.modelo.Canje;
import com.example.proyectoandroid.modelo.CanjeRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CrudBeneficiosFragment extends Fragment {

    private List<Beneficio> listaBeneficios;
    private BeneficiosAdapter adapter;
    private BeneficioRepository repo;
    private Beneficio beneficioSeleccionado = null;

    private EditText etNombre, etDescripcion, etUrlFoto, etBuscar, etCosto;
    private AppDataBase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beneficios, container, false);

        db = AppDataBase.getInstance(requireContext());

        etNombre = view.findViewById(R.id.editTextNombreBeneficio);
        etDescripcion = view.findViewById(R.id.editTextDescripcionBeneficio);
        etUrlFoto = view.findViewById(R.id.editTextUrlFotoBeneficio);
        etBuscar = view.findViewById(R.id.editTextBuscar);
        etCosto = view.findViewById(R.id.editTextCostoBeneficio);

        Button btnAgregar = view.findViewById(R.id.buttonAgregarBeneficio);
        Button btnModificar = view.findViewById(R.id.btnModificar);
        Button btnEliminar = view.findViewById(R.id.buttonEliminarBeneficio);
        Button btnVolver = view.findViewById(R.id.buttonVolver);
        Button btnBuscar = view.findViewById(R.id.btnBuscar);
        TextView tvPuntos = view.findViewById(R.id.tvPuntosDisponibles);

        // PUNTOS DISPONIBLES
        String uid = FirebaseAuth.getInstance().getUid();
        int visitas = db.visitaDao().totalVisitasPorUsuario(uid);
        Integer usados = db.canjeDao().totalPuntosCanjeados(uid);
        if (usados == null) usados = 0;

        int disponibles = visitas - usados;
        tvPuntos.setText("Puntos disponibles: " + disponibles);

        // LISTADO DE BENEFICIOS
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBeneficios);

        repo = new BeneficioRepository(requireContext());
        listaBeneficios = repo.obtenerLocal();
        adapter = new BeneficiosAdapter(listaBeneficios, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // QR DEL BENEFICIO
        adapter.setOnQRClickListener(beneficio -> {
            try {
                String qrContent = "Beneficio: " + beneficio.getNombre() + "\n" +
                        "Desc: " + beneficio.getDescripcion() + "\n" +
                        "ID: " + beneficio.getId();

                View dialogView = LayoutInflater.from(getContext())
                        .inflate(R.layout.fragment_generar_qr_beneficios, null);

                ImageView ivQr = dialogView.findViewById(R.id.ivQrBeneficios);
                ImageView ivFoto = dialogView.findViewById(R.id.ivFotoRealBeneficio);
                Button btnCerrar = dialogView.findViewById(R.id.btnCerrarQrBeneficios);

                if (beneficio.getUrlFoto() != null && !beneficio.getUrlFoto().isEmpty()) {
                    Glide.with(getContext())
                            .load(beneficio.getUrlFoto())
                            .circleCrop()
                            .into(ivFoto);
                }

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 600, 600);
                ivQr.setImageBitmap(bitmap);

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .create();

                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnCerrar.setOnClickListener(v -> dialog.dismiss());
                dialog.show();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error generando QR", Toast.LENGTH_SHORT).show();
            }
        });

        // CLICK ITEM
        adapter.setOnItemClickListener(beneficio -> {
            beneficioSeleccionado = beneficio;
            etNombre.setText(beneficio.getNombre());
            etDescripcion.setText(beneficio.getDescripcion());
            etUrlFoto.setText(beneficio.getUrlFoto());
            etCosto.setText(String.valueOf(beneficio.getCosto()));
        });

        // AGREGAR
        btnAgregar.setOnClickListener(v -> {
            if (etNombre.getText().toString().isEmpty() ||
                    etDescripcion.getText().toString().isEmpty() ||
                    etCosto.getText().toString().isEmpty()) {

                Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_SHORT).show();
                return;
            }

            Beneficio nuevo = new Beneficio(
                    etNombre.getText().toString(),
                    etDescripcion.getText().toString(),
                    etUrlFoto.getText().toString(),
                    Integer.parseInt(etCosto.getText().toString())
            );

            repo.insertar(nuevo);
            actualizarLista();
            limpiarCampos();
        });

        // MODIFICAR
        btnModificar.setOnClickListener(v -> {
            if (beneficioSeleccionado != null) {

                beneficioSeleccionado.setNombre(etNombre.getText().toString());
                beneficioSeleccionado.setDescripcion(etDescripcion.getText().toString());
                beneficioSeleccionado.setUrlFoto(etUrlFoto.getText().toString());
                beneficioSeleccionado.setCosto(Integer.parseInt(etCosto.getText().toString()));

                repo.actualizar(beneficioSeleccionado);

                actualizarLista();
                limpiarCampos();
            }
        });

        // ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (beneficioSeleccionado != null) {
                repo.eliminar(beneficioSeleccionado);
                actualizarLista();
                limpiarCampos();
            }
        });

        // BUSCAR
        btnBuscar.setOnClickListener(v -> {
            String buscar = etBuscar.getText().toString().trim().toLowerCase();

            if (buscar.isEmpty()) {
                actualizarLista();
            } else {
                listaBeneficios.clear();
                listaBeneficios.addAll(
                        repo.obtenerLocal().stream()
                                .filter(b -> b.getNombre().toLowerCase().contains(buscar))
                                .collect(Collectors.toList())
                );
                adapter.notifyDataSetChanged();
            }
        });

        // VOLVER
        btnVolver.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.menuAdminFragment)
        );

        // 🟣 CANJEAR BENEFICIO (FUNCIONAL + GUARDA)
        adapter.setOnCanjearClickListener(beneficio -> {

            String uid2 = FirebaseAuth.getInstance().getUid();

            int vCount = db.visitaDao().totalVisitasPorUsuario(uid2);
            Integer uCount = db.canjeDao().totalPuntosCanjeados(uid2);
            if (uCount == null) uCount = 0;

            int d2 = vCount - uCount;

            if (d2 < beneficio.getCosto()) {
                Toast.makeText(getContext(), "No tienes suficientes puntos", Toast.LENGTH_LONG).show();
                return;
            }

            Canje canje = new Canje(
                    beneficio.getId(),
                    uid2,
                    new Date()
            );

            // 🚀 GUARDAR EN FIREBASE + ROOM
            new CanjeRepository(requireContext()).insertarCanje(canje);

            Toast.makeText(getContext(), "Canje realizado ✔", Toast.LENGTH_LONG).show();

            Navigation.findNavController(requireView())
                    .navigate(R.id.historialCanjesFragment);
        });

        return view;
    }

    private void actualizarLista() {
        listaBeneficios.clear();
        listaBeneficios.addAll(repo.obtenerLocal());
        adapter.notifyDataSetChanged();
    }

    private void limpiarCampos() {
        beneficioSeleccionado = null;
        etNombre.setText("");
        etDescripcion.setText("");
        etUrlFoto.setText("");
        etCosto.setText("");
    }
}
