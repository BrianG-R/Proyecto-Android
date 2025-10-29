package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;
import com.example.proyectoandroid.Adaptadores.ProductoAdapter;
import com.example.proyectoandroid.controller.ProductoController;
import com.example.proyectoandroid.database.AppDataBase;

import androidx.room.Room;
import java.util.List;

public class CatalogoProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ProductoController productoController;
    private List<Producto> listaProductos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogo_productos, container, false);

        AppDataBase db = Room.databaseBuilder(
                requireContext(),
                AppDataBase.class,
                "cafeteria-db"
        ).allowMainThreadQueries().build();

        productoController = new ProductoController(db);

        recyclerView = view.findViewById(R.id.recyclerCatalogo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProductos = productoController.obtenerProductos();
        adapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
