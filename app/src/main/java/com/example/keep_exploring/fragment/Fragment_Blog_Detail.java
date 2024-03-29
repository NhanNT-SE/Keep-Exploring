package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.DAO.DAO_Like;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_Content;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.User;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Blog_Detail extends Fragment {
    //    View
    private View view;
    private TextView tvTitle, tvComment, tvLike;
    private TextView tvUser, tvPubDate;
    private ImageView imgBlog, imgRefresh, imgLike, imgExpanded;
    private CircleImageView imgAvatarUser;
    private LinearLayout layoutComment, layoutLike;
    private Dialog spotDialog;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    //    DAO & Helpers
    private DAO_Auth dao_auth;
    private DAO_Blog dao_blog;
    private DAO_Like dao_like;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Date helper_date;
    //    Variables
    private String idBlog;
    private String type;
    private ListView lvContent;
    private List<Blog_Details> blogDetailsList;
    private Adapter_LV_Content adapterContent;
    private Dialog_Fragment_Comment dialogComment;
    private Dialog_Fragment_Like dialogLike;
    private User user;
    private boolean isLike;
    private int numLike;
    private Blog blog;


    public Fragment_Blog_Detail() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog_detail, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }
    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
        tvUser = (TextView) view.findViewById(R.id.fBlogDetail_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fBlogDetail_tvPubDate);
        tvTitle = (TextView) view.findViewById(R.id.fBlogDetail_tvTitle);
        tvComment = (TextView) view.findViewById(R.id.fBlogDetail_tvComment);
        tvLike = (TextView) view.findViewById(R.id.fBlogDetail_tvLike);
        imgBlog = (ImageView) view.findViewById(R.id.fBlogDetail_imgBlog);
        imgLike = (ImageView) view.findViewById(R.id.fBlogDetail_imgLike);
        imgRefresh = (ImageView) view.findViewById(R.id.fBlogDetail_imgRefresh);
        imgExpanded = (ImageView) view.findViewById(R.id.fBlogDetail_imgExpanded);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fBlogDetail_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fBlogDetail_lvContent);
        layoutComment = (LinearLayout) view.findViewById(R.id.fBlogDetail_layoutComment);
        layoutLike = (LinearLayout) view.findViewById(R.id.fBlogDetail_layoutLike);
        appBar = (AppBarLayout) view.findViewById(R.id.fBlogDetail_appBar);
        toolbar = (Toolbar) view.findViewById(R.id.fBlogDetail_toolbar);
    }

    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_like = new DAO_Like(getContext());
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(view.getContext());
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        blogDetailsList = new ArrayList<>();
        user = helper_sp.getUser();
        numLike = 0;
        idBlog = "6073060019c68e0b99291ffb";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idBlog = bundle.getString("idBlog");
        }
    }

    private void handlerEvent() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    imgExpanded.setVisibility(View.VISIBLE);
                } else {
                    imgExpanded.setVisibility(View.GONE);
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBar.setExpanded(true, true);

            }
        });
        layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComment = Dialog_Fragment_Comment.newInstance(idBlog, "blog");
                dialogComment.show(getChildFragmentManager(), dialogComment.getTag());
            }
        });
        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLike = Dialog_Fragment_Like.newInstance(idBlog, "blog");
                dialogLike.show(getChildFragmentManager(), dialogLike.getTag());
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
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
        imgAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_common.dialogViewProfile(getContext(), blog.getOwner());
            }
        });
        loadData();
    }
    private void loadData() {
        spotDialog.show();
        dao_blog.getBlogById(idBlog, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                 blog = (Blog) response;
                List<String> likeList = blog.getLikes();
                blogDetailsList = blog.getBlogDetails();
                numLike = likeList.size();
                refreshListView();
                spotDialog.dismiss();
                if (user != null) {
                    int checkLike = (int) likeList.stream().filter(e -> e.equals(user.getId())).count();
                    isLike = checkLike == 1;
                    if (isLike) {
                        imgLike.setImageResource(R.drawable.ic_like_red);
                    } else {
                        imgLike.setImageResource(R.drawable.ic_like_outline);
                    }
                }
                displayInfo();

            }
            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayInfo() {
        Picasso.get().load(helper_common.getBaseUrlImage() + "blog/" + blog.getImage()).into(imgBlog);
        Picasso.get()
                .load(helper_common.getBaseUrlImage() + "user/" + blog.getOwner().getImgUser())
                .into(imgAvatarUser);
        tvTitle.setText(blog.getTitle());
        tvUser.setText(blog.getOwner().getDisplayName());
        tvPubDate.setText(helper_date.formatDateDisplay(blog.getCreated_on()));
        tvComment.setText(numLike + " lượt bình luận");
        tvLike.setText(numLike + " lượt thích");
        if (!blog.getStatus().equalsIgnoreCase("done")){
            layoutLike.setVisibility(View.GONE);
            layoutComment.setVisibility(View.GONE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void toggleLike() {
        spotDialog.show();
        isLike = !isLike;
        if (isLike) {
            imgLike.setImageResource(R.drawable.ic_like_red);
            numLike += 1;
        } else {
            imgLike.setImageResource(R.drawable.ic_like_outline);
            numLike -= 1;
        }
        tvLike.setText(numLike + " lượt thích");
    }
    private void setLike() {
        dao_like.setLike(helper_sp.getAccessToken(), idBlog, "blog", new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                spotDialog.dismiss();
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
                    spotDialog.dismiss();
                    helper_common.logOut(getContext());
                } else {
                    spotDialog.dismiss();
                    if (isLike) {
                        toast("Lỗi hệ thống, không thể yêu thích bài viết, vui lòng thử lại");
                    } else {
                        toast("Lỗi hệ thống, không thể bỏ yêu thích bài viết, vui lòng thử lại");

                    }
                }

            }
        });
    }

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
    }

    private void refreshToken() {
        dao_auth.refreshToken(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                setLike();
            }
            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
                helper_common.logOut(getContext());
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
