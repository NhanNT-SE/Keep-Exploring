package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_ProfileBlog;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile_BlogList extends Fragment {
    //    View
    private View view;
    private RecyclerView rvBlog;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    DAO & Helper
    private DAO_Auth dao_auth;
    private DAO_Blog dao_blog;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    //    Variable
    private Adapter_RV_ProfileBlog adapterBlog;
    private List<Blog> blogList;
    private String idUser;
    private Bundle bundle;

    public Fragment_Profile_BlogList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_blog_list, container, false);
        rvBlog = (RecyclerView) view.findViewById(R.id.fProfileBlogList_rvBlog);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fProfileBlogList_refreshLayout);
        initVariable();
        handlerEvent();
        return view;
    }
    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        bundle = getParentFragment().getArguments();
        blogList = new ArrayList<>();
    }

    private void handlerEvent() {
        helper_common.configRecycleView(getContext(), rvBlog);
        helper_common.configAnimBottomNavigation(getContext(), rvBlog);
        helper_common.showSkeleton(rvBlog,adapterBlog,R.layout.row_skeleton_blog);
        if (bundle != null) {
            idUser = bundle.getString("idUser");
        } else {
            idUser = helper_sp.getUser().getId();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        dao_blog.getBlogByUser(helper_sp.getAccessToken(), idUser, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                blogList = (List<Blog>) response;
                refreshRV();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failedReq(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken();
                } else {
                    helper_common.logOut(getContext());
                }
            }
        });
    }


    private void refreshRV() {
        adapterBlog = new Adapter_RV_ProfileBlog(getContext(), blogList);
        rvBlog.setAdapter(adapterBlog);
    }

    private void refreshToken() {
        dao_auth.refreshToken(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                loadData();
            }
            @Override
            public void failedReq(String msg) {
                helper_common.logOut(getContext());
            }
        });
    }



}
