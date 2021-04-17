package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.keep_exploring.DAO.DAO_Like;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Fragment_Post_Details extends Fragment {
    //    View
    private View view;
    private CircleImageView civUser;
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes, tvComments;
    private ImageView imgLike, imgComment, imgRefresh;
    private ImageSlider sliderPost;
    private SpotsDialog spotsDialog;
    private RatingBar ratingBar;
    //    DAO & Helper
    private DAO_Post dao_post;
    private DAO_Like dao_like;
    private Helper_Image helper_image;
    private Helper_Common helper_common = new Helper_Common();
    private Helper_Date helper_date = new Helper_Date();
    private Helper_SP helper_sp;
    //    Variable
    private Dialog_Fragment_Comment dialog_fragment_comment;
    private Dialog_Fragment_Like dialogFragmentLike;
    private boolean isLike;
    private String idPost;
    private User user;
    private int numLike;

    public Fragment_Post_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }
    private void initView() {
        spotsDialog = new SpotsDialog(getActivity());
        civUser = (CircleImageView) view.findViewById(R.id.fDetailPost_civAvatarAdmin);
        tvDate = (TextView) view.findViewById(R.id.fDetailPost_tvPubDate);
        tvDesc = (TextView) view.findViewById(R.id.fDetailPost_tvDescription);
        tvLikes = (TextView) view.findViewById(R.id.fDetailPost_tvLikes);
        tvComments = (TextView) view.findViewById(R.id.fDetailPost_tvComments);
        tvTitle = (TextView) view.findViewById(R.id.fDetailPost_tvTitle);
        tvUserName = (TextView) view.findViewById(R.id.fDetailPost_tvUserName);
        imgComment = (ImageView) view.findViewById(R.id.fDetailPost_imgComment);
        imgLike = (ImageView) view.findViewById(R.id.fDetailPost_imgLike);
        imgRefresh = (ImageView) view.findViewById(R.id.fDetailPost_imgRefresh);
        sliderPost = (ImageSlider) view.findViewById(R.id.fDetailPost_sliderPost);
        ratingBar = (RatingBar) view.findViewById(R.id.fDetailPost_ratingBar);
    }

    private void initVariable() {
        dao_post = new DAO_Post(getContext());
        dao_like = new DAO_Like(getContext());
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_image = new Helper_Image(getContext());
        helper_sp = new Helper_SP(getContext());
        user = helper_sp.getUser();
        numLike = 0;
        idPost = "6075c0fb6e5a5e20e8241100";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idPost = bundle.getString("idPost");
        }
    }

    private void handlerEvent() {
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_fragment_comment = Dialog_Fragment_Comment.newInstance(idPost, "post");
                dialog_fragment_comment.show(getChildFragmentManager(), dialog_fragment_comment.getTag());
            }
        });
        tvLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragmentLike = Dialog_Fragment_Like.newInstance(idPost, "post");
                dialogFragmentLike.show(getChildFragmentManager(), dialogFragmentLike.getTag());
            }
        });
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLike = !isLike;
                toggleLike();
                setLike();
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadData();
    }

    private void loadData() {
        spotsDialog.show();
        dao_post.getPostById(idPost, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                Post post = (Post) response;
                List<String> likeList = post.getLikes();
                displayInfo(post);
                numLike = likeList.size();
                if (user != null) {
                    int checkLike = (int) likeList.stream().filter(e -> e.equals(user.getId())).count();
                    isLike = checkLike == 1;
                    toggleLike();
                }
                spotsDialog.dismiss();

            }

            @Override
            public void failedReq(String msg) {
                spotsDialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayInfo(Post post) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(civUser);
        tvDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        tvLikes.setText(post.getLikes().size() + " lượt thích");
        tvComments.setText(post.getComments().size() + " lượt bình luận");
        ratingBar.setRating(post.getRating());
        List<SlideModel> slideModels = new ArrayList<>();
        for (String urlPost : post.getImgs()) {
            slideModels.add(new SlideModel(URL_IMAGE + "post/" + urlPost));
        }
        sliderPost.setImageList(slideModels, true);
    }

    private void toggleLike() {
        if (isLike) {
            imgLike.setImageResource(R.drawable.ic_like_red);
        } else {
            imgLike.setImageResource(R.drawable.ic_like_outline);
        }

    }

    @SuppressLint("SetTextI18n")
    private void setLike() {
        if (isLike) {
            numLike += 1;
        } else {
            numLike -= 1;
        }
        tvLikes.setText(numLike + " lượt thích");
        spotsDialog.show();
        dao_like.setLike(idPost, "post", new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                spotsDialog.dismiss();
                if (isLike) {
                    toast("Đã  yêu thích bài viết");
                } else {
                    toast(" Đã bỏ yêu thích bài viết");
                }
            }

            @Override
            public void failedReq(String msg) {
                spotsDialog.dismiss();
                if (isLike) {
                    toast("Lỗi hệ thống, không thể yêu thích bài viết, vui lòng thử lại");
                } else {
                    toast("Lỗi hệ thống, không thể bỏ yêu thích bài viết, vui lòng thử lại");

                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String s) {
        Log.d("log", s);
    }
}