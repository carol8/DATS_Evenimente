package com.carol8.datsevenimente.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.carol8.datsevenimente.model.Eveniment;
import com.carol8.datsevenimente.model.Service;
import com.carol8.datsevenimente.view.Evenimente;
import com.carol8.datsevenimente.view.Servicii;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Eveniment> evenimente;
    private ArrayList<Service> servicii;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Eveniment> evenimente, ArrayList<Service> servicii) {
        super(fragmentActivity);
        this.evenimente = evenimente;
        this.servicii = servicii;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Servicii(servicii);
            default:
                return new Evenimente(evenimente);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
