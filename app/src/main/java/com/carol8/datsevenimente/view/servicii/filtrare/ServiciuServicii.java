package com.carol8.datsevenimente.view.servicii.filtrare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.controller.FiltruAdapter;
import com.carol8.datsevenimente.model.CheckBoxItem;
import com.carol8.datsevenimente.model.Filtru;
import com.carol8.datsevenimente.model.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ServiciuServicii extends AppCompatActivity {
    private Filtru filtru;
    private final ArrayList<CheckBoxItem> srv = new ArrayList<>();
    private ArrayList<String> serviciiSelectate = new ArrayList<>();
    private FiltruAdapter filtruAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicii_servicii);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.serviciiActivityTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ArrayList<Service> servicii = (ArrayList<Service>) getIntent().getExtras().get("servicii");
        filtru = (Filtru) getIntent().getExtras().get("filtru");
        Log.wtf(TAG, filtru.toString());
        for(Service service : servicii){
            for(String serviciu : service.getServicii()) {
                boolean ok = false;
                for (CheckBoxItem checkBoxItem : srv) {
                    if (checkBoxItem.getKey().equals(serviciu)) {
                        checkBoxItem.setValue(checkBoxItem.getValue() + 1);
                        ok = true;
                    }
                }
                if (!ok) {
                    srv.add(new CheckBoxItem(serviciu, 1));
                }
            }
        }
        if(filtru.getServicii() != null){
            serviciiSelectate = filtru.getServicii();
        }
        Collections.sort(srv);
        RecyclerView recyclerView = findViewById(R.id.serviciiRecyclerView);
        filtruAdapter = new FiltruAdapter(srv, serviciiSelectate);
        recyclerView.setAdapter(filtruAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        filterAndFinish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        filterAndFinish();
        return super.onOptionsItemSelected(item);
    }

    public void filterAndFinish() {
        Intent finishIntent = new Intent();
        filtru.setServicii(filtruAdapter.getFiltreSelectate());
        Log.wtf(TAG, filtru.toString());
        finishIntent.putExtra("filtru", filtru);
        setResult(RESULT_OK, finishIntent);
        finish();
    }
}