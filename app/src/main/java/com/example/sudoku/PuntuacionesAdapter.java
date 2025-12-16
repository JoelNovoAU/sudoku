package com.example.sudoku;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PuntuacionesAdapter extends RecyclerView.Adapter<PuntuacionesAdapter.ViewHolder> {

    private ArrayList<Puntuaciones> lista;

    public PuntuacionesAdapter(ArrayList<Puntuaciones> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_puntuacion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Puntuaciones p = lista.get(position);

        holder.txtNombre.setText(p.getNombre());
        holder.txtTiempo.setText(p.getTiempoFormateado());
        holder.txtDificultad.setText(p.getDificultadTexto());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtTiempo, txtDificultad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTiempo = itemView.findViewById(R.id.txtTiempo);
            txtDificultad = itemView.findViewById(R.id.txtDificultad);
        }
    }
}
