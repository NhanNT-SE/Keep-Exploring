package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Category extends Fragment {
    //    View
    private View view;
    private TextView tvNothing;
    private MaterialButton btnAll, btnFood, btnCheck_in, btnHotel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_PostList;
    //    DAO & Helper
    private DAO_Post dao_post;
    private Helper_Common helper_common;
    //    Variable
    private String category;
    private List<Post> postList;
    private Adapter_RV_Post adapter_rv_post;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        rv_PostList = (RecyclerView) view.findViewById(R.id.fCategory_rvPostList);
        tvNothing = (TextView) view.findViewById(R.id.fCategory_tvNothing);
        btnAll = (MaterialButton) view.findViewById(R.id.fCategory_btnAll);
        btnFood = (MaterialButton) view.findViewById(R.id.fCategory_btnFood);
        btnCheck_in = (MaterialButton) view.findViewById(R.id.fCategory_btnCheckin);
        btnHotel = (MaterialButton) view.findViewById(R.id.fCategory_btnHotel);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fCategory_refreshLayout);
    }

    private void initVariable() {
        helper_common = new Helper_Common();
        dao_post = new DAO_Post(getContext());
        helper_common.toggleBottomNavigation(getContext(), true);
        postList = new ArrayList<>();
        helper_common.configRecycleView(getContext(), rv_PostList);
        helper_common.configAnimBottomNavigation(getContext(), rv_PostList);
        category = "";

    }

    private void handlerEvent() {
        tvNothing.setVisibility(View.GONE);
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

        btnCheck_in.setOnClickListener(new View.OnClickListener() {
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
            public void successReq(Object response) {
                postList = (List<Post>) response;
                refreshLV();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void failedReq(String msg) {
                refreshLV();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setColorButton() {
        switch (category) {
            case "":
                setActiveButton(btnAll);
                setInactiveButton(btnFood);
                setInactiveButton(btnHotel);
                setInactiveButton(btnCheck_in);
                break;

            case "food":
                setActiveButton(btnFood);
                setInactiveButton(btnAll);
                setInactiveButton(btnHotel);
                setInactiveButton(btnCheck_in);
                break;
            case "hotel":
                setActiveButton(btnHotel);
                setInactiveButton(btnAll);
                setInactiveButton(btnFood);
                setInactiveButton(btnCheck_in);
                break;
            case "check_in":
                setActiveButton(btnCheck_in);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_places, menu);
        MenuItem search = menu.findItem(R.id.menu_search_places);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Please, enter a location");
        final SearchView.SearchAutoComplete autoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoComplete.setTextColor(Color.WHITE);
        autoComplete.setDropDownBackgroundResource(android.R.color.white);
        autoComplete.setThreshold(1);
        MenuItem refresh = menu.findItem(R.id.menu_search_refresh);
        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search_refresh) {
            category = "";
            showPost();
        }
        return super.onOptionsItemSelected(item);
    }

    private void log(String s) {
        Log.d("log", s);
    }

}