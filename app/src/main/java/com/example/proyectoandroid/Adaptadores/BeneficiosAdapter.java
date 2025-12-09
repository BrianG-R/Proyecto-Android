package com.example.proyectoandroid.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Beneficio;

import java.util.List;

public class BeneficiosAdapter extends RecyclerView.Adapter<BeneficiosAdapter.ViewHolder> {

    private final List<Beneficio> listaBeneficios;
    private final Context context;

    private OnItemClickListener itemClickListener;
    private OnQRClickListener qrClickListener;
    private OnCanjearClickListener canjearClickListener;

    public interface OnItemClickListener {
        void onItemClick(Beneficio beneficio);
    }

    public interface OnQRClickListener {
        void onQRClick(Beneficio beneficio);
    }

    public interface OnCanjearClickListener {
        void onCanjearClick(Beneficio beneficio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnQRClickListener(OnQRClickListener listener) {
        this.qrClickListener = listener;
    }

    public void setOnCanjearClickListener(OnCanjearClickListener listener) {
        this.canjearClickListener = listener;
    }

    public BeneficiosAdapter(List<Beneficio> listaBeneficios, Context context) {
        this.listaBeneficios = listaBeneficios;
        this.context = context;
    }

    @NonNull
    @Override
    public BeneficiosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beneficio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiosAdapter.ViewHolder holder, int position) {

        Beneficio beneficio = listaBeneficios.get(position);

        holder.textViewNombre.setText(beneficio.getNombre());
        holder.textViewDescripcion.setText(beneficio.getDescripcion());
        holder.textViewCosto.setText("Costo: " + beneficio.getCosto() + " pts");

        if (beneficio.getUrlFoto() != null && !beneficio.getUrlFoto().isEmpty()) {
            Glide.with(context)
                    .load(beneficio.getUrlFoto())
                    .circleCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imgBeneficio);
        } else {
            holder.imgBeneficio.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onItemClick(beneficio);
        });

        holder.btnQR.setOnClickListener(v -> {
            if (qrClickListener != null) qrClickListener.onQRClick(beneficio);
        });

        holder.btnCanjear.setOnClickListener(v -> {
            if (canjearClickListener != null) canjearClickListener.onCanjearClick(beneficio);
        });
    }

    @Override
    public int getItemCount() {
        return listaBeneficios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombre, textViewDescripcion, textViewCosto;
        Button btnQR, btnCanjear;
        ImageView imgBeneficio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNombre = itemView.findViewById(R.id.tvNombreItem);
            textViewDescripcion = itemView.findViewById(R.id.tvDescItem);
            textViewCosto = itemView.findViewById(R.id.tvCostoItem);

            btnQR = itemView.findViewById(R.id.btnGenerarQRItem);
            btnCanjear = itemView.findViewById(R.id.btnCanjearItem);

            imgBeneficio = itemView.findViewById(R.id.imgBeneficioItem);
        }
    }
}

