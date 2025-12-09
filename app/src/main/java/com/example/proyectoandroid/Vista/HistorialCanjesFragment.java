package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.Adaptadores.HistorialCanjesAdapter;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Canje;
import com.example.proyectoandroid.modelo.CanjeRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HistorialCanjesFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistorialCanjesAdapter adapter;
    private CanjeRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_historial_canjes, container, false);

        recyclerView = view.findViewById(R.id.recyclerHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String uid = FirebaseAuth.getInstance().getUid();
        repo = new CanjeRepository(requireContext());

        adapter = new HistorialCanjesAdapter(new java.util.ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 🔥 OBSERVAR CAMBIOS EN TIEMPO REAL
        repo.observarHistorial().observe(getViewLifecycleOwner(), new Observer<List<Canje>>() {
            @Override
            public void onChanged(List<Canje> canjes) {
                adapter.actualizarLista(canjes);
            }
        });

        return view;
    }
}
