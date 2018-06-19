package com.example.peterphchen.firebasepractice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ImagePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> pathlist;

    public ImagePagerAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        pathlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return ImagePagerFragment.newInstance(position,pathlist);
    }

    @Override
    public int getCount() {
        return pathlist.size();
    }
}
