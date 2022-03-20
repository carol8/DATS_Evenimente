package com.carol8.datsevenimente.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Tehnician implements Comparable{
    private String nume, pozitie;
    private HashMap<ArrayList<String>, Integer> recenzii = new HashMap<>();

    public Tehnician(String nume, String pozitie) {
        this.nume = nume;
        this.pozitie = pozitie;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPozitie() {
        return pozitie;
    }

    public void setPozitie(String pozitie) {
        this.pozitie = pozitie;
    }

    public HashMap<ArrayList<String>, Integer> getRecenzii() {
        return recenzii;
    }

    public void setRecenzii(HashMap<ArrayList<String>, Integer> recenzii) {
        this.recenzii = recenzii;
    }

    @Override
    public int compareTo(Object o) {
        return this.pozitie.compareTo(((Tehnician)o).pozitie) != 0 ? this.pozitie.compareTo(((Tehnician)o).pozitie) : this.nume.compareTo(((Tehnician)o).nume);
    }
}
