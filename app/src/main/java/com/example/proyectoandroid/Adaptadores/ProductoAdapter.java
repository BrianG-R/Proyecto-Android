package com.example.proyectoandroid.Adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Producto;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private boolean modoAdmin;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductoAdapter(List<Producto> productos) {
        this(productos, false);
    }

    public ProductoAdapter(List<Producto> productos, boolean modoAdmin) {
        this.productos = productos;
        this.modoAdmin = modoAdmin;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = modoAdmin ? R.layout.item_producto_admin : R.layout.item_producto_cliente;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtPrecio.setText("$" + producto.getPrecio());

        if (holder.txtStock != null) {
            holder.txtStock.setText("Stock: " + producto.getStock());
        }

        holder.txtEstado.setText(producto.isDisponible() ? "Disponible" : "No disponible");
        holder.txtEstado.setTextColor(producto.isDisponible() ? 0xFF4CAF50 : 0xFFF44336);

        // 🔥 LÓGICA DE IMAGEN (BASE64) AGREGADA AQUÍ
        if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(producto.getImagen(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgProducto.setImageBitmap(decodedByte);
            } catch (Exception e) {
                holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            // Imagen por defecto si no tiene
            holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        if (modoAdmin) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(producto);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.productos = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtPrecio, txtEstado, txtStock;
        ImageView imgProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtStock = itemView.findViewById(R.id.txtStock);
        }
    }
}