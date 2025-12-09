package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.database.AppDataBase;
import com.example.proyectoandroid.modelo.Visita;
import com.example.proyectoandroid.modelo.VisitaRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.google.zxing.ResultPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanQRFragment extends Fragment {

    private CompoundBarcodeView barcodeView;
    private boolean qrProcesado = false;
    private AppDataBase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan_qr, container, false);

        barcodeView = view.findViewById(R.id.barcode_scanner);
        db = AppDataBase.getInstance(requireContext());

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                if (qrProcesado) return;
                if (result == null || result.getText() == null) return;

                qrProcesado = true;

                Log.d("SCANQR", "QR leído: " + result.getText());

                procesarVisita(result.getText(), view);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) { }
        });

        return view;
    }


    private void procesarVisita(String qrValue, View rootView) {

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            Toast.makeText(getContext(), "Debe iniciar sesión", Toast.LENGTH_LONG).show();
            return;
        }

        String fechaQR = "";
        String hashQR = "";

        try {
            String[] partes = qrValue.split("\\|");

            fechaQR = partes[1].split("=")[1];
            hashQR = partes[2].split("=")[1];

        } catch (Exception e) {
            Toast.makeText(getContext(), "QR inválido", Toast.LENGTH_LONG).show();
            return;
        }

        // VALIDAR QR
        if (!validarQR(hashQR)) {
            Toast.makeText(getContext(), "QR inválido o expirado", Toast.LENGTH_LONG).show();
            return;
        }

        int dia = getDiaActual();

        // Verificar si ya escaneó hoy
        Visita hoy = db.visitaDao().obtenerVisitaDeHoy(uid, dia);
        if (hoy != null) {
            Toast.makeText(getContext(), "Ya registraste una visita hoy", Toast.LENGTH_LONG).show();
            return;
        }

        // Crear visita
        Visita nueva = new Visita();
        nueva.uid = uid;
        nueva.id_tienda = 1;
        nueva.fecha_hora = new Date();
        nueva.diaScan = dia;
        nueva.estado_sync = "pendiente";
        nueva.hash_qr = hashQR;

        Log.d("SCANQR", "Insertando visita: UID=" + uid + " día=" + dia);

        // Guardar visita local
        db.visitaDao().insertarVisita(nueva);

        // Guardar visita en Firebase
        new VisitaRepository(requireContext()).insertarVisita(nueva);

        // Calcular puntos → 100 puntos por visita (temporal)
        int puntos = db.visitaDao().totalVisitasPorUsuario(uid);

        // Guardar puntos en Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .child("puntos");

        ref.setValue(puntos).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("SCANQR", "Puntos actualizados en Firebase: " + puntos);
            } else {
                Log.e("SCANQR", "Error al actualizar puntos", task.getException());
            }
        });

        Toast.makeText(getContext(),
                "¡Visita registrada! Total puntos: " + puntos,
                Toast.LENGTH_LONG).show();

        // 👉 Enviar al historial de canjes
        NavController nav = Navigation.findNavController(rootView);
        nav.navigate(R.id.crudBeneficiosFragment);

    }


    private int getDiaActual() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return y * 10000 + m * 100 + d;
    }


    private boolean validarQR(String hashEscaneado) {

        String fechaHoy = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String clave = "CAFE-2025";

        String base = fechaHoy + "-" + clave;
        String hashCorrecto = String.valueOf(base.hashCode());

        Log.d("SCANQR", "HASH ESCANEADO=" + hashEscaneado + " HASH GENERADO=" + hashCorrecto);

        return hashCorrecto.equals(hashEscaneado);
    }


    @Override
    public void onResume() {
        super.onResume();
        qrProcesado = false;
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
