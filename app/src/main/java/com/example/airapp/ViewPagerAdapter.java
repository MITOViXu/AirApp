package com.example.airapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.airapp.Fragment.GrapFragment;
import com.example.airapp.Fragment.MainDashBoardFragment;
import com.example.airapp.Fragment.MapFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainDashBoardFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new GrapFragment();
            default:
                return new MainDashBoardFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
