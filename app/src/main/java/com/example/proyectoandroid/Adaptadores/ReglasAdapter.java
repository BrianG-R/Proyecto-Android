package com.example.proyectoandroid.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Regla;

import java.util.List;

public class ReglasAdapter extends RecyclerView.Adapter<ReglasAdapter.ReglaViewHolder> {

    private final List<Regla> listaReglas;
    private OnItemClickListener listener;
    private OnQRClickListener qrClickListener;

    // Listener para click en QR
    public interface OnQRClickListener {
        void onQRClick(Regla regla);
    }

    public void setOnQRClickListener(OnQRClickListener listener) {
        this.qrClickListener = listener;
    }

    // Listener para click en el item
    public interface OnItemClickListener {
        void onItemClick(Regla regla);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ReglasAdapter(List<Regla> listaReglas) {
        this.listaReglas = listaReglas;
    }

    @NonNull
    @Override
    public ReglaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_regla, parent, false);
        return new ReglaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReglaViewHolder holder, int position) {

        Regla regla = listaReglas.get(position);

        holder.tvNombre.setText(regla.getNombre());
        holder.tvDescripcion.setText(regla.getDescripcion());
        holder.tvOtros.setText(regla.getOtros());

        // CLICK EN EL ITEM COMPLETO
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(regla);
        });


    }

    @Override
    public int getItemCount() {
        return listaReglas.size();
    }

    static class ReglaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvDescripcion, tvOtros;


        public ReglaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombreRegla);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionRegla);
            tvOtros = itemView.findViewById(R.id.tvOtrosRegla);

        }
    }
}
