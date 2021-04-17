package com.example.keep_exploring.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.activities.MainActivity;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Post extends RecyclerView.Adapter<Adapter_RV_Post.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private Helper_Common helper_common = new Helper_Common();
    private Helper_Date helper_date = new Helper_Date();

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
        Helper_SP helper_sp = new Helper_SP(context);
        Post post = postList.get(position);

        holder.tvUserName.setText(post.getOwner().getDisplayName());
        holder.tvTitle.setText(post.getTitle());
        holder.tvAddress.setText(post.getAddress());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(holder.civUser);

        List<SlideModel> slideModels = new ArrayList<>();
        for (String urlPost : post.getImgs()) {
            slideModels.add(new SlideModel(URL_IMAGE + "post/"+ urlPost));
        }
        holder.isPost.setImageList(slideModels, true);
        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getOwner().getId().equals(helper_sp.getUser().getId())) {
                    helper_common.replaceFragment(context, new Fragment_Tab_UserInfo());
                } else {
                    helper_common.dialogViewProfile(context, post.getOwner());
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Post_Details fragment_post_details = new Fragment_Post_Details();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                fragment_post_details.setArguments(bundle);
                helper_common.replaceFragment(context, fragment_post_details);
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
        private ImageView imgPost;
        private ImageSlider isPost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_post_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_post_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_post_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_post_tvTitle);
            isPost = (ImageSlider) itemView.findViewById(R.id.row_post_imgPost);
            tvAddress = (TextView) itemView.findViewById(R.id.row_post_tvAddress);
        }
    }

}
