package com.carol8.datsevenimente.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.controller.ServiciiAdapter;
import com.carol8.datsevenimente.model.Service;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Servicii extends Fragment {
    private ServiciiAdapter serviciiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Service> servicii = new ArrayList<>();

    public Servicii() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.service, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewService);
        serviciiAdapter = new ServiciiAdapter();
        recyclerView.setAdapter(serviciiAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        swipeRefreshLayout = v.findViewById(R.id.swipeContainerService);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAsync);
        fetchAsync();
        return v;
    }

    public void fetchAsync() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
        db.collection("service")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            servicii.add(new Service(documentSnapshot.getString("nume"), documentSnapshot.getString("nrTelefon"), new ArrayList<>(Arrays.asList(documentSnapshot.getString("servicii").split(";"))), documentSnapshot.getGeoPoint("locatie")));
                        }
                        serviciiAdapter.clear();
                        serviciiAdapter.addAllServicii(servicii);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
