package com.carol8.datsevenimente.model;

import java.util.Date;

public class Event {
    private String nume, descriere, url;
    private Date date;

    public Event(String nume, String descriere, String url, Date date) {
        this.nume = nume;
        this.descriere = descriere;
        this.url = url;
        this.date = date;
    }

    public String getNume() {
        return nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getUrl() {
        return url;
    }

    public Date getDate() {
        return date;
    }
}

