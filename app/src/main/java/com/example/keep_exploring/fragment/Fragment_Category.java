package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Category extends Fragment {
    private View view;
    private RecyclerView rv_PostList;
    private Adapter_RV_Post adapter_rv_post;
    private DAO_Post dao_post;
    private Helper_Common helper_common;
    private String category;
    private List<Post> postList;
    private TextView tvNothing;
    private MaterialButton btnAll, btnFood, btnCheckin, btnHotel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        init();
        return view;
    }

    private void init() {
        helper_common = new Helper_Common();
        dao_post = new DAO_Post(getContext());
        helper_common.toggleBottomNavigation(getContext(), true);
        postList = new ArrayList<>();
        category = "";
        rv_PostList = (RecyclerView) view.findViewById(R.id.fCategory_rvPostList);
        tvNothing = (TextView) view.findViewById(R.id.fCategory_tvNothing);
        btnAll = (MaterialButton) view.findViewById(R.id.fCategory_btnAll);
        btnFood = (MaterialButton) view.findViewById(R.id.fCategory_btnFood);
        btnCheckin = (MaterialButton) view.findViewById(R.id.fCategory_btnCheckin);
        btnHotel = (MaterialButton) view.findViewById(R.id.fCategory_btnHotel);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fCategory_refreshLayout);
        tvNothing.setVisibility(View.GONE);
        helper_common.configRecycleView(getContext(), rv_PostList);
        helper_common.configAnimBottomNavigation(getContext(), rv_PostList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showPost();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "";
                showPost();
            }
        });

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "food";
                showPost();
            }
        });

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "check_in";
                showPost();
            }
        });

        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "hotel";
                showPost();
            }
        });
        showPost();

    }

    private void showPost() {
        setColorButton();
        helper_common.showSkeleton(rv_PostList, adapter_rv_post, R.layout.row_skeleton_post);
        dao_post.getPostByCategory(category, new Helper_Callback() {
            @Override
            public List<User> successReq(Object response) {
                postList = (List<Post>) response;
                refreshLV();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                return null;
            }

            @Override
            public void failedReq(String msg) {
                refreshLV();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setColorButton() {
        switch (category) {
            case "":
                setActiveButton(btnAll);
                setInactiveButton(btnFood);
                setInactiveButton(btnHotel);
                setInactiveButton(btnCheckin);
                break;

            case "food":
                setActiveButton(btnFood);
                setInactiveButton(btnAll);
                setInactiveButton(btnHotel);
                setInactiveButton(btnCheckin);
                break;
            case "hotel":
                setActiveButton(btnHotel);
                setInactiveButton(btnAll);
                setInactiveButton(btnFood);
                setInactiveButton(btnCheckin);
                break;
            case "check_in":
                setActiveButton(btnCheckin);
                setInactiveButton(btnAll);
                setInactiveButton(btnFood);
                setInactiveButton(btnHotel);
                break;
        }
    }

    private void setActiveButton(MaterialButton materialButton) {
        @SuppressLint("ResourceType")
        ColorStateList activeColor = ColorStateList
                .valueOf(Color.parseColor(getResources().getString(R.color.colorPrimary)));

        materialButton.setTextColor(activeColor);
        materialButton.setStrokeColor(activeColor);
    }

    private void setInactiveButton(MaterialButton materialButton) {
        @SuppressLint("ResourceType")
        ColorStateList inactiveColor = ColorStateList
                .valueOf(Color.parseColor(getResources().getString(R.color.inactive_button)));
        materialButton.setTextColor(inactiveColor);
        materialButton.setStrokeColor(inactiveColor);
    }

    private void refreshLV() {
        adapter_rv_post = new Adapter_RV_Post(getContext(), postList);
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