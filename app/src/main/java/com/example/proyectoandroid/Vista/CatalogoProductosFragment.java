package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.Adaptadores.ProductoAdapter;


public class CatalogoProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ArrayList<Producto> listaProductos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogo_productos, container, false);

        recyclerView = view.findViewById(R.id.recyclerCatalogo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("Café Americano", 1500, true));
        listaProductos.add(new Producto("Croissant", 1200, true));
        listaProductos.add(new Producto("Jugo Natural", 1800, false));

        adapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
