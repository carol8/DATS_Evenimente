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

import com.carol8.datsevenimente.controller.EventsAdapter;
import com.carol8.datsevenimente.model.Event;
import com.carol8.datsevenimente.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Evenimente extends Fragment {
    private EventsAdapter eventsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Evenimente(){ }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.evenimente, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewEvents);
        eventsAdapter = new EventsAdapter();
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        swipeRefreshLayout = v.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAsync);
        fetchAsync();
        return v;
    }

    public void fetchAsync(){
        ArrayList<Event> events = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("evenimente").orderBy("data")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            //Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            events.add(new Event(documentSnapshot.getString("nume"), documentSnapshot.getString("detalii"), documentSnapshot.getString("url"), documentSnapshot.getTimestamp("data").toDate()));
                        }
                        eventsAdapter.clear();
                        eventsAdapter.addAll(events);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }
}