package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.keep_exploring.DAO.DAO_Auth;
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
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes, tvComments, tvCategory;
    private ImageView imgLike, imgComment, imgRefresh;
    private RelativeLayout layoutInfo;
    private ImageSlider sliderPost;
    private SpotsDialog spotsDialog;
    private RatingBar ratingBar;
    //    DAO & Helper
    private DAO_Auth dao_auth;
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
    private Post post;

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
        tvCategory = (TextView) view.findViewById(R.id.fDetailPost_tvCategory);
        imgComment = (ImageView) view.findViewById(R.id.fDetailPost_imgComment);
        imgLike = (ImageView) view.findViewById(R.id.fDetailPost_imgLike);
        imgRefresh = (ImageView) view.findViewById(R.id.fDetailPost_imgRefresh);
        sliderPost = (ImageSlider) view.findViewById(R.id.fDetailPost_sliderPost);
        ratingBar = (RatingBar) view.findViewById(R.id.fDetailPost_ratingBar);
        layoutInfo = (RelativeLayout) view.findViewById(R.id.fDetailPost_layoutInfo);
    }

    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
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
                dialogComment();
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
                if (user == null) {
                    helper_common.dialogRequireLogin(getContext());
                } else {
                    toggleLike();
                    setLike();
                }

            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_common.dialogViewProfile(getContext(), post.getOwner());
            }
        });
        tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComment();
            }
        });
        loadData();
    }
    private void dialogComment(){
        dialog_fragment_comment = Dialog_Fragment_Comment.newInstance(idPost, "post");
        dialog_fragment_comment.show(getChildFragmentManager(), dialog_fragment_comment.getTag());
    }
    private void loadData() {
        spotsDialog.show();
        dao_post.getPostById(idPost, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                post = (Post) response;
                List<String> likeList = post.getLikes();
                displayInfo();
                numLike = likeList.size();
                if (user != null) {
                    int checkLike = (int) likeList.stream().filter(e -> e.equals(user.getId())).count();
                    isLike = checkLike == 1;
                    if (isLike) {
                        imgLike.setImageResource(R.drawable.ic_like_red);
                    } else {
                        imgLike.setImageResource(R.drawable.ic_like_outline);
                    }
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
    private void displayInfo() {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(civUser);
        tvDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        helper_common.displayTextViewCategory(post.getCategory(),tvCategory);
        tvLikes.setText(post.getLikes().size() + " lượt thích");
        tvComments.setText(post.getComments().size() + " lượt bình luận");
        ratingBar.setRating(post.getRating());
        List<SlideModel> slideModels = new ArrayList<>();
        for (String urlPost : post.getImgs()) {
            slideModels.add(new SlideModel(URL_IMAGE + "post/" + urlPost));
        }
        if (!post.getStatus().equalsIgnoreCase("done")){
            layoutInfo.setVisibility(View.GONE);
        }
        sliderPost.setImageList(slideModels, true);
    }

    @SuppressLint("SetTextI18n")
    private void toggleLike() {
        spotsDialog.show();
        isLike = !isLike;
        if (isLike) {
            imgLike.setImageResource(R.drawable.ic_like_red);
            numLike += 1;

        } else {
            imgLike.setImageResource(R.drawable.ic_like_outline);
            numLike -= 1;
        }
        tvLikes.setText(numLike + " lượt thích");

    }

    @SuppressLint("SetTextI18n")
    private void setLike() {
        dao_like.setLike(helper_sp.getAccessToken(), idPost, "post", new Helper_Callback() {
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
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken();
                } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                    spotsDialog.dismiss();
                    helper_common.logOut(getContext());
                } else {
                    spotsDialog.dismiss();
                    if (isLike) {
                        toast("Lỗi hệ thống, không thể yêu thích bài viết, vui lòng thử lại");
                    } else {
                        toast("Lỗi hệ thống, không thể bỏ yêu thích bài viết, vui lòng thử lại");

                    }
                }
            }
        });
    }


    private void refreshToken() {
        dao_auth.refreshToken(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                setLike();
            }

            @Override
            public void failedReq(String msg) {
                spotsDialog.dismiss();
                helper_common.logOut(getContext());
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}