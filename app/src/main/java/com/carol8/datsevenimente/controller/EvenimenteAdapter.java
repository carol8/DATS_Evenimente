package com.carol8.datsevenimente.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Eveniment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EvenimenteAdapter extends RecyclerView.Adapter<EvenimenteAdapter.ViewHolder> {

    private List<Eveniment> mEveniments = new ArrayList<>();

    public EvenimenteAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm", Locale.US);
        Eveniment eveniment = mEveniments.get(position);

        holder.nameTextView.setText(eveniment.getNume());
        holder.dataInceputTextView.setText("Data inceput: " + dateFormat.format(eveniment.getDataInceput()));
        holder.dataFinalTextView.setText("Data final: " + dateFormat.format(eveniment.getDataFinal()));
        holder.buyButton.setText("Vezi detalii");
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
    }

    @Override
    public int getItemCount() {
        return mEveniments.size();
    }

    public void clear() {
        mEveniments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Eveniment> list) {
        mEveniments.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, dataInceputTextView, dataFinalTextView;
        public Button buyButton;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.evenimentNume);
            buyButton = itemView.findViewById(R.id.evenimentButon);
            dataInceputTextView = itemView.findViewById(R.id.evenimentDataInceput);
            dataFinalTextView = itemView.findViewById(R.id.evenimentDataFinal);
        }
    }
}
