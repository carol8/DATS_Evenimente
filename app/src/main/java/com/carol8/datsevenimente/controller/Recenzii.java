package com.carol8.datsevenimente.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carol8.datsevenimente.R;
import com.carol8.datsevenimente.model.Recenzie;
import com.carol8.datsevenimente.model.Service;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Recenzii extends AppCompatActivity {
    private Service service;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        service = (Service) getIntent().getSerializableExtra("service");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recenzii " + service.getNume());

        RecyclerView recyclerView = findViewById(R.id.recenzieRecyclerView);
        RecenziiAdapter recenziiAdapter = new RecenziiAdapter(service.getRecenzii());
        recyclerView.setAdapter(recenziiAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        EditText numeUtilizator = findViewById(R.id.numeUtilizatorEditText);
        EditText review = findViewById(R.id.reviewEditText);
        Button sendReviewButton = findViewById(R.id.sendReviewButton);
        DocumentReference documentReference = FirebaseFirestore.getInstance().document("service/" + service.getDocumentName());
        sendReviewButton.setOnClickListener(view -> {
            //data conversions
            List<String> recenziiUser = new ArrayList<>();
            List<String> recenziiDate = new ArrayList<>();
            List<String> recenziiText = new ArrayList<>();
            for(Recenzie recenzie : service.getRecenzii()){
                recenziiUser.add(recenzie.getUser());
                recenziiDate.add(new SimpleDateFormat("dd.MM.yyyy").format(recenzie.getDataPostarii()));
                recenziiText.add(recenzie.getText());
            }
            recenziiUser.add(numeUtilizator.getText().toString());
            recenziiDate.add(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
            recenziiText.add(review.getText().toString());

            //db update
            WriteBatch batch = FirebaseFirestore.getInstance().batch();

            batch.update(documentReference, "recenziiDate", String.join(";", recenziiDate));
            batch.update(documentReference, "recenziiText", String.join(";", recenziiText));
            batch.update(documentReference, "recenziiUser", String.join(";", recenziiUser));

            batch.commit().addOnCompleteListener(task -> {
                service.getRecenzii().add(
                    new Recenzie(
                        review.getText().toString(),
                        numeUtilizator.getText().toString(),
                        Calendar.getInstance().getTime()
                    )
                );
                recenziiAdapter.updateRecenzii(service.getRecenzii());
            }).addOnFailureListener(e -> Toast.makeText(this, "Nu am reusit sa postam recenzia!", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
