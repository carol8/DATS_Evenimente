package com.carol8.datsevenimente.controller;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Eveniment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EvenimenteAdapter extends RecyclerView.Adapter<EvenimenteAdapter.ViewHolder> {

    private final List<Eveniment> mEvenimente = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ro", "RO"));
        Eveniment eveniment = mEvenimente.get(position);

        holder.nameTextView.setText(eveniment.getNume());
        holder.dataInceputTextView.setText(context.getResources().getString(R.string.data_inceput, dateFormat.format(eveniment.getDataInceput())));
        holder.dataFinalTextView.setText(context.getResources().getString(R.string.data_final, dateFormat.format(eveniment.getDataFinal())));
        holder.buyButton.setText(R.string.buton_evenimente);
        holder.buyButton.setEnabled(true);
        holder.buyButton.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse(eveniment.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(holder.buyButton.getContext(), "Detaliile nu exista! Incercati mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(context)
                .load(eveniment.getStorageReference())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mEvenimente.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        mEvenimente.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<Eveniment> list) {
        mEvenimente.addAll(list);
        Collections.sort(mEvenimente);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTextView;
        public final TextView dataInceputTextView;
        public final TextView dataFinalTextView;
        public final Button buyButton;
        public final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.evenimentNume);
            buyButton = itemView.findViewById(R.id.evenimentButon);
            dataInceputTextView = itemView.findViewById(R.id.evenimentDataInceput);
            dataFinalTextView = itemView.findViewById(R.id.evenimentDataFinal);
            imageView = itemView.findViewById(R.id.evenimentIcon);
        }
    }
}
