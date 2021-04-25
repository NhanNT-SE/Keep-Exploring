package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_ProfilePost;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile_PostList extends Fragment {
    //    View
    private View view;
    private RecyclerView rvPost;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    DAO & Helper
    private DAO_Post dao_post;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    private DAO_Auth dao_auth;
    //    Variable
    private Adapter_RV_ProfilePost adapterPost;
    private List<Post> listPost;
    private String idUser;
    private Bundle bundle;
    public Fragment_Profile_PostList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_post_list, container, false);
        rvPost = (RecyclerView) view.findViewById(R.id.fProfilePostList_rvPost);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fProfilePostList_refreshLayout);
        initVariable();
        handlerEvent();
        return view;
    }
    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_post = new DAO_Post(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        listPost = new ArrayList<>();
        bundle = getParentFragment().getArguments();
    }
    private void handlerEvent(){
        helper_common.configRecycleView(getContext(), rvPost);
        helper_common.configAnimBottomNavigation(getContext(), rvPost);
        helper_common.showSkeleton(rvPost,adapterPost,R.layout.row_skeleton_post);
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
        dao_post.getPostByUser(helper_sp.getAccessToken(), idUser, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                listPost = (List<Post>) response;
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
        adapterPost = new Adapter_RV_ProfilePost(getContext(), listPost);
        rvPost.setAdapter(adapterPost);
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
