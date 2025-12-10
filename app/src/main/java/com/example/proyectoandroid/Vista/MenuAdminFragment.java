package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.proyectoandroid.R;
import com.google.firebase.auth.FirebaseAuth;

public class MenuAdminFragment extends Fragment {

    private LinearLayout menuContainer;

    public MenuAdminFragment() {
        super(R.layout.fragment_menu_admin);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuContainer = view.findViewById(R.id.linearLayoutMenu);

        Button btnClientes = view.findViewById(R.id.btnClientes);
        Button btnReglas = view.findViewById(R.id.btnReglas);
        Button btnTiendas = view.findViewById(R.id.btnTiendas);
        Button btnProductos = view.findViewById(R.id.btnProductos);
        Button btnBeneficios = view.findViewById(R.id.btnBeneficios);
        Button btnEscanearQR = view.findViewById(R.id.btnEscanearQR);
        Button btnCerrar = view.findViewById(R.id.btnCerrar);
        Button btnPerfil = view.findViewById(R.id.btnPerfil);
        Button btnForo = view.findViewById(R.id.btnForo);



        btnReglas.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_crudReglasFragment)
        );

        btnTiendas.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_crudTiendasFragment)
        );

        btnBeneficios.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_crudBeneficiosFragment)
        );

        btnEscanearQR.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_scanQRFragment)
        );

        btnCerrar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_loginFragment);
        });

        btnPerfil.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.perfilFragment)
        );


        btnForo.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_menuAdminFragment_to_foroFragment)
        );
        btnProductos.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_menuAdminFragment_to_productosFragment)
        );

        btnClientes.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_menuAdminFragment_to_clienteFragment)
        );

        Button btnGenerarQRDiario = view.findViewById(R.id.btnGenerarQRDiario);

        btnGenerarQRDiario.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.generarQRDiarioFragment)
        );





    }
}
