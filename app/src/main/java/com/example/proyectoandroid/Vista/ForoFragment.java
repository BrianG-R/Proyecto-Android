package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.MensajeAdapter;
import com.example.proyectoandroid.modelo.Mensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ForoFragment extends Fragment {

    private RecyclerView rvMensajes;
    private EditText etMensaje;
    private Button btnEnviar;

    private LinearLayout layoutResponder;
    private TextView tvResponderTexto;
    private Button btnCancelarRespuesta;

    private String responderTexto = "";

    private MensajeAdapter adapter;
    private List<Mensaje> lista = new ArrayList<>();

    private String uid;
    private String nombreUsuario = "Usuario";
    private String fotoBase64 = "";

    private DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        rvMensajes = view.findViewById(R.id.rvMensajes);
        etMensaje = view.findViewById(R.id.etNuevoMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviarMensaje);

        layoutResponder = view.findViewById(R.id.layoutResponder);
        tvResponderTexto = view.findViewById(R.id.tvResponderTexto);
        btnCancelarRespuesta = view.findViewById(R.id.btnCancelarRespuesta);

        uid = FirebaseAuth.getInstance().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("foro");

        adapter = new MensajeAdapter(lista, uid, new MensajeAdapter.OnMensajeActions() {
            @Override
            public void onEliminar(Mensaje mensaje) {
                dbRef.child(mensaje.getId()).removeValue();
            }

            @Override
            public void onResponder(Mensaje mensaje) {
                responderTexto = mensaje.getContenido();
                layoutResponder.setVisibility(View.VISIBLE);
                tvResponderTexto.setText("Respondiendo a: " + responderTexto);
            }
        });

        rvMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMensajes.setAdapter(adapter);

        cargarDatosUsuario();
        cargarMensajes();

        btnEnviar.setOnClickListener(v -> enviarMensaje());
        btnCancelarRespuesta.setOnClickListener(v -> cancelarRespuesta());
    }

    private void cancelarRespuesta() {
        responderTexto = "";
        layoutResponder.setVisibility(View.GONE);
    }

    private void enviarMensaje() {
        String texto = etMensaje.getText().toString().trim();
        if (texto.isEmpty()) return;

        String id = dbRef.push().getKey();

        Mensaje m = new Mensaje(
                id,
                uid,
                nombreUsuario,
                texto,
                System.currentTimeMillis(),
                fotoBase64,
                responderTexto
        );

        dbRef.child(id).setValue(m);

        etMensaje.setText("");
        cancelarRespuesta();
    }

    private void cargarDatosUsuario() {
        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    nombreUsuario = snapshot.child("nombre").getValue(String.class);
                    if (nombreUsuario == null) nombreUsuario = "Usuario";

                    fotoBase64 = snapshot.child("foto").getValue(String.class);
                    if (fotoBase64 == null) fotoBase64 = "";
                });
    }

    private void cargarMensajes() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Mensaje m = ds.getValue(Mensaje.class);
                    lista.add(m);
                }
                adapter.notifyDataSetChanged();
                rvMensajes.scrollToPosition(lista.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
