package com.example.keep_exploring.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Post extends RecyclerView.Adapter<Adapter_RV_Post.ViewHolder> {
    private final Context context;
    private final List<Post> postList;
    private final Helper_Common helper_common;
    private final Helper_Date helper_date;

    public Adapter_RV_Post(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
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
        holder.tvAddress.setText(post.getAddress());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
        helper_common.displayTextViewCategory(post.getCategory(), holder.tvCategory);
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(holder.civUser);
        List<SlideModel> slideModels = new ArrayList<>();
        for (String urlPost : post.getImgs()) {
            slideModels.add(new SlideModel(URL_IMAGE + "post/"+ urlPost));
        }
        holder.sliderPost.setImageList(slideModels, true);
        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_common.dialogViewProfile(context, post.getOwner());
            }
        });
        holder.sliderPost.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = new Fragment_Post_Details();
                Bundle bundle = new Bundle();
                bundle.putString("idPost", post.get_id());
                fragment.setArguments(bundle);
                helper_common.replaceFragment(context, fragment);
            }
        });
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvTitle, tvAddress, tvCategory;
        private ImageSlider sliderPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_post_imgAvatarUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_post_tvUser);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_post_tvPubDate);
            tvTitle = (TextView) itemView.findViewById(R.id.row_post_tvTitle);
            sliderPost = (ImageSlider) itemView.findViewById(R.id.row_post_sliderPost);
            tvAddress = (TextView) itemView.findViewById(R.id.row_post_tvAddress);
            tvCategory = (TextView) itemView.findViewById(R.id.row_post_tvCategory);
        }
    }

}
