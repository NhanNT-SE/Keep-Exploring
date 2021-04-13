package com.example.keep_exploring.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_User;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserInfo extends Fragment {
    //View
    private View view;
    //DAO & Helper
    private DAO_User dao_user;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    //Variable
    private User user;
    private String idUser;

    public Fragment_UserInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_infor, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }
    private void initView() {

    }
    private void initVariable() {

    }
    private void handlerEvent() {

    }
    private void showInfo(){
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    private void log(String s) {
        Log.d("log", s);
    }
}
