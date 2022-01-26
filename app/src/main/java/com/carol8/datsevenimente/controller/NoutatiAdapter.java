package com.carol8.datsevenimente.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Noutate;

import java.util.ArrayList;
import java.util.List;

public class NoutatiAdapter extends RecyclerView.Adapter<NoutatiAdapter.ViewHolder> {
    private List<Noutate> noutati = new ArrayList<>();

    public NoutatiAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.item_noutate, parent, false);

        // Return a new holder instance

        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Noutate noutate = noutati.get(position);

        holder.nameTextView.setText(noutate.getNume());
        holder.descriptionTextView.setText(noutate.getDescriere());
    }

    @Override
    public int getItemCount() {
        return noutati.size();
    }

    public void clear() {
        noutati.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Noutate> list) {
        noutati.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eveniment_nume);
            descriptionTextView = itemView.findViewById(R.id.eveniment_descriere);
        }
    }
}
