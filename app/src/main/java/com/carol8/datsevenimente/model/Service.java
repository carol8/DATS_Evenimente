package com.carol8.datsevenimente.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Service {
    private final String nume, nrTelefon;
    private final ArrayList<String> servicii;
    private final GeoPoint locatie;

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
