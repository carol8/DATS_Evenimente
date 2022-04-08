package com.carol8.datsevenimente.view.servicii.filtrare;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Filtru;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;
import java.util.Objects;

public class LocatieServicii extends AppCompatActivity implements OnMapReadyCallback {
    private Filtru filtru;

    private GoogleMap googleMap;
    private LatLng mapLocation;
    private float mapZoom;
    private double mapCircleRadius;
    private Marker center;
    private Circle radiusCircle;
    private int circleColor = Color.BLACK;

    private double logVal = -1;

    private final Locale ro = new Locale("ro", "RO");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatie_servicii);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.locatieActivityTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        filtru = (Filtru) getIntent().getExtras().get("filtru");

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> initialiseMap());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.locationDialogExplanation, "filtrarea"))
                    .setPositiveButton(R.string.locationDialogYesButton, (dialogInterface, i) -> locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}))
                    .setNegativeButton(R.string.locationDialogNoButton, (dialogInterface, i) -> initialiseMap())
                    .show();
        } else {
            initialiseMap();
        }

        SeekBar locatieSeekBar = findViewById(R.id.locatieSeekBar);
        TextView razaLocatieTextView = findViewById(R.id.razaLocatieTextView);
        if ((filtru.getLat() != 0 || filtru.getLng() != 0) && filtru.getRadius() != -1) {
            locatieSeekBar.setProgress((int) filtru.getRadius());
            logVal = filtru.getRadius();
            if (logVal > 1000) {
                razaLocatieTextView.setText(String.format(ro, "%.1f km", logVal / 1000.0));
            } else {
                razaLocatieTextView.setText(String.format(ro, "%d m", (int) logVal));
            }
        }

        locatieSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i != seekBar.getMax()) {
                    logVal = ((double) i * i) / seekBar.getMax() + 1;
                    radiusCircle.setRadius(logVal);
                    if (logVal > 1000) {
                        razaLocatieTextView.setText(String.format(ro, "%.1f km", logVal / 1000.0));
                    } else {
                        razaLocatieTextView.setText(String.format(ro, "%d m", (int) logVal));
                    }
                } else {
                    logVal = -1;
                    radiusCircle.setRadius(0);
                    razaLocatieTextView.setText(R.string.locatieLabelDistanta);
                }
                updateMapContents();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initialiseMap() {
        MapView mapView = findViewById(R.id.locatieMapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void initialiseMapContents() {
        googleMap.clear();
        center = googleMap.addMarker(new MarkerOptions().position(mapLocation));
        radiusCircle = googleMap.addCircle(new CircleOptions()
                .radius(mapCircleRadius)
                .center(mapLocation)
                .strokeColor(circleColor)
                .strokeWidth(5));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mapLocation, mapZoom);
        googleMap.moveCamera(cameraUpdate);
    }

    private void updateMapContents() {
        LatLng latLng = googleMap.getCameraPosition().target;
        center.setPosition(latLng);
        radiusCircle.setCenter(latLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        this.googleMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        if ((this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            circleColor = Color.WHITE;
        }
        if ((filtru.getLat() != 0 || filtru.getLng() != 0) && filtru.getRadius() != -1) {
            mapLocation = new LatLng(filtru.getLat(), filtru.getLng());
            mapZoom = filtru.getZoom();
            mapCircleRadius = filtru.getRadius();
            initialiseMapContents();
        } else {
            FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocation.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        mapLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mapZoom = 13;
                    }
                    else{
                        Toast.makeText(this, R.string.serviciiAdapter_toastLocatie, Toast.LENGTH_SHORT).show();
                        mapLocation = new LatLng(0, 0);
                        mapZoom = 1;
                    }
                    mapCircleRadius = 0;
                    initialiseMapContents();
                });
            } else {
                mapLocation = new LatLng(0, 0);
                mapZoom = 1;
                mapCircleRadius = 0;
                initialiseMapContents();
            }
        }

        googleMap.setOnCameraMoveListener(this::updateMapContents);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            filterAndFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            filterAndFinish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterAndFinish() {
        Intent finishIntent = new Intent();
        filtru.setLat(googleMap.getCameraPosition().target.latitude);
        filtru.setLng(googleMap.getCameraPosition().target.longitude);
        filtru.setRadius(logVal);
        filtru.setZoom(googleMap.getCameraPosition().zoom);
        finishIntent.putExtra("filtru", filtru);
        setResult(RESULT_OK, finishIntent);
        finish();
    }
}