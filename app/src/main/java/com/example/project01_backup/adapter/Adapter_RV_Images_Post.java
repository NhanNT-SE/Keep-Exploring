package com.example.project01_backup.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01_backup.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class Adapter_RV_Images_Post extends RecyclerView.Adapter<Adapter_RV_Images_Post.ViewHolder> {
    private List<Uri> uriList;
    private List<String> imageSubmitList;

    public Adapter_RV_Images_Post(List<Uri> uriList, List<String> imageSubmitList) {
        this.uriList = uriList;
        this.imageSubmitList = imageSubmitList;
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
        Uri uri = uriList.get(position);
        holder.imagePost.setImageURI(uri);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriList.remove(position);
                imageSubmitList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imagePost;
        private ImageButton imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = (RoundedImageView) itemView.findViewById(R.id.rImagePost);
            imgDelete = (ImageButton) itemView.findViewById(R.id.rImagePost_btnDelete);
        }
    }
}
