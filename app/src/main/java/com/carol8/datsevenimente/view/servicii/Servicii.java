package com.carol8.datsevenimente.view.servicii;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.controller.ServiciiAdapter;
import com.carol8.datsevenimente.model.Filtru;
import com.carol8.datsevenimente.model.Service;
import com.carol8.datsevenimente.model.Tehnician;
import com.carol8.datsevenimente.view.servicii.filtrare.FiltrareServicii;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Servicii extends Fragment {
    private ServiciiAdapter serviciiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ArrayList<Service> servicii = new ArrayList<>();
    private Filtru filtru = new Filtru();

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

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {
                            //noinspection ConstantConditions
                            if(result.get(Manifest.permission.ACCESS_COARSE_LOCATION) && result.get(Manifest.permission.ACCESS_FINE_LOCATION)){
                                serviciiAdapter.sortareServicii("Distanta");
                            }
                            else{
                                Toast.makeText(this.getContext(), R.string.servicii_toastLocatie, Toast.LENGTH_SHORT).show();
                            }
                        });

        butonSortare.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(butonSortare.getContext(), butonSortare, Gravity.BOTTOM);
            popupMenu.getMenuInflater().inflate(R.menu.popup_sortare, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getTitle().toString().equals("Distanta")){
                    if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage(view.getContext().getString(R.string.locationDialogExplanation, "sortarea"))
                                .setPositiveButton(R.string.locationDialogYesButton, (dialogInterface, i) -> locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}))
                                .setNegativeButton(R.string.locationDialogNoButton, (dialogInterface, i) -> Toast.makeText(view.getContext(), R.string.servicii_toastLocatie, Toast.LENGTH_SHORT).show())
                                .show();
                    } else {
                        serviciiAdapter.sortareServicii(menuItem.getTitle().toString());
                    }
                }
                else {
                    serviciiAdapter.sortareServicii(menuItem.getTitle().toString());
                }
                return true;
            });
            popupMenu.show();
        });

        Button butonFiltrare = v.findViewById(R.id.butonFiltrare);
        butonFiltrare.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FiltrareServicii.class);
            intent.putExtra("servicii", servicii);
            intent.putExtra("filtru", filtru);
            Log.wtf(TAG, filtru.toString());
            activityResultLauncher.launch(intent);
        });

        swipeRefreshLayout = v.findViewById(R.id.swipeContainerService);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAsync);
        fetchAsync();
        return v;
    }

    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        filtru = (Filtru) Objects.requireNonNull(result.getData()).getExtras().get("filtru");
                        ArrayList<Service> serviciiFiltrate = (ArrayList<Service>) result.getData().getExtras().get("servicii");
                        serviciiAdapter.addAllServicii(serviciiFiltrate);
                    }
                }
            }
    );

    @SuppressWarnings("ComparatorCombinators")
    public void fetchAsync() {
        servicii.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
        db.collection("service")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            ArrayList<Tehnician> s = new ArrayList<>();
                            ArrayList<String> srv = new ArrayList<>();
                            for(String str : Objects.requireNonNull(documentSnapshot.getString("servicii")).split(";")){
                                if(str.contains(":")) {
                                    srv.add(str.split(":")[0]);
                                    String oameni = str.split(":")[1];
                                    for (String str2 : oameni.split(",")) {
                                        s.add(new Tehnician(str2, str.split(":")[0]));
                                    }
                                }
                                else{
                                    srv.add(str);
                                }
                            }

                            servicii.add(new Service(documentSnapshot.getString("nume"),
                                    documentSnapshot.getString("nrTelefon"),
                                    s,
                                    srv,
                                    Objects.requireNonNull(documentSnapshot.getGeoPoint("locatie"))));
                        }
                        filtru = new Filtru();
                        //noinspection ComparatorCombinators
                        Collections.sort(servicii, (service, t1) -> service.getNume().compareTo(t1.getNume()));
                        serviciiAdapter.addAllServicii(servicii);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
