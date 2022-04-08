package com.carol8.datsevenimente.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.CheckBoxItem;

import java.util.ArrayList;

public class FiltruAdapter extends RecyclerView.Adapter<FiltruAdapter.ViewHolder>{

    private final ArrayList<CheckBoxItem> filtru;
    private final ArrayList<String> filtreSelectate;

    public FiltruAdapter(ArrayList<CheckBoxItem> filtru, ArrayList<String> filtreSelectate) {
        this.filtru = filtru;
        this.filtreSelectate = filtreSelectate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View numeView = inflater.inflate(R.layout.item_filtru, parent, false);
        return new ViewHolder(numeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckBoxItem checkBoxItem = filtru.get(position);

        holder.numeCheckbox.setText(checkBoxItem.getKey());
        for(String string : filtreSelectate){
            if(string.equals(checkBoxItem.getKey())){
                holder.numeCheckbox.setChecked(true);
            }
        }
        holder.numeCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.wtf(TAG, compoundButton.getText().toString());
            if(b){
                filtreSelectate.add(compoundButton.getText().toString());
            }
            else{
                filtreSelectate.remove(compoundButton.getText().toString());
            }
        });
        holder.labelCheckbox.setText(String.valueOf(checkBoxItem.getValue()));
    }

    @Override
    public int getItemCount() {
        return filtru.size();
    }

    public ArrayList<String> getFiltreSelectate() {
        return filtreSelectate;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final CheckBox numeCheckbox;
        public final TextView labelCheckbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeCheckbox = itemView.findViewById(R.id.numeCheckBox);
            labelCheckbox = itemView.findViewById(R.id.numeTextView);
        }
    }
}
