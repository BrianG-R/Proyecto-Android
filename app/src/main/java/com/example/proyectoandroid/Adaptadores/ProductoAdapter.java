package com.example.proyectoandroid.Adaptadores;

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

    // 🔥 Nueva interfaz igual que Tiendas
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
        holder.txtEstado.setText(producto.isDisponible() ? "Disponible" : "No disponible");
        holder.txtEstado.setTextColor(producto.isDisponible() ? 0xFF4CAF50 : 0xFFF44336);

        // 🔥 CLICK NORMAL IGUAL QUE TIENDAS
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

        TextView txtNombre, txtPrecio, txtEstado;
        ImageView imgProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}
