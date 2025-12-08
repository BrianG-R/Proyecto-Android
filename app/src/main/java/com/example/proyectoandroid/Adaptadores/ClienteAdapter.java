package com.example.proyectoandroid.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.modelo.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private OnClienteClickListener listener;

    public interface OnClienteClickListener {
        void onDetallesClick(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> clientes, OnClienteClickListener listener) {
        this.clientes = clientes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getTelefono());
        holder.tvFechaNacimiento.setText(cliente.getFechaNacimiento());
        holder.tvEstado.setText(cliente.getEstado());

        // Aquí podrías cargar la foto con Glide/Picasso si es URL
        // Glide.with(holder.imgFoto.getContext()).load(cliente.getFoto()).into(holder.imgFoto);

        holder.btnDetalles.setOnClickListener(v -> listener.onDetallesClick(cliente));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvFechaNacimiento, tvEstado;
        ImageView imgFoto;
        Button btnDetalles;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCliente);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoCliente);
            tvFechaNacimiento = itemView.findViewById(R.id.tvFechaNacimientoCliente);
            tvEstado = itemView.findViewById(R.id.tvEstadoCliente);
            imgFoto = itemView.findViewById(R.id.imgFotoCliente);
            btnDetalles = itemView.findViewById(R.id.btnDetallesCliente);
        }
    }
}
