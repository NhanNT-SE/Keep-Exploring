package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
    private ImageView imgBlog, imgRefresh, imgLike;
    private CircleImageView imgAvatarUser;
    private LinearLayout layoutComment, layoutLike;
    private Dialog spotDialog;
    //    DAO & Helpers
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
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fBlogDetail_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fBlogDetail_lvContent);
        layoutComment = (LinearLayout) view.findViewById(R.id.fBlogDetail_layoutComment);
        layoutLike = (LinearLayout) view.findViewById(R.id.fBlogDetail_layoutLike);
    }
    private void initVariable() {
        dao_like = new DAO_Like(getContext());
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(view.getContext());
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        blogDetailsList = new ArrayList<>();
        user = helper_sp.getUser();
        idBlog = "6073060019c68e0b99291ffb";
    }
    private void handlerEvent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idBlog = bundle.getString("idBlog");
        }

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
                isLike = !isLike;
                toggleLike();
                setLike();
            }
        });
        loadData();
    }
    private void loadData() {
        spotDialog.show();
        dao_blog.getBlogById(idBlog, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                Blog blog = (Blog) response;
                List<String> likeList = blog.getLikes();
                blogDetailsList = blog.getBlogDetails();
                Picasso.get().load(helper_common.getBaseUrlImage() + "blog/" + blog.getImage()).into(imgBlog);
                tvTitle.setText(blog.getTitle());
                tvPubDate.setText(helper_date.formatDateDisplay(blog.getCreated_on()));
                tvComment.setText(helper_common.displayNumber(blog.getComments().size()));
                tvLike.setText(helper_common.displayNumber(blog.getLikes().size()));
                refreshListView();
                spotDialog.dismiss();
                int checkLike = (int) likeList.stream().filter(e -> e.equals(user.getId())).count();
                isLike = checkLike == 1;
                toggleLike();
            }

            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
            }
        });
    }

    private void toggleLike() {
        if (isLike) {
            imgLike.setImageResource(R.drawable.ic_like_red);
        } else {
            imgLike.setImageResource(R.drawable.ic_like_outline);
        }
    }
    private void setLike() {
        spotDialog.show();
        dao_like.setLike(idBlog, "blog", new Helper_Callback() {
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
                spotDialog.dismiss();
                if (isLike) {
                    toast("Lỗi hệ thống, không thể yêu thích bài viết, vui lòng thử lại");
                } else {
                    toast("Lỗi hệ thống, không thể bỏ yêu thích bài viết, vui lòng thử lại");

                }
            }
        });
    }

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String s) {
        Log.d("log", s);
    }
}
