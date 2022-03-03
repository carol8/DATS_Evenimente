package com.carol8.datsevenimente.model;

import java.util.ArrayList;

public class ListaEvenimente {
    final ArrayList<Eveniment> evenimente = new ArrayList<>();

    public ListaEvenimente() {

    }

    public void actualizareLista(Eveniment actualizat){
        int ok = 0;
        for(Eveniment eveniment : evenimente){
            if(eveniment.getId().equals(actualizat.getId())){
                ok = 1;
                if(!eveniment.getNume().equals(actualizat.getNume())){
                    eveniment.setNume(actualizat.getNume());
                }
                if(!eveniment.getUrl().equals(actualizat.getUrl())){
                    eveniment.setUrl(actualizat.getUrl());
                }
                if(!eveniment.getDataInceput().equals(actualizat.getDataInceput())){
                    eveniment.setDataInceput(actualizat.getDataInceput());
                }
                if(!eveniment.getDataFinal().equals(actualizat.getDataFinal())){
                    eveniment.setDataFinal(actualizat.getDataFinal());
                }
                if(!eveniment.getStorageReference().equals(actualizat.getStorageReference())){
                    eveniment.setStorageReference(actualizat.getStorageReference());
                }
            }
        }
        if(ok == 0){
            evenimente.add(actualizat);
        }
    }

    public ArrayList<Eveniment> getEvenimente() {
        return evenimente;
    }
}
