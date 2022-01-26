package com.carol8.datsevenimente.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.controller.NoutatiAdapter;
import com.carol8.datsevenimente.model.Event;
import com.carol8.datsevenimente.model.Noutate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Noutati extends Fragment {
    private NoutatiAdapter noutatiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Noutati() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.noutati, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewNoutati);
        noutatiAdapter = new NoutatiAdapter();
        recyclerView.setAdapter(noutatiAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        swipeRefreshLayout = v.findViewById(R.id.swipeContainerNoutati);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAsync);
        fetchAsync();
        return v;
    }

    public void fetchAsync(){
        ArrayList<Noutate> noutati = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("noutati")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            //Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            noutati.add(new Noutate(documentSnapshot.getString("nume"), documentSnapshot.getString("detalii")));
                        }
                        noutatiAdapter.clear();
                        noutatiAdapter.addAll(noutati);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }
}