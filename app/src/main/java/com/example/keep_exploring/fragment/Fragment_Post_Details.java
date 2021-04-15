package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_PostUser;
import com.example.keep_exploring.adapter.Adapter_RV_Comment;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Post_Details extends Fragment {

    private View view;
    private DAO_Post dao_post;
    private CircleImageView civUser;
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes;
    private ImageView imgLike, imgComment;
    private Post post;
    private Helper_Common helper_common;

    private TextView dComment_tvDone, dComment_tvNothing;
    private EditText dComment_etComment;
    private ImageView dComment_imgSend;
    private RecyclerView dComment_rvCommentList;
    private Adapter_RV_Comment adapter_rv_comment;
    private DAO_Comment dao_comment;
    private List<Comment> commentList = new ArrayList<>();

    public Fragment_Post_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_details, container, false);

        Bundle bundle = getArguments();
        post = (Post) bundle.getSerializable("post");

        initView();
        showPost();
        return view;
    }

    private void initView() {
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
                showDialogComment();
            }
        });
    }

    private void showPost() {
        helper_common = new Helper_Common();
        String URL_IMAGE = helper_common.getBaseUrlImage();

        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(civUser);
        String dateFormated = post.getCreated_on().substring(0, 10);
        tvDate.setText(dateFormated);
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        tvLikes.setText(post.getLikes().size() + " lượt thích");
    }

    private void showDialogComment() {
        final Dialog dialogComment = new Dialog(getContext());
        dialogComment.setContentView(R.layout.dialog_comment);
        dialogComment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dComment_etComment = (EditText) dialogComment.findViewById(R.id.dComment_etComment);
        dComment_imgSend = (ImageView) dialogComment.findViewById(R.id.dComment_imgPost);
        dComment_tvNothing = (TextView) dialogComment.findViewById(R.id.dComment_tvNothing);
        dComment_tvDone = (TextView) dialogComment.findViewById(R.id.dComment_tvDone);
        dComment_rvCommentList = (RecyclerView) dialogComment.findViewById(R.id.dComment_rvComment);

        //recycle
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        dComment_rvCommentList.setLayoutManager(layoutManager);
        dComment_rvCommentList.addItemDecoration(decoration);


        showCommentList();


        dComment_tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComment.dismiss();
            }
        });

        dialogComment.show();

    }

    private void showCommentList() {
        dao_comment = new DAO_Comment(getContext());
        dao_comment.getCommentPost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Comment> list = (List<Comment>) response;
                               rfCommentList(list);
            }

            @Override
            public void failedReq(String msg) {
                log(msg);
            }
        });
    }

    private void rfCommentList(List<Comment> comment) {
        commentList = comment;
        adapter_rv_comment = new Adapter_RV_Comment(getContext(), commentList);
        dComment_rvCommentList.setAdapter(adapter_rv_comment);

        if (commentList.size() > 0) {
            dComment_tvNothing.setVisibility(View.GONE);
        } else {
            dComment_tvNothing.setVisibility(View.VISIBLE);
        }

    }

    private void log(String s) {
        Log.d("log", s);
    }
}