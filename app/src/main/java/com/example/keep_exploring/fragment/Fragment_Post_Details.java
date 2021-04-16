package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Comment;
import com.example.keep_exploring.adapter.Adapter_UserLikeList;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Post_Details extends Fragment {

    private View view;
    private DAO_Post dao_post;
    private CircleImageView civUser;
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes;
    private ImageView imgLike, imgComment;
    private Post post;
    private Helper_Common helper_common = new Helper_Common();
    private Helper_SP helper_sp;
    private Boolean isLogIn = false;

    private TextView dComment_tvDone, dComment_tvNothing;
    private EditText dComment_etComment;
    private ImageView dComment_imgSend;
    private RecyclerView dComment_rvCommentList;
    private Adapter_RV_Comment adapter_rv_comment;
    private DAO_Comment dao_comment;
    private List<Comment> commentList = new ArrayList<>();

    private RecyclerView dUserLike_rcUserList;
    private TextView dUserLike_tvCancel, dUserLike_tvNothing;
    private Adapter_UserLikeList adapter_userLikeList;
    private List<User> userLikeList = new ArrayList<>();
    private boolean isLike = false;
    private int sizeList = 0;


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
        helper_sp = new Helper_SP(getContext());


        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });

        tvLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUserLiked();
            }
        });
    }

    private void showPost() {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        sizeList = post.getLikes().size();


        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(civUser);
        String dateFormated = post.getCreated_on().substring(0, 10);
        tvDate.setText(dateFormated);
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        tvLikes.setText(sizeList + " lượt thích");

        getLikeList();

//        log("user like list: " + userLikeList.toString());
        isLogIn = checkLogin();
        if (isLogIn) {
            String idUser = helper_sp.getUser().getId();
            for (User user : userLikeList) {
                if (user.getId().equalsIgnoreCase(idUser)) {
                    isLike = true;
                    checkLike();
                    break;
                }
            }

            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likePost();
                }
            });
        } else {
            isLike = false;
            checkLike();
            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Bạn cần đăng nhập để thực hiện thao tác này", Toast.LENGTH_SHORT).show();
                }
            });
        }


        checkLike();
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
        helper_common.configRecycleView(getContext(), dComment_rvCommentList);
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

    private void dialogUserLiked() {
        final Dialog dialogUserLike = new Dialog(getActivity());
        dialogUserLike.setContentView(R.layout.dialog_user_like);
        dialogUserLike.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        log("show dialog: " + userLikeList.toString());

        //anh xa
        dUserLike_tvCancel = (TextView) dialogUserLike.findViewById(R.id.dUserLike_tvCancel);
        dUserLike_rcUserList = (RecyclerView) dialogUserLike.findViewById(R.id.dUserLike_rcUserList);
        dUserLike_tvNothing = (TextView) dialogUserLike.findViewById(R.id.dUserLike_tvNothing);

        //recycle
        helper_common.configRecycleView(getContext(), dUserLike_rcUserList);
        refreshRLike();

        dUserLike_tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUserLike.dismiss();
            }
        });

        dialogUserLike.show();
    }

    private void getLikeList() {
        //fetch data from server
        dao_post = new DAO_Post(getContext());

        dao_post.getLikeByPost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<User> likeList = (List<User>) response;
                userLikeList = likeList;
                String idUSer = helper_sp.getUser().getId();
                List<User> checkList = userLikeList.stream()
                        .filter(item -> item.getId().equalsIgnoreCase(idUSer))
                        .collect(Collectors.toList());
                if (checkList.size() > 0) {
                    Log.d("TAG", "da like");
                } else {
                    Log.d("TAG", "chua like");

                }
            }

            @Override
            public void failedReq(String msg) {
                log(msg);
            }
        });
    }

    private void likePost() {
        dao_post.likePost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                if (response.equals("Đã thích bài viết")) {
                    isLike = true;
                    sizeList += 1;
                    tvLikes.setText(sizeList + " lượt thích");
                    Toast.makeText(getContext(), "Đã thích bài viết", Toast.LENGTH_SHORT).show();
                } else {
                    isLike = false;
                    sizeList -= 1;
                    tvLikes.setText(sizeList + " lượt thích");
                    Toast.makeText(getContext(), "Đã bỏ thích bài viết", Toast.LENGTH_SHORT).show();

                }
                checkLike();

            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void checkLike() {
        if (isLike == true) {
            imgLike.setImageResource(R.drawable.heart_liked);
        } else {
            imgLike.setImageResource(R.drawable.heart_outline_90px);
        }

    }

    private void refreshRLike() {
        adapter_userLikeList = new Adapter_UserLikeList(getContext(), userLikeList);
        dUserLike_rcUserList.setAdapter(adapter_userLikeList);
        if (userLikeList.size() > 0) {
            dUserLike_tvNothing.setVisibility(View.GONE);
        } else {
            dUserLike_tvNothing.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkLogin() {
        String accessToken = helper_sp.getAccessToken();
        log(accessToken);
        if (accessToken.isEmpty()) {
            return false;
        }
        return true;

    }

    private void log(String s) {
        Log.d("log", s);
    }
}