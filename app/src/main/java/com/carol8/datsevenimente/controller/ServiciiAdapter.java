package com.carol8.datsevenimente.controller;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.carol8.datsevenimente.model.Service;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ServiciiAdapter extends RecyclerView.Adapter<ServiciiAdapter.ViewHolder>{
    private ArrayList<Service> mServicii = new ArrayList<>();

    public ServiciiAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.item_service, parent, false);

        // Return a new holder instance

        return new ServiciiAdapter.ViewHolder(eventView, c);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciiAdapter.ViewHolder holder, int position) {
        Service service = mServicii.get(position);

        holder.numeTextView.setText(service.getNume());
        holder.nrTelefonTextView.setText(String.format("Telefon: %s", service.getNrTelefon()));
        holder.serviciiTextView.setText(String.format("Servicii:\n%s", service.getServiciiString()));
        holder.callButton.setText("Apeleaza");
        holder.callButton.setEnabled(true);
        holder.setMapLocation(new LatLng(service.getLocatie().getLatitude(), service.getLocatie().getLongitude()));

        holder.callButton.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse("tel:" + service.getNrTelefon());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(holder.callButton.getContext(), "Numarul de telefon nu exista! Incercati mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServicii.size();
    }

    public void clear() {
        mServicii.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAllServicii(List<Service> list) {
        mServicii.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        public TextView numeTextView, nrTelefonTextView, serviciiTextView;
        public Button callButton;
        public MapView mapView;
        private GoogleMap googleMap;
        private LatLng mapLocation;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            numeTextView = itemView.findViewById(R.id.serviceNume);
            nrTelefonTextView = itemView.findViewById(R.id.serviceNrTelefon);
            serviciiTextView = itemView.findViewById(R.id.serviceServicii);
            callButton = itemView.findViewById(R.id.serviceButon);
            mapView = itemView.findViewById(R.id.serviceMapView);

            this.context = context;
            if(mapView != null){
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            this.googleMap = googleMap;
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            if((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json));
            }
            updateMapContents();
        }

        public void setMapLocation(LatLng mapLocation){
            this.mapLocation = mapLocation;
            if(googleMap != null){
                updateMapContents();
            }
        }

        protected void updateMapContents() {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(mapLocation));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mapLocation, 14f);
            googleMap.moveCamera(cameraUpdate);
        }
    }
}
