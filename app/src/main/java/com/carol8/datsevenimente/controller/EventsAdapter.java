package com.carol8.datsevenimente.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> mEvents = new ArrayList<>();

    public EventsAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.item_event, parent, false);

        // Return a new holder instance

        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEvents.get(position);

        holder.nameTextView.setText(event.getNume());
        holder.descriptionTextView.setText(event.getDescriere());
        holder.dataTextView.setText(event.getDate().toString());
        holder.buyButton.setText("Vezi detalii");
        holder.buyButton.setEnabled(true);

        holder.buyButton.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse(event.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(holder.buyButton.getContext(), "Detaliile nu exista! Incercati mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void clear() {
        mEvents.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Event> list) {
        mEvents.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, descriptionTextView, dataTextView;
        public Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eveniment_nume);
            descriptionTextView = itemView.findViewById(R.id.eveniment_descriere);
            buyButton = itemView.findViewById(R.id.eveniment_button);
            dataTextView = itemView.findViewById(R.id.eveniment_data);
        }
    }
}
