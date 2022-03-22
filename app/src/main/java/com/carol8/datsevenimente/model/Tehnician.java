package com.carol8.datsevenimente.model;

public class Tehnician implements Comparable<Tehnician>{
    private final String nume;
    private final String pozitie;
    // --Commented out by Inspection (22-Mar-22 03:33):private final HashMap<ArrayList<String>, Integer> recenzii = new HashMap<>();

    public Tehnician(String nume, String pozitie) {
        this.nume = nume;
        this.pozitie = pozitie;
    }

    public String getNume() {
        return nume;
    }

    public String getPozitie() {
        return pozitie;
    }

    @Override
    public int compareTo(Tehnician tehnician) {
        return this.pozitie.compareTo(tehnician.pozitie) != 0 ?
                this.pozitie.compareTo(tehnician.pozitie) :
                this.nume.compareTo(tehnician.nume);
    }
}
