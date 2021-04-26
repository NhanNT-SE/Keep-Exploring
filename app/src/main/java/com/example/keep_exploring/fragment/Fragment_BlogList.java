package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Blog;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.RxSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_BlogList extends Fragment {
    //    View
    private View view;
    private RecyclerView rvBlog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNothing;
    private SearchView searchView;
    //    DAO & Helper
    private DAO_Blog dao_blog;
    private Helper_Common helper_common;
    //    Variable
    private Adapter_RV_Blog adapterBlog;
    private List<Blog> blogList;

    public Fragment_BlogList() {
        // Required empty public constructor
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        rvBlog = (RecyclerView) view.findViewById(R.id.fBlogList_rvBlog);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fBlogList_refreshLayout);
        tvNothing = (TextView) view.findViewById(R.id.fBlogList_tvNothing);
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
                blogList.clear();
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

    private void getBlogByQuery(String query) {
        helper_common.showSkeleton(rvBlog, adapterBlog, R.layout.row_skeleton_blog);
        dao_blog.getBlogByQuery(query, new Helper_Callback() {
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
                        .subscribe(query->getBlogByQuery(query));
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

}
