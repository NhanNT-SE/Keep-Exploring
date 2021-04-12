package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_Tab_Category;
import com.google.android.material.tabs.TabLayout;


public class Fragment_TabCategory extends Fragment {
    private View view;
    private TabLayout tab;
    private ViewPager viewPager;

    public Fragment_TabCategory() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__tab_category, container, false);
        initView();
        return view;
    }

    private void initView() {
        tab = (TabLayout) view.findViewById(R.id.tab);
        viewPager = (ViewPager) view.findViewById(R.id.vp);
        viewPager.setAdapter(new Adapter_Tab_Category(getChildFragmentManager(), 4));
        tab.setupWithViewPager(viewPager);
    }
}

