package com.example.keep_exploring.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.activities.MainActivity;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.io.PipedInputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_PostUser extends RecyclerView.Adapter<Adapter_LV_PostUser.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private Helper_Common helper_common = new Helper_Common();

    public Adapter_LV_PostUser(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.raw_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        Post post = postList.get(position);

        holder.tvUserName.setText(post.getOwner().getDisplayName());
        holder.tvTitle.setText(post.getTitle());
        String dateFormated = post.getCreated_on().substring(0, 10);
        holder.tvPubDate.setText(dateFormated);
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(holder.civUser);
        Picasso.get().load(URL_IMAGE + "post/" + post.getImgs().get(0)).into(holder.imgPost);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Post_Details fragment_post_details = new Fragment_Post_Details();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                fragment_post_details.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout,fragment_post_details)
                        .addToBackStack(null)
                        .commit();

            }
        });

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
            civUser = (CircleImageView) itemView.findViewById(R.id.raw_post_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.raw_post_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.raw_post_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.raw_post_tvTitle);
            imgPost = (ImageView) itemView.findViewById(R.id.raw_post_imgPost);
        }
    }
}
