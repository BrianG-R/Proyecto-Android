package com.example.proyectoandroid.Vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText txtNombre = view.findViewById(R.id.txtNombre);
        EditText txtEmail = view.findViewById(R.id.txtEmail);
        EditText txtPass = view.findViewById(R.id.txtPass);
        Button btnRegistrar = view.findViewById(R.id.btnRegistrar);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("usuarios");

        btnRegistrar.setOnClickListener(v -> {

            String nombre = txtNombre.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(auth -> {

                        String userID = mAuth.getCurrentUser().getUid();

                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", nombre);
                        map.put("email", email);
                        map.put("rol", "usuario");

                        dbRef.child(userID).setValue(map);

                        Toast.makeText(getContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}

