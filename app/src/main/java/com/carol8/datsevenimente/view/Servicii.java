package com.carol8.datsevenimente.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.controller.ServiciiAdapter;
import com.carol8.datsevenimente.model.Service;
import com.carol8.datsevenimente.model.Tehnician;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Servicii extends Fragment {
    private ServiciiAdapter serviciiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ArrayList<Service> servicii = new ArrayList<>();

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

        Button butonSortare = v.findViewById(R.id.butonSortare);
        butonSortare.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(butonSortare.getContext(), butonSortare, Gravity.BOTTOM);
            popupMenu.getMenuInflater().inflate(R.menu.popup_sortare, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                serviciiAdapter.sortareServicii(menuItem.getTitle().toString());
                return true;
            });
            popupMenu.show();
        });
        EditText editTextFiltrare = v.findViewById(R.id.editTextFiltrare);
        editTextFiltrare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                serviciiAdapter.FiltrareServicii(editable.toString());
            }
        });

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
                            ArrayList<Tehnician> s = new ArrayList<>();
                            for(String str : Objects.requireNonNull(documentSnapshot.getString("servicii")).split(";")){
                                String oameni = str.split(":")[1];
                                for(String str2: oameni.split(",")){
                                    s.add(new Tehnician(str2, str.split(":")[0]));
                                }
                            }

                            servicii.add(new Service(documentSnapshot.getString("nume"),
                                    documentSnapshot.getString("nrTelefon"),
                                    s,
                                    documentSnapshot.getGeoPoint("locatie")));
                        }
                        serviciiAdapter.clear();
                        serviciiAdapter.addAllServicii(servicii);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
