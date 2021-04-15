package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.keep_exploring.R;


public class Fragment_Notification extends Fragment {
    View view;
    public Fragment_Notification() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView();
        return view;
    }

    private void initView() {
    }

}