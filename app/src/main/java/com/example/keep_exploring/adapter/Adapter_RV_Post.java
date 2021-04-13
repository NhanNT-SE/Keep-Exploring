package com.example.keep_exploring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Post extends RecyclerView.Adapter<Adapter_RV_Post.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private Helper_Common helper_common = new Helper_Common();

    public Adapter_RV_Post(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Post post = postList.get(position);
        holder.tvUserName.setText(post.getOwner().getDisplayName());
        holder.tvTitle.setText(post.getTitle());
        holder.tvPubDate.setText(helper_common.formatDateDisplay(post.getCreated_on()));
        Picasso.get().load(URL_IMAGE+"user/"+post.getOwner().getImgUser()).into(holder.civUser);
        Picasso.get().load(URL_IMAGE + "post/" + post.getImgs().get(0)).into(holder.imgPost);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvTitle;
        private ImageView imgPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_post_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_post_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_post_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_post_tvTitle);
            imgPost = (ImageView) itemView.findViewById(R.id.row_post_imgPost);
        }
    }
}
