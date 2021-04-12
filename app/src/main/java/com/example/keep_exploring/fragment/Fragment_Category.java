package com.example.keep_exploring.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_PostUser;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.model.Post;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Category extends Fragment {
    private View view;
    private RecyclerView rv_PostList;
    private Adapter_LV_PostUser adapter_lv_post;
    private DAO_Post dao_post;
    private String category;
    private List<Post> listPost;
    private TextView tvNothing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__category, container, false);
        init();
        showPost();
        // Inflate the layout for this fragment
        return view;
    }

    private void init() {
        rv_PostList = (RecyclerView) view.findViewById(R.id.fCategory_rvPostList);
        tvNothing = (TextView) view.findViewById(R.id.fCategory_tvNothing);

        //get Category
        Bundle bundle = getArguments();
        category = bundle.getString("category");
        log("category: " + category);
        //setup RecycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());

        rv_PostList.setLayoutManager(layoutManager);
        rv_PostList.addItemDecoration(decoration);
    }

    private void showPost() {
        dao_post = new DAO_Post(view.getContext());
        dao_post.getPostByCategory(category, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Post> postList = (List<Post>) response;
//                log(postList.toString());
                refreshLV(postList);
            }

            @Override
            public void failedReq(String msg) {
                Log.d("TAG", "failedReq: " + msg);
            }
        });
    }

    private void refreshLV(List<Post> postList) {
        listPost = new ArrayList<>(postList);
        adapter_lv_post = new Adapter_LV_PostUser(getContext(), listPost);
        log(listPost.toString());
        rv_PostList.setAdapter(adapter_lv_post);
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