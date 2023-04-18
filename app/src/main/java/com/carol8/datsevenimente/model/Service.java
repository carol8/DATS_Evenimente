package com.carol8.datsevenimente.model;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Service implements Serializable {
    private final String nume, nrTelefon;
    private final ArrayList<Tehnician> tehnicieni;
    private final ArrayList<String> servicii;
    private final double lat, lng;
    private final List<Recenzie> recenzii;
    private final String documentName;

    public Service(String nume, String nrTelefon, ArrayList<Tehnician> tehnicieni, ArrayList<String> servicii, GeoPoint locatie, List<Recenzie> recenzii, String documentName) {
        this.nume = nume;
        this.nrTelefon = nrTelefon;
        this.tehnicieni = tehnicieni;
        this.lat = locatie.getLatitude();
        this.lng = locatie.getLongitude();
        this.servicii = servicii;
        this.recenzii = recenzii;
        this.documentName = documentName;

        Collections.sort(tehnicieni);
        Collections.sort(servicii);
        Collections.sort(recenzii);
    }

    public String getNume() {
        return nume;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public LatLng getLocatie() {
        return new LatLng(lat, lng);
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }

    public ArrayList<String> getServicii() {
        return servicii;
    }

    public String getServiciiString(){
        StringBuilder stringBuilder = new StringBuilder();
        if(tehnicieni.size() != 0) {
            String serviciu = tehnicieni.get(0).getPozitie();
            stringBuilder.append(serviciu).append(": ").append(tehnicieni.get(0).getNume());
            for (Tehnician tehnician : tehnicieni.subList(1, tehnicieni.size())) {
                if (serviciu.compareTo(tehnician.getPozitie()) != 0) {
                    serviciu = tehnician.getPozitie();
                    stringBuilder.append("\n").append(serviciu).append(": ").append(tehnician.getNume());
                } else {
                    stringBuilder.append(",").append(tehnician.getNume());
                }
            }
        }
        else{
            stringBuilder.append(servicii.get(0));
            for(String string : servicii.subList(1, servicii.size())){
                stringBuilder.append('\n').append(string);
            }
        }
        return stringBuilder.toString();
    }

    public List<Recenzie> getRecenzii() {
        return recenzii;
    }

    public String getDocumentName() {
        return documentName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Service{" +
                "nume='" + nume + '\'' +
                ", nrTelefon='" + nrTelefon + '\'' +
                ", tehnicieni=" + tehnicieni +
                ", servicii=" + servicii +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
