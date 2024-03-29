package com.example.keep_exploring.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Blog_Detail;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.model.Blog;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Blog extends RecyclerView.Adapter<Adapter_RV_Blog.ViewHolder> {
    private Context context;
    private List<Blog> blogList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Adapter_LV_Image adapterImage;

    public Adapter_RV_Blog(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_blog, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Blog blog = blogList.get(position);
        adapterImage = new Adapter_LV_Image(context, blog.getBlogDetails());
        holder.tvUserName.setText(blog.getOwner().getDisplayName());
        holder.tvTitle.setText(blog.getTitle());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(blog.getCreated_on()));
        holder.tvComment.setText(blog.getComments().size() + " lượt bình luận");
        holder.tvLike.setText(blog.getLikes().size() + " lượt thích");
        Picasso.get().load(URL_IMAGE + "user/" + blog.getOwner().getImgUser()).into(holder.civUser);
        holder.gridView.setAdapter(adapterImage);

        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new Fragment_Blog_Detail();
                Bundle bundle = new Bundle();
                bundle.putString("idBlog", blog.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context, fragment);
            }
        });
        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_common.dialogViewProfile(context, blog.getOwner());
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvTitle, tvLike, tvComment;
        private GridView gridView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Activity activity = (Activity)context;
            civUser = (CircleImageView) itemView.findViewById(R.id.row_blog_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_blog_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_blog_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_blog_tvTitle);
            tvComment = (TextView) itemView.findViewById(R.id.row_blog_tvComment);
            tvLike = (TextView) itemView.findViewById(R.id.row_blog_tvLike);
            gridView = (GridView) itemView.findViewById(R.id.row_blog_gridView);
        }

    }


}
