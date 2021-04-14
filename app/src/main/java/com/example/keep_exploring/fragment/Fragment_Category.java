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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
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
    private List<Post> postList;
    private TextView tvNothing;
    private Button btnAll, btnFood, btnCheckin, btnHotel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SkeletonScreen skeletonScreen;

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
        btnAll = (Button) view.findViewById(R.id.fCategory_btnAll);
        btnFood = (Button) view.findViewById(R.id.fCategory_btnFood);
        btnCheckin = (Button) view.findViewById(R.id.fCategory_btnCheckin);
        btnHotel = (Button) view.findViewById(R.id.fCategory_btnHotel);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fCategory_refreshLayout);
        tvNothing.setVisibility(View.GONE);
        helper_common.configRecycleView(getContext(), rv_PostList);
        helper_common.configAnimBottomNavigation(getContext(), rv_PostList);
        skeletonScreen = Skeleton.bind(rv_PostList)
                .adapter(adapter_rv_post)
                .load(R.layout.row_sekeleton_post)
                .show();

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
        dao_post.getPostByCategory(category, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                postList = (List<Post>) response;
                refreshLV();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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