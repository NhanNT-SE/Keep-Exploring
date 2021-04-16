package com.example.keep_exploring.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Comment extends RecyclerView.Adapter<Adapter_RV_Comment.ViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    public Adapter_RV_Comment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String URL_IMG = helper_common.getBaseUrlImage();
        Comment comment = commentList.get(position);
        Picasso.get().load(URL_IMG + "user/" + comment.getUser().getImgUser()).into(holder.civUser);
        holder.tvComment.setText(comment.getContent());
        holder.tvUserName.setText(comment.getUser().getDisplayName());
        holder.tvPubDate.setText(helper_date.formatDateDisplay(comment.getDate()));
        if (comment.getUriImg() == null && (comment.getImg() == null || comment.getImg().isEmpty())) {
            holder.imgComment.setVisibility(View.GONE);
        } else {
            holder.imgComment.setVisibility(View.VISIBLE);
            if (comment.getUriImg() != null) {
                holder.imgComment.setImageURI(comment.getUriImg());
            } else if (comment.getImg() != null) {
                Picasso.get()
                        .load(URL_IMG + "comment/post/" + comment.getImg())
                        .into(holder.imgComment);
            }
        }

        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getUser().getId().equals(helper_sp.getUser().getId())) {
                    helper_common.replaceFragment(context, new Fragment_Tab_UserInfo());
                } else {
                    helper_common.dialogViewProfile(context, comment.getUser());
                }
            }
        });
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuComment(holder.imgMenu, comment);
            }
        });
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
            civUser = (CircleImageView) itemView.findViewById(R.id.row_comment_imgAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.row_comment_tvUserName);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_comment_tvPubDate);
            tvComment = (TextView) itemView.findViewById(R.id.row_comment_tvComment);
            imgMenu = (ImageView) itemView.findViewById(R.id.row_comment_imgMore);
            imgComment = (ImageView) itemView.findViewById(R.id.row_comment_imgComment);
        }
    }


    private void menuComment(View view, Comment comment) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_comment, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_comment_edit:
                        break;
                    case R.id.menu_coment_delete:
                        deleteComment(comment);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void deleteComment(Comment comment) {
        commentList.remove(comment);
        notifyDataSetChanged();
    }

    private void editComment(Comment comment){

    }
}
