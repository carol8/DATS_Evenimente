package com.carol8.datsevenimente.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Eveniment;
import com.carol8.datsevenimente.model.Service;
import com.carol8.datsevenimente.view.Evenimente;
import com.carol8.datsevenimente.view.Servicii;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Eveniment> evenimente = new ArrayList<>();
        ArrayList<Service> servicii = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
        db.collection("evenimente").orderBy("dataInceput")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            //Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            evenimente.add(new Eveniment(documentSnapshot.getString("nume"), documentSnapshot.getString("url"), documentSnapshot.getTimestamp("dataInceput").toDate(), documentSnapshot.getTimestamp("dataFinal").toDate()));
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                });
        db.collection("service")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            //Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            servicii.add(new Service(documentSnapshot.getString("nume"), documentSnapshot.getString("nrTelefon"), new ArrayList<>(Arrays.asList(documentSnapshot.getString("servicii").split(";"))), documentSnapshot.getGeoPoint("locatie")));
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                });

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, evenimente, servicii);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Evenimente");
            } else {
                tab.setText("Service Auto");
            }
        }).attach();
    }
}