package com.carol8.datsevenimente.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Service {
    private String nume, nrTelefon;
    private ArrayList<String> servicii;
    private GeoPoint locatie;

    public Service(String nume, String nrTelefon, ArrayList<String> servicii, GeoPoint locatie) {
        this.nume = nume;
        this.nrTelefon = nrTelefon;
        this.servicii = servicii;
        this.locatie = locatie;
    }

    public String getNume() {
        return nume;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public ArrayList<String> getServicii() {
        return servicii;
    }

    public GeoPoint getLocatie() {
        return locatie;
    }

    public String getServiciiString(){
        StringBuilder s = new StringBuilder("-" + servicii.get(0));
        for(int i = 1; i < servicii.size(); i++){
            s.append("\n-").append(servicii.get(i));
        }
        return s.toString();
    }
}
