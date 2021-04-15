package com.example.keep_exploring.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class Adapter_Tab_User extends FragmentStatePagerAdapter {
    public Adapter_Tab_User(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return new Fragment_Profile_PostList();
            case 1:
//                return new Fragment_Profile_BlogList();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
