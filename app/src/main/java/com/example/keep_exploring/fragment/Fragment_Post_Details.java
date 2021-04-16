package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Comment;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Post_Details extends Fragment {
    //    View
    private View view;
    private DAO_Post dao_post;
    private CircleImageView civUser;
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes;
    private RecyclerView dUserLike_rcUserList;
    private ImageView imgLike, imgComment;

    //    DAO & Helper
    private DAO_Comment dao_comment;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_Image helper_image;
    //    Variable
    private TextView dUserLike_tvCancel;
    private Post post;


    public Fragment_Post_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_details, container, false);

        post = new Post();
        Bundle bundle = getArguments();
        if (bundle != null) {
            post = (Post) bundle.getSerializable("post");
        }
        initView();
        Dialog_Fragment_Comment dialog_fragment_comment = new Dialog_Fragment_Comment();
        dialog_fragment_comment.show(getChildFragmentManager(),dialog_fragment_comment.getTag());
 //        showPost();
        return view;
    }

    private void initView() {
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_image = new Helper_Image(getContext());
        dao_comment = new DAO_Comment(getContext());
        civUser = (CircleImageView) view.findViewById(R.id.fDetailPost_civAvatarAdmin);
        tvDate = (TextView) view.findViewById(R.id.fDetailPost_tvPubDate);
        tvDesc = (TextView) view.findViewById(R.id.fDetailPost_tvDescription);
        tvLikes = (TextView) view.findViewById(R.id.fDetailPost_tvLikes);
        tvTitle = (TextView) view.findViewById(R.id.fDetailPost_tvTitle);
        tvUserName = (TextView) view.findViewById(R.id.fDetailPost_tvUserName);
        imgComment = (ImageView) view.findViewById(R.id.fDetailPost_imgComment);
        imgLike = (ImageView) view.findViewById(R.id.fDetailPost_imgLike);
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialogComment();
            }
        });
    }

    private void showPost() {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(civUser);
        tvDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        tvLikes.setText(post.getLikes().size() + " lượt thích");
    }
    private void dialogUserLiked() {
        final Dialog dialogUserLike = new Dialog(getActivity());
        dialogUserLike.setContentView(R.layout.dialog_user_like);
        dialogUserLike.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //anh xa
        dUserLike_tvCancel = (TextView) dialogUserLike.findViewById(R.id.dUserLike_tvCancel);
        dUserLike_rcUserList = (RecyclerView) dialogUserLike.findViewById(R.id.dUserLike_rcUserList);

        //recycle
        helper_common.configRecycleView(getContext(), dUserLike_rcUserList);

        //fetch data from server
        dao_post = new DAO_Post(getContext());
        dao_post.getLikeByPost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {

            }

            @Override
            public void failedReq(String msg) {

            }
        });


        dUserLike_tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUserLike.dismiss();
            }
        });

        dialogUserLike.show();
    }

    private void log(String s) {
        Log.d("log", s);
    }
}