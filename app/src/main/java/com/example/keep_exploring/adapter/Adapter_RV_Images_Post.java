package com.example.keep_exploring.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.ImageDisplay;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_RV_Images_Post extends RecyclerView.Adapter<Adapter_RV_Images_Post.ViewHolder> {

    private final List<ImageDisplay> imageDisplayList;
    private List<String> imageDeleteList;

    public Adapter_RV_Images_Post(List<ImageDisplay> imageDisplayList) {
        this.imageDisplayList = imageDisplayList;
    }

    public Adapter_RV_Images_Post(List<ImageDisplay> imageDisplayList, List<String> imageDeleteList) {
        this.imageDisplayList = imageDisplayList;
        this.imageDeleteList = imageDeleteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_image_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageDisplay imageDisplay = imageDisplayList.get(position);
        String URL_IMAGE = holder.helper_common.getBaseUrlImage() + "post/";
        if (imageDisplay.getImageUri() != null) {
            holder.imagePost.setImageURI(imageDisplay.getImageUri());
        } else {
            Picasso.get().load(URL_IMAGE + imageDisplay.getImageString()).into(holder.imagePost);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDisplayList.remove(position);
                if (imageDeleteList != null && imageDisplay.getImageUri() == null) {
                    imageDeleteList.add(imageDisplay.getImageString());
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageDisplayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView imagePost;
        private final ImageButton imgDelete;
        private final Helper_Common helper_common;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            helper_common = new Helper_Common();
            imagePost = (RoundedImageView) itemView.findViewById(R.id.rImagePost);
            imgDelete = (ImageButton) itemView.findViewById(R.id.rImagePost_btnDelete);
        }
    }
}
