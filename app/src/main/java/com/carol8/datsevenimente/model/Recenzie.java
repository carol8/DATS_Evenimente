package com.carol8.datsevenimente.model;

import java.io.Serializable;
import java.util.Date;

public class Recenzie implements Comparable<Recenzie>, Serializable {
    private final String text, user;
    private final Date dataPostarii;

    public Recenzie(String text, String user, Date dataPostarii) {
        this.text = text;
        this.user = user;
        this.dataPostarii = dataPostarii;
    }

    public String getText() {
        return text;
    }

    public String getUser(){
        return user;
    }

    public Date getDataPostarii() {
        return dataPostarii;
    }

//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public void setUser(String user){
//        this.user = user;
//    }
//
//    public void setDataPostarii(Date dataPostarii) {
//        this.dataPostarii = dataPostarii;
//    }

    @Override
    public int compareTo(Recenzie recenzie) {
        return dataPostarii.compareTo(recenzie.dataPostarii);
    }
}
