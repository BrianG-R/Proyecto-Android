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
    private final Context context; // Necesario para cargar imágenes con Glide
    private OnItemClickListener itemClickListener;
    private OnQRClickListener qrClickListener;

    // Interfaces para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(Beneficio beneficio);
    }

    public interface OnQRClickListener {
        void onQRClick(Beneficio beneficio);
    }

    // Constructor actualizado
    public BeneficiosAdapter(List<Beneficio> listaBeneficios, Context context) {
        this.listaBeneficios = listaBeneficios;
        this.context = context;
    }

    // Métodos para configurar los listeners
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnQRClickListener(OnQRClickListener listener) {
        this.qrClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño de la tarjeta individual (item_beneficio.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beneficio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Beneficio beneficio = listaBeneficios.get(position);

        // 1. Asignar textos
        holder.textViewNombre.setText(beneficio.getNombre());
        holder.textViewDescripcion.setText(beneficio.getDescripcion());

        // 2. CARGAR LA IMAGEN CON GLIDE
        // Verificamos si hay URL, si no, ponemos una imagen por defecto
        if (beneficio.getUrlFoto() != null && !beneficio.getUrlFoto().isEmpty()) {
            Glide.with(context)
                    .load(beneficio.getUrlFoto())
                    .circleCrop() // Esto hace que la imagen se vea redonda
                    .placeholder(R.drawable.ic_launcher_background) // Imagen mientras carga
                    .error(R.drawable.ic_launcher_background) // Imagen si falla el link
                    .into(holder.imgBeneficio);
        } else {
            holder.imgBeneficio.setImageResource(R.drawable.ic_launcher_background);
        }

        // 3. Configurar Clic en la tarjeta (Para editar/borrar)
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(beneficio);
            }
        });

        // 4. Configurar Clic en el botón QR
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

    // CLASE VIEWHOLDER: Vincula los elementos visuales
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewDescripcion;
        Button btnGenerarQR;
        ImageView imgBeneficio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs deben existir EXACTAMENTE IGUAL en tu archivo item_beneficio.xml
            textViewNombre = itemView.findViewById(R.id.tvNombreItem);
            textViewDescripcion = itemView.findViewById(R.id.tvDescItem);
            btnGenerarQR = itemView.findViewById(R.id.btnGenerarQRItem);
            imgBeneficio = itemView.findViewById(R.id.imgBeneficioItem);
        }
    }
}