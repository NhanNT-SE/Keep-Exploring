package com.example.project01_backup.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project01_backup.DAO.DAO_Post;
import com.example.project01_backup.R;


public class Fragment_Post_Details extends Fragment {

    private View view;
    private DAO_Post dao_post;

    public Fragment_Post_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        initView();
        initVariable();
        return view;
    }

    private void initView() {
    }

    private void initVariable() {
        dao_post = new DAO_Post(getContext());

//        dao_post.getPostById("606b1aad23618107b4608eb0");
    }
}