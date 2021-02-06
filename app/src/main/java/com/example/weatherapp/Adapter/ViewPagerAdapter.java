package com.example.weatherapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private  final List<Fragment> fragList = new ArrayList<>();
    private final List<String> fragName = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    public void addFragment(Fragment fragment, String name){
        fragList.add(fragment);
        fragName.add(name);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragName.get(position);
    }
}
