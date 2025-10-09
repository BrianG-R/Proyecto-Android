package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;




import com.example.proyectoandroid.R;

public class MenuAdmin extends AppCompatActivity {
    private LinearLayout menuContainer;
    private View fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            // Si no hay fragments en el backstack → mostramos el menú
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                showMenu();
            }
        });

        // Inicializar referencias
        menuContainer = findViewById(R.id.linearLayoutMenu);
        fragmentContainer = findViewById(R.id.fragmentContainer);


        // Botones del menú
        Button btnClientes =findViewById(R.id.btnClientes);
        Button btnReglas = findViewById(R.id.btnReglas);
        Button btnTiendas = findViewById(R.id.btnTiendas);
        Button btnProductos = findViewById(R.id.btnProductos);
        Button btnBeneficios = findViewById(R.id.btnBeneficios);
        Button btnCerrar = findViewById(R.id.btnCerrar);
        Button btnEscanearQR = findViewById(R.id.btnEscanearQR);

        // Botón Reglas → mostrar fragment a pantalla completa
        btnReglas.setOnClickListener(v -> {
            loadFragment(new CrudReglasFragment());
        });

        btnClientes.setOnClickListener(v -> {
        });


        // Aquí se pueden agregar los otros botones cuando tengas los fragments
        btnTiendas.setOnClickListener(v -> {
            loadFragment(new CrudTiendasFragment());
        });

        btnProductos.setOnClickListener(v -> {
            // loadFragment(new ProductosFragment());
        });

        // Botón Beneficios → mostrar fragment a pantalla completa
        btnBeneficios.setOnClickListener(v -> {
            loadFragment(new CrudBeneficiosFragment());
        });

        btnEscanearQR.setOnClickListener(v -> {
            loadFragment(new ScanQRFragment());
        });

        btnCerrar.setOnClickListener(v -> finish());
    }

    // Cargar fragment en el contenedor
    private void loadFragment(Fragment fragment) {
        menuContainer.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Mostrar nuevamente el menú y ocultar fragment
    public void showMenu() {
        menuContainer.setVisibility(View.VISIBLE);
        fragmentContainer.setVisibility(View.GONE);
    }
}
