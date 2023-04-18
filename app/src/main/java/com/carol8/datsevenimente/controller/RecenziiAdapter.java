package com.carol8.datsevenimente.controller;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Recenzie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecenziiAdapter extends RecyclerView.Adapter<RecenziiAdapter.ViewHolder>{
    private final List<Recenzie> recenzii = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void updateRecenzii(List<Recenzie> recenzii){
        this.recenzii.clear();
        this.recenzii.addAll(recenzii);
        Collections.sort(this.recenzii);
        notifyDataSetChanged();
    }

    public RecenziiAdapter(List<Recenzie> recenzii){
        this.recenzii.addAll(recenzii);
        Collections.sort(this.recenzii);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View recenzieView = inflater.inflate(R.layout.item_review, parent, false);
        return new ViewHolder(recenzieView);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recenzie recenzie = recenzii.get(position);

        holder.userTextView.setText(recenzie.getUser());
        holder.dataTextView.setText(new SimpleDateFormat("dd.MM.yyyy").format(recenzie.getDataPostarii()));
        holder.textTextView.setText(recenzie.getText());
    }

    @Override
    public int getItemCount() {
        return recenzii.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView userTextView;
        public final TextView dataTextView;
        public final TextView textTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.userTextView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
        }
    }
}
