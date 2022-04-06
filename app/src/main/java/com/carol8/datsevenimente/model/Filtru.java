package com.carol8.datsevenimente.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Filtru implements Serializable, Cloneable {
    private double lat, lng;
    private double radius = -1;
    private float zoom;

    public Filtru(){}

    public Filtru(Filtru filtru){
        this.lat = filtru.getLat();
        this.lng = filtru.getLng();
        this.radius = filtru.getRadius();
        this.zoom = filtru.getZoom();
        this.nume.addAll(filtru.getNume());
        this.servicii.addAll(filtru.getServicii());
    }

    private ArrayList<String> nume = new ArrayList<>();

    private ArrayList<String> servicii = new ArrayList<>();

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public ArrayList<String> getNume() {
        return nume;
    }

    public void setNume(ArrayList<String> nume) {
        this.nume = nume;
    }

    public ArrayList<String> getServicii() {
        return servicii;
    }

    public void setServicii(ArrayList<String> servicii) {
        this.servicii = servicii;
    }

    @NonNull
    @Override
    public String toString() {
        return "Filtru{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", radius=" + radius +
                ", nume=" + nume +
                ", servicii=" + servicii +
                '}';
    }
}
