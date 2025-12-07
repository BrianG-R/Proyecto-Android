package com.example.proyectoandroid.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Mensaje;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;


import java.util.Date;
import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    public interface OnMensajeActions {
        void onEliminar(Mensaje mensaje);
        void onResponder(Mensaje mensaje);
    }

    private List<Mensaje> lista;
    private String uidActual;
    private OnMensajeActions listener;

    public MensajeAdapter(List<Mensaje> lista, String uidActual, OnMensajeActions listener) {
        this.lista = lista;
        this.uidActual = uidActual;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAutor;
        TextView tvNombre, tvContenido, tvFecha, tvRespuestaA;
        ImageButton btnEliminar, btnResponder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAutor = itemView.findViewById(R.id.imgAutor);
            tvNombre = itemView.findViewById(R.id.tvNombreAutor);
            tvContenido = itemView.findViewById(R.id.tvContenido);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvRespuestaA = itemView.findViewById(R.id.tvRespuestaA);

            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnResponder = itemView.findViewById(R.id.btnResponder);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Mensaje m = lista.get(position);

        holder.tvNombre.setText(m.getAutorNombre());
        holder.tvContenido.setText(m.getContenido());
        holder.tvFecha.setText(new Date(m.getFecha()).toString());

        // FOTO
        if (m.getFotoAutor() != null && !m.getFotoAutor().isEmpty()) {
            try {
                byte[] bytes = Base64.decode(m.getFotoAutor(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                Bitmap redonda = hacerRedonda(bmp);

                holder.imgAutor.setImageBitmap(redonda);

            } catch (Exception ignored) {}
        } else {
            holder.imgAutor.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // RESPUESTA
        if (m.getTextoRespuesta() != null && !m.getTextoRespuesta().isEmpty()) {
            holder.tvRespuestaA.setText("Respuesta a: " + m.getTextoRespuesta());
            holder.tvRespuestaA.setVisibility(View.VISIBLE);
        } else {
            holder.tvRespuestaA.setVisibility(View.GONE);
        }

        // SOLO autor puede eliminar
        if (!m.getAutorUid().equals(uidActual)) {
            holder.btnEliminar.setVisibility(View.GONE);
        }

        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(m));
        holder.btnResponder.setOnClickListener(v -> listener.onResponder(m));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
    private Bitmap hacerRedonda(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        float radius = size / 2f;

        canvas.drawCircle(radius, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, size, size),
                paint);

        return output;
    }

}

