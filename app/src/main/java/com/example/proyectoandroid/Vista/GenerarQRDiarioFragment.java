package com.example.proyectoandroid.Vista;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoandroid.R;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GenerarQRDiarioFragment extends Fragment {

    private String generarHashDiario() {
        String fecha = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String clave = "CAFE-2025";

        String base = fecha + "-" + clave;
        return String.valueOf(base.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generar_q_r_diario, container, false);

        TextView tvFecha = view.findViewById(R.id.tvFechaQRDiario);
        TextView tvHash = view.findViewById(R.id.tvHashQRDiario);
        ImageView ivQr = view.findViewById(R.id.ivQrDiario);


        // Generar fecha y hash
        String fecha = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String hash = generarHashDiario();

        tvFecha.setText("Fecha: " + fecha);
        tvHash.setText("Hash: " + hash);

        String contenidoQR = "QRDIA|fecha=" + fecha + "|hash=" + hash;

        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(contenidoQR, BarcodeFormat.QR_CODE, 600, 600);
            ivQr.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
