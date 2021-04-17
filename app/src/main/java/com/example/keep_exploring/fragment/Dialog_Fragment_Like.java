package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Like;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Like;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Fragment_Like extends DialogFragment {
    //    View
    private View view;
    private RecyclerView rvLike;
    private TextView tvNothing, tvClose;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    DAO & Helper
    private DAO_Like dao_like;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    //    Variable
    private User user;
    private List<User> userList;
    private String id, type;
    private Adapter_RV_Like adapterLike;


    public static Dialog_Fragment_Like newInstance(String id, String type) {
        Dialog_Fragment_Like dialog = new Dialog_Fragment_Like();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("type", type);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_like, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            type = bundle.getString("type");
            loadData();
        }
    }

    private void initView() {
        rvLike = (RecyclerView) view.findViewById(R.id.dLike_rvLike);
        tvNothing = (TextView) view.findViewById(R.id.dLike_tvNothing);
        tvClose = (TextView) view.findViewById(R.id.dLike_tvClose);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dLike_refreshLayout);
    }

    private void initVariable() {
        dao_like = new DAO_Like(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(getContext());
        userList = new ArrayList<>();
    }

    private void handlerEvent() {
        helper_common.configRecycleView(getContext(), rvLike);
        helper_common.showSkeleton(rvLike, adapterLike, R.layout.row_skeleton_like);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void loadData() {
        dao_like.getLikeList(id, type, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                userList = (List<User>) response;
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
        adapterLike = new Adapter_RV_Like(getContext(), userList);
        rvLike.setAdapter(adapterLike);
        if (userList.size() > 0) {
            tvNothing.setVisibility(View.GONE);
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getTheme() {
        return R.style.DialogCommentTheme;
    }

    private void log(String s) {
        Log.d("log", s);
    }
}
