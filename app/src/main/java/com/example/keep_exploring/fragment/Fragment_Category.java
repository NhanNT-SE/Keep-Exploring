package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Category extends Fragment {
    private View view;
    private RecyclerView rv_PostList;
    private Adapter_RV_Post adapter_rv_post;
    private DAO_Post dao_post;
    private Helper_Common helper_common;
    private String category;
    private List<Post> listPost = new ArrayList<>();
    private TextView tvNothing;
    private Button btnAll, btnFood, btnCheckin, btnHotel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        init();
        showPost("");
        return view;
    }

    private void init() {
        helper_common = new Helper_Common();
        helper_common.toggleBottomNavigation(getContext(),true);
        rv_PostList = (RecyclerView) view.findViewById(R.id.fCategory_rvPostList);
        tvNothing = (TextView) view.findViewById(R.id.fCategory_tvNothing);
        btnAll = (Button) view.findViewById(R.id.fCategory_btnAll);
        btnFood = (Button) view.findViewById(R.id.fCategory_btnFood);
        btnCheckin = (Button) view.findViewById(R.id.fCategory_btnCheckin);
        btnHotel = (Button) view.findViewById(R.id.fCategory_btnHotel);
        helper_common.configRecycleView(getContext(), rv_PostList);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPost("");
            }
        });

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPost("food");
            }
        });

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPost("check_in");
            }
        });

        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPost("hotel");
            }
        });


    }

    private void showPost(String category) {
        dao_post = new DAO_Post(view.getContext());
        dao_post.getPostByCategory(category, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Post> postList1 = (List<Post>) response;
                refreshLV(postList1);
            }

            @Override
            public void failedReq(String msg) {
                Log.d("TAG", "failedReq: " + msg);
            }
        });
    }

    private void refreshLV(List<Post> postList) {
        listPost = postList;
        adapter_rv_post = new Adapter_RV_Post(getContext(), listPost);
        rv_PostList.setAdapter(adapter_rv_post);
        if (postList.size() > 0) {
            tvNothing.setVisibility(View.GONE);
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }
    }

    private void log(String s) {
        Log.d("log", s);
    }

}