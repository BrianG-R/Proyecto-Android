package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoandroid.R;

public class ProductosFragment extends Fragment {

    private Button btnCatalogo, btnGestion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        btnCatalogo = view.findViewById(R.id.btnCatalogo);
        btnGestion = view.findViewById(R.id.btnGestion);

        btnCatalogo.setOnClickListener(v -> {
            loadChildFragment(new CatalogoProductosFragment());
        });

        btnGestion.setOnClickListener(v -> {
            loadChildFragment(new GestionProductosFragment());
        });

        // Cargar catálogo por defecto
        loadChildFragment(new CatalogoProductosFragment());

        return view;
    }

    private void loadChildFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.productosFragmentContainer, fragment)
                .commit();
    }
}

