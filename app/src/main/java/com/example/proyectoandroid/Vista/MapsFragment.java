package com.example.proyectoandroid.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoandroid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Coordenada por defecto (Ovalle)
    private LatLng initialLocation = new LatLng(-30.6, -71.2);
    private LatLng seleccion;

    private boolean modoSeleccion = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            float lat = getArguments().getFloat("lat", 0f);
            float lon = getArguments().getFloat("lon", 0f);
            modoSeleccion = getArguments().getBoolean("modoSeleccion", true);

            // Si lat/lon = 0, significa sin ubicación → usamos coordenada por defecto
            if (lat == 0f && lon == 0f) {
                initialLocation = new LatLng(-30.6, -71.2); // Ovalle
            } else {
                initialLocation = new LatLng(lat, lon);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) mapFragment.getMapAsync(this);

        Button btnConfirmar = view.findViewById(R.id.btnConfirmarUbicacion);
        btnConfirmar.setVisibility(modoSeleccion ? View.VISIBLE : View.GONE);

        btnConfirmar.setOnClickListener(v -> {
            if (seleccion != null) {
                Bundle result = new Bundle();
                result.putFloat("lat", (float) seleccion.latitude);
                result.putFloat("lon", (float) seleccion.longitude);
                getParentFragmentManager().setFragmentResult("ubicacionSeleccionada", result);

                requireActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
        mMap.addMarker(new MarkerOptions().position(initialLocation).title("Ubicación actual"));

        if (modoSeleccion) {
            mMap.setOnMapClickListener(latLng -> {
                seleccion = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
            });
        }
    }
}
