package com.carol8.datsevenimente.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carol8.datsevenimente.controller.EvenimenteAdapter;
import com.carol8.datsevenimente.model.Eveniment;
import com.carol8.datsevenimente.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Evenimente extends Fragment {
    private EvenimenteAdapter evenimenteAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Eveniment> evenimente = new ArrayList<>();

    public Evenimente(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.evenimente, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewEvenimente);
        evenimenteAdapter = new EvenimenteAdapter();
        recyclerView.setAdapter(evenimenteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        swipeRefreshLayout = v.findViewById(R.id.swipeContainerEvenimente);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAsync);
        fetchAsync();
        return v;
    }

    public void fetchAsync(){
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
                        evenimenteAdapter.clear();
                        evenimenteAdapter.addAll(evenimente);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }
}