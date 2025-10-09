package com.example.proyectoandroid.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Beneficio;

import java.util.List;

public class BeneficiosAdapter extends RecyclerView.Adapter<BeneficiosAdapter.ViewHolder> {

    private final List<Beneficio> listaBeneficios;
    private OnItemClickListener itemClickListener;
    private OnQRClickListener qrClickListener;

    public interface OnItemClickListener {
        void onItemClick(Beneficio beneficio);
    }

    public interface OnQRClickListener {
        void onQRClick(Beneficio beneficio);
    }

    public BeneficiosAdapter(List<Beneficio> listaBeneficios) {
        this.listaBeneficios = listaBeneficios;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnQRClickListener(OnQRClickListener listener) {
        this.qrClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beneficio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Beneficio beneficio = listaBeneficios.get(position);
        holder.textViewNombre.setText(beneficio.getNombre());
        holder.textViewDescripcion.setText(beneficio.getDescripcion());

        // CLICK en el item para selección
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(beneficio);
            }
        });

        // CLICK en el botón Generar QR
        holder.btnGenerarQR.setOnClickListener(v -> {
            if (qrClickListener != null) {
                qrClickListener.onQRClick(beneficio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaBeneficios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewDescripcion;
        Button btnGenerarQR; // Nuevo botón

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreBeneficio);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcionBeneficio);
            btnGenerarQR = itemView.findViewById(R.id.btnGenerarQR); // Vincula el botón
        }
    }
}
