package com.carol8.datsevenimente.controller;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServiciiAdapter extends RecyclerView.Adapter<ServiciiAdapter.ViewHolder>{
    private final ArrayList<Service> mServicii = new ArrayList<>();

    public ServiciiAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        View eventView = inflater.inflate(R.layout.item_service, parent, false);
        return new ViewHolder(eventView, c);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciiAdapter.ViewHolder holder, int position) {
        Service service = mServicii.get(position);

        holder.numeTextView.setText(service.getNume());
        holder.nrTelefonTextView.setText(String.format("Telefon: %s", service.getNrTelefon()));
        holder.serviciiTextView.setText(String.format("Servicii:\n%s", service.getServiciiString()));
        holder.callButton.setText(R.string.buton_apel);
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

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        mServicii.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAllServicii(List<Service> list) {
        mServicii.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sortareServicii(String tip){
        switch (tip){
            case "Alfabetic":
                Collections.sort(mServicii, (service, t1) -> service.getNume().compareTo(t1.getNume()));
                notifyDataSetChanged();
                break;
            case "Numar servicii descrescator":
                Collections.sort(mServicii, (service, t1) ->
                        t1.getServicii().size() - service.getServicii().size() != 0 ?
                        t1.getServicii().size() - service.getServicii().size() :
                        service.getNume().compareTo(t1.getNume()));
                notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        public final TextView numeTextView;
        public final TextView nrTelefonTextView;
        public final TextView serviciiTextView;
        public final Button callButton;
        public final MapView mapView;
        private GoogleMap googleMap;
        private LatLng mapLocation;
        final Context context;

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
