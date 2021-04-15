package com.example.keep_exploring.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Comment extends RecyclerView.Adapter<Adapter_RV_Comment.ViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private Helper_Common helper_common = new Helper_Common();

    public Adapter_RV_Comment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.raw_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMG = helper_common.getBaseUrlImage();
        Comment comment = commentList.get(position);

        Picasso.get().load(URL_IMG + "user/" + comment.getIdUser().getImgUser()).into(holder.civUser);
        holder.tvComment.setText(comment.getContent());
        holder.tvUserName.setText(comment.getIdUser().getDisplayName());
        String dateFormated = comment.getDate().substring(0, 10);
        holder.tvPubDate.setText(dateFormated);
        Log.d("log", "onBindViewHolder: "+comment.getImg());

        if (comment.getImg().isEmpty()){
            holder.imgComment.setVisibility(View.GONE);
        } else {
            Picasso.get()
                    .load(URL_IMG+"comment/post/"+comment.getImg())
                    .into(holder.imgComment);
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName, tvPubDate, tvComment;
        private ImageView imgMenu, imgComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.raw_comment_imgAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.raw_comment_tvUserName);
            tvPubDate = (TextView) itemView.findViewById(R.id.raw_comment_tvPubDate);
            tvComment = (TextView) itemView.findViewById(R.id.raw_comment_tvComment);
            imgMenu = (ImageView) itemView.findViewById(R.id.raw_comment_imgMore);
            imgComment = (ImageView) itemView.findViewById(R.id.raw_comment_imgComment);
        }
    }
}
