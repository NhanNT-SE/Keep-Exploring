package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Like;
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

public class Fragment_Post_Details extends Fragment {
    //    View
    private View view;
    private CircleImageView civUser;
    private TextView tvDate, tvTitle, tvUserName, tvDesc, tvLikes;
    private TextView dUserLike_tvCancel, dUserLike_tvNothing;
    private RecyclerView dUserLike_rcUserList;
    private ImageView imgLike, imgComment;
    private ImageSlider isPost;
    //    DAO & Helper
    private DAO_Post dao_post;
    private Helper_Image helper_image;
    private Helper_Common helper_common = new Helper_Common();
    private Helper_Date helper_date = new Helper_Date();
    private Helper_SP helper_sp;
    //    Variable
    private Dialog_Fragment_Comment dialog_fragment_comment;
    private Adapter_RV_Like adapter_RVLike;
    private List<User> userLikeList = new ArrayList<>();
    private boolean isLike = false;
    private int sizeList = 0;
    private Post post;
    private Boolean isLogIn = false;

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
        showPost();
        return view;
    }
    private void initView() {
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_image = new Helper_Image(getContext());
        civUser = (CircleImageView) view.findViewById(R.id.fDetailPost_civAvatarAdmin);
        tvDate = (TextView) view.findViewById(R.id.fDetailPost_tvPubDate);
        tvDesc = (TextView) view.findViewById(R.id.fDetailPost_tvDescription);
        tvLikes = (TextView) view.findViewById(R.id.fDetailPost_tvLikes);
        tvTitle = (TextView) view.findViewById(R.id.fDetailPost_tvTitle);
        tvUserName = (TextView) view.findViewById(R.id.fDetailPost_tvUserName);
        imgComment = (ImageView) view.findViewById(R.id.fDetailPost_imgComment);
        imgLike = (ImageView) view.findViewById(R.id.fDetailPost_imgLike);
        isPost = (ImageSlider) view.findViewById(R.id.fDetailPost_imgPost);
        helper_sp = new Helper_SP(getContext());
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_fragment_comment = Dialog_Fragment_Comment.newInstance(post.get_id(),"post");
                dialog_fragment_comment.show(getChildFragmentManager(), dialog_fragment_comment.getTag());
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
        tvDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        tvUserName.setText(post.getOwner().getDisplayName());
        tvTitle.setText(post.getTitle());
        tvDesc.setText(post.getDesc());
        tvLikes.setText(sizeList + " lượt thích");
        List<SlideModel> slideModels = new ArrayList<>();
        for (String urlPost : post.getImgs()) {
            slideModels.add(new SlideModel(URL_IMAGE + "post/" + urlPost));
        }
        isPost.setImageList(slideModels, true);
        checkUserLiked();
        checkLike();
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
        dao_post.getLikeByPost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<User> likeList = (List<User>) response;
                rfLikeList(likeList);
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

    private void checkUserLiked() {
        //fetch data from server
        dao_post = new DAO_Post(getContext());
        dao_post.getLikeByPost(post.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<User> likeList = (List<User>) response;
                userLikeList = likeList;
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
        if (isLike) {
            imgLike.setImageResource(R.drawable.ic_like_red);
        } else {
            imgLike.setImageResource(R.drawable.ic_like_outline);
        }
    }
    private void rfLikeList(List<User> likeList) {
        adapter_RVLike = new Adapter_RV_Like(getContext(), likeList);
        dUserLike_rcUserList.setAdapter(adapter_RVLike);
        if (likeList.size() > 0) {
            dUserLike_tvNothing.setVisibility(View.GONE);
        } else {
            dUserLike_tvNothing.setVisibility(View.VISIBLE);
        }
    }
    private boolean checkLogin() {
        String accessToken = helper_sp.getAccessToken();
        return !accessToken.isEmpty();
    }
    private void log(String s) {
        Log.d("log", s);
    }
}