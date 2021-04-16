package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Notification;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Notify;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Notification;
import com.example.keep_exploring.model.User;
import com.google.android.material.button.MaterialButton;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Fragment_Notification extends Fragment {
    //    View
    private View view;
    private RecyclerView rvNotify;
    private TextView tvNothing;
    private MaterialButton mBtnAll, mBtnSeen, mBtnNew;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    DAO & Helper
    private DAO_Notification dao_notification;
    private Helper_Common helper_common;
    // Variable
    private List<Notification> defaultList, filterList;
    private int total, totalSeen, totalNew;
    private Adapter_RV_Notify adapterNotify;
    private String filter;

    public Fragment_Notification() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        rvNotify = (RecyclerView) view.findViewById(R.id.fNotification_rvNotify);
        tvNothing = (TextView) view.findViewById(R.id.fNotification_tvNothing);
        mBtnAll = (MaterialButton) view.findViewById(R.id.fNotification_mBtnAll);
        mBtnNew = (MaterialButton) view.findViewById(R.id.fNotification_mBtnNew);
        mBtnSeen = (MaterialButton) view.findViewById(R.id.fNotification_mBtnSeen);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fNotification_refreshLayout);
    }

    private void initVariable() {
        filter = "";
        helper_common = new Helper_Common();
        dao_notification = new DAO_Notification(getContext());
        defaultList = new ArrayList<>();
        filterList = new ArrayList<>();
    }

    private void handlerEvent() {
        helper_common.configRecycleView(getContext(), rvNotify);
        helper_common.configAnimBottomNavigation(getContext(), rvNotify);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = "";
                setFilterList();
            }
        });
        mBtnSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = "seen";
                setFilterList();
            }
        });
        mBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = "new";
                setFilterList();
            }
        });
        loadData();
        changeSeenStatusNotify();
    }

    private void loadData() {
        helper_common.showSkeleton(rvNotify, adapterNotify, R.layout.row_skeleton_notify);
        dao_notification.getAll(new Helper_Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public List<User> successReq(Object response) {
                defaultList = (List<Notification>) response;
                setBadgeMButton();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                setFilterList();

                return null;
            }

            @Override
            public void failedReq(String msg) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setFilterList() {
        setColorButton();
        filterList.clear();
        if (filter.equals("")) {
            filterList.addAll(defaultList);
        } else {
            filterList = defaultList.stream().
                    filter(item -> item.getStatus().equals(filter))
                    .collect(Collectors.toList());
            refreshRV(filterList);
        }
        refreshRV(filterList);
    }

    private void refreshRV(List<Notification> notificationList) {
        adapterNotify = new Adapter_RV_Notify(getContext(), notificationList);
        rvNotify.setAdapter(adapterNotify);
        if (notificationList.size() > 0) {
            tvNothing.setVisibility(View.GONE);
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }
    }

    private void setBadgeMButton() {
        totalSeen = (int) defaultList.stream().filter(item -> item.getStatus().equals("seen")).count();
        totalNew = (int) defaultList.stream().filter(item -> item.getStatus().equals("new")).count();
        total = defaultList.size();
        mBtnAll.setText("Tất cả thông báo(" + total + ")");
        mBtnNew.setText("Thông báo chưa đọc(" + totalNew + ")");
        mBtnSeen.setText("Thông báo đã đọc(" + totalSeen + ")");
    }

    private void setColorButton() {
        switch (filter) {
            case "":
                setActiveButton(mBtnAll);
                setInactiveButton(mBtnNew);
                setInactiveButton(mBtnSeen);
                break;
            case "new":
                setActiveButton(mBtnNew);
                setInactiveButton(mBtnAll);
                setInactiveButton(mBtnSeen);
                break;
            case "seen":
                setActiveButton(mBtnSeen);
                setInactiveButton(mBtnAll);
                setInactiveButton(mBtnNew);
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

    private void changeSeenStatusNotify() {
        dao_notification.changeSeenStatusNotify(new Helper_Callback() {
            @Override
            public List<User> successReq(Object response) {
                return null;
            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void log(String msg) {
        Log.d("log", msg);
    }
}