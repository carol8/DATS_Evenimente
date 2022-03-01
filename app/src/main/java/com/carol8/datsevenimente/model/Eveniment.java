package com.carol8.datsevenimente.model;

import java.util.Date;

public class Eveniment {
    private String nume, url;
    private Date dataInceput, dataFinal;

    public Eveniment(String nume, String url, Date dataInceput, Date dataFinal) {
        this.nume = nume;
        this.url = url;
        this.dataInceput = dataInceput;
        this.dataFinal = dataFinal;
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
}

