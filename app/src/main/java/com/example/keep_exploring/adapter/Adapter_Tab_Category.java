package com.example.keep_exploring.adapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Accommodations;
import com.example.keep_exploring.fragment.Fragment_AddBlog;
import com.example.keep_exploring.fragment.Fragment_BeautifulPlaces;
import com.example.keep_exploring.fragment.Fragment_Category;
import com.example.keep_exploring.fragment.Fragment_Restaurant;


public class Adapter_Tab_Category extends FragmentStatePagerAdapter {
    private Fragment_Category categoryFragment;

    public Adapter_Tab_Category(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("log", "position: "+position);

        switch (position) {
            case 0:
                getCategory("");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: all");
                return categoryFragment;

            case 1:
                getCategory("food");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: food");

                return categoryFragment;
            case 2:
                getCategory("check_in");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: checkin");

                return categoryFragment;
            case 3:
                getCategory("hotel");
                Log.d("log", "position1: "+position);
                Log.d("log", "getItem: hotel");
                return categoryFragment;
            default:
                return categoryFragment;
        }
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
            default:
                return null;
        }
    }

    private void getCategory(String category) {
        categoryFragment = new Fragment_Category();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        categoryFragment.setArguments(bundle);

    }
}
