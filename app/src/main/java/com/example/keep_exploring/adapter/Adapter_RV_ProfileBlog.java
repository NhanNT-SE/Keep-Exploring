package com.example.keep_exploring.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Blog_Detail;
import com.example.keep_exploring.fragment.Fragment_EditBlog;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_ProfileBlog extends RecyclerView.Adapter<Adapter_RV_ProfileBlog.ViewHolder> {
    private Context context;
    private List<Blog> blogList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    private User user;

    public Adapter_RV_ProfileBlog(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(context);
        user = helper_sp.getUser();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_profile_blog, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Blog blog = blogList.get(position);
        holder.toggleView(blog);
        holder.tvUserName.setText(blog.getOwner().getDisplayName());
        holder.tvTitle.setText(blog.getTitle());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(blog.getCreated_on()));
        holder.tvComment.setText(blog.getComments().size() + " lượt bình luận");
        holder.tvLike.setText(blog.getLikes().size() + " lượt thích");
        Picasso.get().load(URL_IMAGE + "user/" + blog.getOwner().getImgUser()).into(holder.civUser);
        Picasso.get().load(URL_IMAGE + "blog/" + blog.getImage()).into(holder.imgBlog);
        helper_common.displayStatus(blog.getStatus(), holder.tvStatus);
        if (!blog.getStatus().equalsIgnoreCase("done")) {
            holder.layoutContainer.setVisibility(View.GONE);
        }
        holder.imgBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Blog_Detail();
                Bundle bundle = new Bundle();
                bundle.putString("idBlog", blog.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context, fragment);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_EditBlog();
                Bundle bundle = new Bundle();
                bundle.putString("idBlog", blog.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context, fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvTitle, tvLike, tvComment, tvStatus;
        private ImageView imgBlog, imgEdit;
        private LinearLayout layoutContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_blogProfile_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_blogProfile_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_blogProfile_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_blogProfile_tvTitle);
            tvComment = (TextView) itemView.findViewById(R.id.row_blogProfile_tvComment);
            tvLike = (TextView) itemView.findViewById(R.id.row_blogProfile_tvLike);
            tvStatus = (TextView) itemView.findViewById(R.id.row_blogProfile_tvStatus);
            imgBlog = (ImageView) itemView.findViewById(R.id.row_blogProfile_imgBlog);
            imgEdit = (ImageView) itemView.findViewById(R.id.row_blogProfile_imgEdit);
            layoutContainer = (LinearLayout) itemView.findViewById(R.id.row_blogProfile_container_LikeComment);
        }

        public void toggleView(Blog blog) {
            if (blog.getOwner().getId().equals(user.getId())) {
                imgEdit.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);

            }

        }
    }


}
