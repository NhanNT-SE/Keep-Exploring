package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Blog;
import com.example.keep_exploring.adapter.Adapter_RV_ProfileBlog;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_BlogList extends Fragment {
    //    View
    private View view;
    private RecyclerView rvBlog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNothing;
    //    DAO & Helper
    private DAO_Blog dao_blog;
    private Helper_Common helper_common;
    //    Variable
    private Adapter_RV_Blog adapterBlog;
    private List<Blog> blogList;
    public Fragment_BlogList() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        rvBlog = (RecyclerView) view.findViewById(R.id.fBlogList_rvBlog);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fBlogList_refreshLayout);
        tvNothing = (TextView) view.findViewById(R.id.fBlogList_tvNothing);
        initVariable();
        handlerEvent();
        return view;
    }
    private void initVariable() {
        dao_blog = new DAO_Blog(getContext());
        helper_common = new Helper_Common();
        blogList = new ArrayList<>();
    }
    private void handlerEvent() {
        helper_common.configRecycleView(getContext(), rvBlog);
        helper_common.configAnimBottomNavigation(getContext(), rvBlog);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        helper_common.showSkeleton(rvBlog, adapterBlog, R.layout.row_skeleton_blog);
        dao_blog.getBlogList(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                blogList = (List<Blog>) response;
                refreshRV();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failedReq(String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void refreshRV() {
        adapterBlog = new Adapter_RV_Blog(getContext(), blogList);
        rvBlog.setAdapter(adapterBlog);
        if (blogList.size() > 0) {
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
        if (item.getItemId() == R.id.menu_search_refresh){
            loadData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    private void log(String s) {
        Log.d("log", s);
    }
}
