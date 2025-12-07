package com.example.proyectoandroid.Vista;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PerfilFragment extends Fragment {

    private ImageView imgPerfil;
    private EditText etNombre, etTelefono, etFecha, etEstado;
    private Button btnGuardar, btnCerrar;

    private String uid;
    private String fotoBase64 = "";

    private static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imgPerfil = view.findViewById(R.id.imgPerfil);
        etNombre = view.findViewById(R.id.etNombre);
        etTelefono = view.findViewById(R.id.etTelefono);
        etFecha = view.findViewById(R.id.etFechaNac);
        etEstado = view.findViewById(R.id.etEstado);

        btnGuardar = view.findViewById(R.id.btnGuardarPerfil);
        btnCerrar = view.findViewById(R.id.btnCerrarSesion);

        uid = FirebaseAuth.getInstance().getUid();

        cargarPerfil();

        imgPerfil.setOnClickListener(v -> abrirGaleria());
        btnGuardar.setOnClickListener(v -> guardarPerfil());
        btnCerrar.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());
    }

    // ------------------------------------------------
    // 🔥 CARGAR PERFIL DESDE FIREBASE
    // ------------------------------------------------
    private void cargarPerfil() {
        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String telefono = snapshot.child("telefono").getValue(String.class);
                        String fecha = snapshot.child("fecha_nac").getValue(String.class);
                        String estado = snapshot.child("estado").getValue(String.class);
                        fotoBase64 = snapshot.child("fotoPerfil").getValue(String.class);

                        etNombre.setText(nombre);
                        etTelefono.setText(telefono);
                        etFecha.setText(fecha);
                        etEstado.setText(estado);

                        // FOTO PERFIL
                        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                            byte[] bytes = Base64.decode(fotoBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgPerfil.setImageBitmap(hacerCircular(bitmap));
                        }
                    }
                });
    }

    // ------------------------------------------------
    // 🔥 ABRIR GALERÍA
    // ------------------------------------------------
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    // ------------------------------------------------
    // 🔥 RECIBIR IMAGEN ELEGIDA
    // ------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream stream = requireActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);

                Bitmap circular = hacerCircular(bitmap);

                imgPerfil.setImageBitmap(circular);
                fotoBase64 = convertirBase64(circular);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------------------------------------
    // 🔥 HACER IMAGEN CIRCULAR
    // ------------------------------------------------
    private Bitmap hacerCircular(Bitmap bitmap) {

        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        float radius = size / 2f;

        canvas.drawCircle(radius, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, size, size);
        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;
    }

    // ------------------------------------------------
    // 🔥 CONVERTIR IMAGEN A BASE64
    // ------------------------------------------------
    private String convertirBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    // ------------------------------------------------
    // 🔥 GUARDAR PERFIL EN FIREBASE
    // ------------------------------------------------
    private void guardarPerfil() {

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("nombre")
                .setValue(etNombre.getText().toString());

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("telefono")
                .setValue(etTelefono.getText().toString());

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("fecha_nac")
                .setValue(etFecha.getText().toString());

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("estado")
                .setValue(etEstado.getText().toString());

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("fotoPerfil")
                .setValue(fotoBase64);

        Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
    }

}

