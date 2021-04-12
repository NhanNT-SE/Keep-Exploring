package com.example.keep_exploring.adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.keep_exploring.fragment.Fragment_Category;


public class Adapter_Tab_Category extends FragmentStatePagerAdapter {
    private Fragment_Category categoryFragment;

    public Adapter_Tab_Category(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("log", "position: " + position);

        switch (position) {
            case 0:
//                getCategory("");
//                return categoryFragment;
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: all");
                return getCategory("");


            case 1:
//                getCategory("food");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: food");

                return getCategory("food");
            case 2:
//                getCategory("check_in");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: checkin");
//                return categoryFragment;
                return getCategory("check_in");

            case 3:
//                getCategory("hotel");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: hotel");
//                return categoryFragment;
                return getCategory("hotel");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Food";
            case 2:
                return "Check-in";
            case 3:
                return "Accommodation";

        }
        return null;
    }

    private Fragment_Category getCategory(String category) {
        categoryFragment = new Fragment_Category();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }
}
