package com.example.keep_exploring.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_PostAdmin;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Censorship extends Fragment {
    private View view;
    private Adapter_LV_PostAdmin adapterPost;
    private ListView lvPost;
    private TextView tvNothing;
    private TextView tvNumber;

    public Fragment_Censorship() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_censorship, container, false);
        initView();
        return view;
    }

    private void initView() {
        lvPost = (ListView) view.findViewById(R.id.fCensorship_lvPost);
        tvNumber = (TextView) view.findViewById(R.id.fCensorship_tvNumber);
        tvNothing = (TextView) view.findViewById(R.id.fCensorship_tvNothing);


    }
}
