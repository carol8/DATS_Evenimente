package com.carol8.datsevenimente.model;

import android.util.Pair;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;

public class Service {
    private final String nume, nrTelefon;
    private final ArrayList<Tehnician> tehnicieni;
    private final ArrayList<String> servicii;
    private final GeoPoint locatie;

    public Service(String nume, String nrTelefon, ArrayList<Tehnician> tehnicieni, GeoPoint locatie) {
        this.nume = nume;
        this.nrTelefon = nrTelefon;
        this.tehnicieni = tehnicieni;
        this.locatie = locatie;
        Collections.sort(tehnicieni);

        servicii = new ArrayList<>();
        String serviciu = "";
        for(Tehnician tehnician : tehnicieni){
            if(serviciu.compareTo(tehnician.getPozitie()) != 0){
                serviciu = tehnician.getPozitie();
                servicii.add(serviciu);
            }
        }
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

    public ArrayList<Tehnician> getTehnicieni() {
        return tehnicieni;
    }

    public ArrayList<String> getServicii() {
        return servicii;
    }

    public String getServiciiString(){
        StringBuilder stringBuilder = new StringBuilder();
        String serviciu = tehnicieni.get(0).getPozitie();
        stringBuilder.append(serviciu).append(": ").append(tehnicieni.get(0).getNume());
        for(Tehnician tehnician : tehnicieni.subList(1, tehnicieni.size())){
            if(serviciu.compareTo(tehnician.getPozitie()) != 0){
                serviciu = tehnician.getPozitie();
                stringBuilder.append("\n").append(serviciu).append(": ").append(tehnician.getNume());
            }
            else {
                stringBuilder.append(",").append(tehnician.getNume());
            }
        }
        return stringBuilder.toString();
    }
}
