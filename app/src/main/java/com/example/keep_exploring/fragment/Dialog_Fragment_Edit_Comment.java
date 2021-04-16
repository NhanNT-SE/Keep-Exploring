package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Comment;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.User;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Fragment_Edit_Comment extends DialogFragment {
    //    View
    private View view;
    private RelativeLayout layoutImage;
    private EditText etContent;
    private ImageView imgComment, imgDeleteImage;
    private ImageView imgSend, imgCamera;
    //    DAO & Helper
    private DAO_Comment dao_comment;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    //    Variable
    private static final int PICK_IMAGE_CODE = 1;
    private String path;
    private Uri uriImage;

    public static Dialog_Fragment_Edit_Comment newInstance(Comment comment) {
        Dialog_Fragment_Edit_Comment dialogFragment = new Dialog_Fragment_Edit_Comment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("comment",comment);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_comment, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle !=null){
            Comment comment = (Comment) bundle.getSerializable("comment");
            Log.d("log", "onCreate: " + comment.toString());
        }

    }
    private void initView() {
        layoutImage = (RelativeLayout) view.findViewById(R.id.dComment_layoutImage);
        etContent = (EditText) view.findViewById(R.id.dComment_etComment);
        imgSend = (ImageView) view.findViewById(R.id.dComment_imgSend);
        imgCamera = (ImageView) view.findViewById(R.id.dComment_imgCamera);
        imgComment = (ImageView) view.findViewById(R.id.dComment_imgComment);
        imgDeleteImage = (ImageView) view.findViewById(R.id.dComment_imgDeleteImage);


    }

    private void initVariable() {
        dao_comment = new DAO_Comment(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(getContext());
        path = "";
    }

    private void handlerEvent() {

        imgDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path = "";
                layoutImage.setVisibility(View.GONE);
            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_CODE);
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();

            }
        });
//        imgSend.setEnabled(false);
//        imgSend.setImageResource(R.drawable.ic_comment_send_disbled);
//        etContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().isEmpty()){
//                    imgSend.setEnabled(false);
//                    imgSend.setImageResource(R.drawable.ic_comment_send_disbled);
//                }else {
//                    imgSend.setEnabled(true);
//                    imgSend.setImageResource(R.drawable.ic_comment_send);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data.getData() != null) {
            uriImage = data.getData();
            imgComment.setImageURI(uriImage);
            path = helper_image.getPathFromUri(uriImage);
            layoutImage.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void log(String s) {
        Log.d("log", s);
    }
}
