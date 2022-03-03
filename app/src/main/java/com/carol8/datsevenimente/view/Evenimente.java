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
import com.carol8.datsevenimente.model.ListaEvenimente;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Evenimente extends Fragment {
    private EvenimenteAdapter evenimenteAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ListaEvenimente evenimente = new ListaEvenimente();

    public Evenimente(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        fetchAsyncDatabase();
    }

    public void fetchAsyncDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
        firebaseFirestore.collection("evenimente").orderBy("dataInceput")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(int index = 0; index < task.getResult().size(); index++) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(index);
                            evenimente.actualizareLista(new Eveniment(documentSnapshot.getId(), documentSnapshot.getString("nume"),
                                    documentSnapshot.getString("url"),
                                    Objects.requireNonNull(documentSnapshot.getTimestamp("dataInceput")).toDate(),
                                    Objects.requireNonNull(documentSnapshot.getTimestamp("dataFinal")).toDate(),
                                    firebaseStorage.getReference(documentSnapshot.getString("nume") + "/icon.jpg")));
                        }
                        evenimenteAdapter.clear();
                        evenimenteAdapter.addAll(evenimente.getEvenimente());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}