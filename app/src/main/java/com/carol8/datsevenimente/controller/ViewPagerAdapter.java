package com.carol8.datsevenimente.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.carol8.datsevenimente.view.evenimente.Evenimente;
import com.carol8.datsevenimente.view.servicii.Servicii;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (position){
            case 1:
                return new Servicii();
            default:
                return new Evenimente();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
