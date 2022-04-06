package com.carol8.datsevenimente.view.servicii.filtrare;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Filtru;
import com.carol8.datsevenimente.model.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class FiltrareServicii extends AppCompatActivity {
    private ArrayList<Service> servicii;
    private ArrayList<Service> serviciiFiltrate = new ArrayList<>();
    private Filtru filtru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrare_servicii);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.filtrareActivityTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        servicii = (ArrayList<Service>) getIntent().getExtras().get("servicii");
        filtru = (Filtru) getIntent().getExtras().get("filtru");
        Log.wtf(TAG, filtru.toString());
        serviciiFiltrate = filtrareTotala(filtru);


        Button filtrareLocatieButton = findViewById(R.id.filtrareLocatieButton);
        filtrareLocatieButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), LocatieServicii.class);
            intent.putExtra("filtru", filtru);
            filterResultLauncher.launch(intent);
        });

        Button filtrareNumeButton = findViewById(R.id.filtrareNumeButton);
        filtrareNumeButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), NumeServicii.class);
            Filtru filtruNou = new Filtru(filtru);
            filtruNou.getNume().clear();
            serviciiFiltrate = filtrareTotala(filtruNou);
            intent.putExtra("servicii", serviciiFiltrate);
            intent.putExtra("filtru", filtru);
            Log.wtf(TAG, filtru.toString());
            filterResultLauncher.launch(intent);
        });

        Button filtrareServiciuButton = findViewById(R.id.filtrareServiciiButton);
        filtrareServiciuButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ServiciuServicii.class);
            Filtru filtruNou = new Filtru(filtru);
            filtruNou.getServicii().clear();
            serviciiFiltrate = filtrareTotala(filtruNou);
            intent.putExtra("servicii", serviciiFiltrate);
            intent.putExtra("filtru", filtru);
            filterResultLauncher.launch(intent);
        });

        Button filtrareTerminataButton = findViewById(R.id.filtrareTerminataButton);
        filtrareTerminataButton.setOnClickListener(view -> finishResult());
    }

    private ArrayList<Service> filtrareTotala(Filtru filtru){
        ArrayList<Service> serviciiFiltrate = new ArrayList<>(servicii);
        //locatie
        if((filtru.getLat() != 0 || filtru.getLng() != 0) && filtru.getRadius() != -1){
            Iterator<Service> serviceIterator = serviciiFiltrate.iterator();
            while(serviceIterator.hasNext()){
                Service service = serviceIterator.next();

                Location serviceLocation = new Location("");
                serviceLocation.setLatitude(service.getLocatie().latitude);
                serviceLocation.setLongitude(service.getLocatie().longitude);

                Location userLocation = new Location("");
                userLocation.setLatitude(filtru.getLat());
                userLocation.setLongitude(filtru.getLng());

                if(serviceLocation.distanceTo(userLocation) > filtru.getRadius()){
                    serviceIterator.remove();
                }
            }
        }
        if(filtru.getNume().size() != 0){
            Iterator<Service> serviceIterator = serviciiFiltrate.iterator();
            while(serviceIterator.hasNext()){
                Service service = serviceIterator.next();

                if(!filtru.getNume().contains(service.getNume())){
                    serviceIterator.remove();
                }
            }
        }
        if(filtru.getServicii().size() != 0){
            Iterator<Service> serviceIterator = serviciiFiltrate.iterator();
            while(serviceIterator.hasNext()){
                Service service = serviceIterator.next();

                if(Collections.disjoint(service.getServicii(), filtru.getServicii())){
                    serviceIterator.remove();
                }
            }
        }
        return serviciiFiltrate;
    }

    final ActivityResultLauncher<Intent> filterResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    filtru = (Filtru) Objects.requireNonNull(result.getData()).getExtras().get("filtru");
                }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_filter, menu);
        SpannableString s = new SpannableString(getString(R.string.filtrareStergereButton));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        menu.getItem(0).setTitle(s);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishResult();
        }
        else if(item.getItemId() == R.id.actionSterge){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.filtrareStergereExplanation)
                    .setPositiveButton(R.string.filtrareStergereYes, (dialogInterface, i) -> filtru = new Filtru())
                    .setNeutralButton(R.string.filtrareStergereNo, (dialogInterface, i) -> {})
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishResult() {
        Intent finishIntent = new Intent();
        serviciiFiltrate = filtrareTotala(filtru);
        finishIntent.putExtra("servicii", serviciiFiltrate);
        finishIntent.putExtra("filtru", filtru);
        setResult(RESULT_OK, finishIntent);
        finish();
    }
}