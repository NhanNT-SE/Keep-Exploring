package com.example.keep_exploring.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_EditPost;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_ProfilePost extends RecyclerView.Adapter<Adapter_RV_ProfilePost.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    private User user;

    public Adapter_RV_ProfilePost(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(context);
        user = helper_sp.getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_profile_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Post post = postList.get(position);
        holder.toggleView(post);
        holder.tvUserName.setText(post.getOwner().getDisplayName());
        holder.tvTitle.setText(post.getTitle());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        holder.ratingBar.setRating(post.getRating());
        holder.tvComment.setText(helper_common.displayNumber(post.getComments().size()));
        holder.tvLike.setText(helper_common.displayNumber(post.getLikes().size()));
        holder.tvAddress.setText(post.getAddress());
        helper_common.displayStatus(post.getStatus(),holder.tvStatus);
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(holder.civUser);
        Picasso.get().load(URL_IMAGE + "post/" + post.getImgs().get(0)).into(holder.imgPost);

        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Post_Details();
                Bundle bundle = new Bundle();
                bundle.putString("idPost", post.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context,fragment);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_EditPost();
                Bundle bundle = new Bundle();
                bundle.putString("idPost", post.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context,fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvTitle, tvAddress;
        private TextView tvLike, tvComment, tvStatus;
        private ImageView imgPost, imgEdit;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_postProfile_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_postProfile_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_postProfile_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_postProfile_tvTitle);
            tvComment = (TextView) itemView.findViewById(R.id.row_postProfile_tvComment);
            tvLike = (TextView) itemView.findViewById(R.id.row_postProfile_tvLike);
            tvStatus = (TextView) itemView.findViewById(R.id.row_postProfile_tvStatus);
            tvAddress = (TextView) itemView.findViewById(R.id.row_postProfile_tvAddress);
            imgPost = (ImageView) itemView.findViewById(R.id.row_postProfile_imgPost);
            imgEdit = (ImageView) itemView.findViewById(R.id.row_postProfile_imgEdit);
            ratingBar = (RatingBar) itemView.findViewById(R.id.row_postProfile_ratingBar);
        }

        public void toggleView(Post post) {
            if (post.getOwner().getId().equals(user.getId())) {
                imgEdit.setVisibility(View.VISIBLE);
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
            }
        }
    }

}
