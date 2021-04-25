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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.RxSearch;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class Fragment_Category extends Fragment {
    //    View
    private View view;
    private TextView tvNothing;
    private MaterialButton btnAll, btnFood, btnCheck_in, btnHotel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_PostList;
    private SpotsDialog spotsDialog;
    private SearchView searchView;
    //    DAO & Helper
    private DAO_Post dao_post;
    private DAO_Auth dao_auth;
    private Helper_Common helper_common;
    //    Variable
    private String category;
    private List<Post> postList;
    private Adapter_RV_Post adapter_rv_post;
    private ColorStateList colorStateList;

    public Fragment_Category() {
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

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
        spotsDialog = new SpotsDialog(getContext());
        rv_PostList = (RecyclerView) view.findViewById(R.id.fCategory_rvPostList);
        tvNothing = (TextView) view.findViewById(R.id.fCategory_tvNothing);
        btnAll = (MaterialButton) view.findViewById(R.id.fCategory_btnAll);
        btnFood = (MaterialButton) view.findViewById(R.id.fCategory_btnFood);
        btnCheck_in = (MaterialButton) view.findViewById(R.id.fCategory_btnCheckin);
        btnHotel = (MaterialButton) view.findViewById(R.id.fCategory_btnHotel);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fCategory_refreshLayout);
    }

    @SuppressLint("ResourceType")
    private void initVariable() {
        dao_post = new DAO_Post(getContext());
        dao_auth = new DAO_Auth(getContext());
        helper_common = new Helper_Common();
        helper_common.configRecycleView(getContext(), rv_PostList);
        helper_common.configAnimBottomNavigation(getContext(), rv_PostList);
        helper_common.toggleBottomNavigation(getContext(), true);
        colorStateList = ColorStateList
                .valueOf(Color.parseColor(getResources().getString(R.color.colorPrimary)));
        postList = new ArrayList<>();
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
                postList.clear();
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

    private void getPostByQuery(String query) {
        helper_common.showSkeleton(rv_PostList, adapter_rv_post, R.layout.row_skeleton_post);
        dao_post.getPostByQuery(query, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                postList.clear();
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
        materialButton.setTextColor(Color.parseColor("#FFFFFF"));
        materialButton.setStrokeColor(colorStateList);
        materialButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
    }

    private void setInactiveButton(MaterialButton materialButton) {
        materialButton.setTextColor(Color.parseColor("#366577"));
        materialButton.setStrokeColor(colorStateList);
        materialButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
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


    @SuppressLint({"RestrictedApi", "CheckResult"})
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_places, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_search_places);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint("Điền từ khóa đề tìm kiếm");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RxSearch.fromSearchView(searchView)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(query -> getPostByQuery(query));
                return false;
            }


        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

    private void log(String s) {
        Log.d("log", s);
    }

}