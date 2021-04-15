package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Notification;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.model.Notification;

import java.util.List;


public class Fragment_Notification extends Fragment {
    View view;
    private DAO_Notification dao_notification;
    private List<Notification> defaultList, filterList;
    private int total, totalSeen, totalNew;

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

    }

    private void initVariable() {
        dao_notification = new DAO_Notification(getContext());
    }

    private void handlerEvent() {
        loadData();
    }

    private void loadData() {
        dao_notification.getAll(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                defaultList = (List<Notification>) response;
                totalSeen = (int) defaultList.stream().filter(item -> item.getStatus().equals("seen")).count();
                totalNew = (int) defaultList.stream().filter(item -> item.getStatus().equals("new")).count();
                total = defaultList.size();
                log("total: " + total);
                log("seen: " + totalSeen);
                log("seen: " + totalNew);

            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void refreshRV() {

    }

    private void log(String msg) {
        Log.d("log", msg);
    }
}