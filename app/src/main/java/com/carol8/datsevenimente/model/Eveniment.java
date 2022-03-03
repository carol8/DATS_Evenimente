package com.carol8.datsevenimente.model;

import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class Eveniment {
    private String nume, url, id;
    private Date dataInceput, dataFinal;
    private StorageReference storageReference;

    public Eveniment(String id, String nume, String url, Date dataInceput, Date dataFinal, StorageReference storageReference) {
        this.id = id;
        this.nume = nume;
        this.url = url;
        this.dataInceput = dataInceput;
        this.dataFinal = dataFinal;
        this.storageReference = storageReference;
    }

    public String getNume() {
        return nume;
    }

    public String getUrl() {
        return url;
    }

    public Date getDataInceput() {
        return dataInceput;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public StorageReference getStorageReference(){
        return storageReference;
    }

    public String getId() {
        return id;
    }

    public void setStorageReference(StorageReference storageReference){
        this.storageReference = storageReference;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDataInceput(Date dataInceput) {
        this.dataInceput = dataInceput;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}

