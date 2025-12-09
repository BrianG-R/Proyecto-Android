package com.example.proyectoandroid.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Canje;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistorialCanjesAdapter extends RecyclerView.Adapter<HistorialCanjesAdapter.ViewHolder> {

    private List<Canje> lista;  // ❗ YA NO ES FINAL

    public HistorialCanjesAdapter(List<Canje> lista) {
        this.lista = lista;
    }

    // 👉 Método nuevo para actualizar el historial
    public void actualizarLista(List<Canje> nuevaLista) {
        this.lista.clear();
        this.lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_canje_historial, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Canje canje = lista.get(position);

        holder.tvBeneficio.setText("ID Beneficio: " + canje.id_beneficio);

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(canje.fecha_canje);

        holder.tvFecha.setText("Fecha: " + fecha);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBeneficio, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBeneficio = itemView.findViewById(R.id.tvBeneficioHist);
            tvFecha = itemView.findViewById(R.id.tvFechaHist);
        }
    }
}
