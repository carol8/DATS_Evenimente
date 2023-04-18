package com.carol8.datsevenimente.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Service;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ServiciiAdapter extends RecyclerView.Adapter<ServiciiAdapter.ViewHolder>{
    private final ArrayList<Service> servicii = new ArrayList<>();
    private Context context;
    public ServiciiAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.item_service, parent, false);
        return new ViewHolder(eventView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciiAdapter.ViewHolder holder, int position) {
        Service service = servicii.get(position);

        holder.numeTextView.setText(service.getNume());
        holder.nrTelefonTextView.setText(String.format("Telefon: %s", service.getNrTelefon()));
        holder.serviciiTextView.setText(String.format("Servicii:\n%s", service.getServiciiString()));
        try {
            Geocoder geocoder = new Geocoder(holder.context.getApplicationContext(), new Locale("ro", "RO"));
            List<Address> addresses = geocoder.getFromLocation(service.getLocatie().latitude, service.getLocatie().longitude, 1);
            holder.locatieTextView.setText(String.format("Locatie: %s", addresses.get(0).getAddressLine(0)));
        } catch (IOException e) {
            holder.locatieTextView.setText(R.string.serviciiDefaultLocatieText);
            e.printStackTrace();
        }
        holder.callButton.setText(R.string.buton_apel);
        holder.callButton.setEnabled(true);
        holder.reviewsButton.setText(R.string.serviceReviewButtonText);
        holder.reviewsButton.setEnabled(true);
        holder.setMapLocation(service.getLocatie());

        holder.callButton.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse("tel:" + service.getNrTelefon());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(holder.callButton.getContext(), "Numarul de telefon nu exista! Incercati mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });

        holder.reviewsButton.setOnClickListener(view -> {
            Intent recenziiIntent = new Intent(context, Recenzii.class);
            recenziiIntent.putExtra("service", service);
            context.startActivity(recenziiIntent);
        });
    }

    @Override
    public int getItemCount() {
        return servicii.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAllServicii(List<Service> list) {
        servicii.clear();
        servicii.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sortareServicii(String tip){
        switch (tip){
            case "Alfabetic":
                //noinspection ComparatorCombinators
                Collections.sort(servicii, (service, t1) -> service.getNume().compareTo(t1.getNume()));
                notifyDataSetChanged();
                break;
            case "Distanta":
                FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocation.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            Collections.sort(servicii, ((service, t1) ->
                                    Math.round(service.getLocation().distanceTo(location) -
                                            t1.getLocation().distanceTo(location))));
                            notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(context, R.string.serviciiAdapter_toastLocatie, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, R.string.servicii_toastLocatie, Toast.LENGTH_SHORT).show();
                }
                break;
            case "Numar servicii descrescator":
                Collections.sort(servicii, (service, t1) ->
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
        public final TextView locatieTextView;
        public final Button callButton;
        public final Button reviewsButton;
        public final MapView mapView;
        public final Context context;
        private GoogleMap googleMap;
        private LatLng mapLocation;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            numeTextView = itemView.findViewById(R.id.serviceNume);
            nrTelefonTextView = itemView.findViewById(R.id.serviceNrTelefon);
            serviciiTextView = itemView.findViewById(R.id.serviceServicii);
            locatieTextView = itemView.findViewById(R.id.serviceLocatie);
            callButton = itemView.findViewById(R.id.serviceApeleazaButon);
            reviewsButton = itemView.findViewById(R.id.serviceRecenziiButton);
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
