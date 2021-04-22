package com.example.keep_exploring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Comment extends RecyclerView.Adapter<Adapter_RV_Comment.ViewHolder> {
    private Context context;
    private List<Comment> commentList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    private DAO_Comment dao_comment;
    private DAO_Auth dao_auth;
    private String type;
    private User user;

    public Adapter_RV_Comment(Context context, List<Comment> commentList, String type) {
        this.context = context;
        this.commentList = commentList;
        this.type = type;
        dao_auth = new DAO_Auth(context);
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(context);
        dao_comment = new DAO_Comment(context);
        user = helper_sp.getUser();
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
        if (comment.getDate() != null) {
            holder.tvPubDate.setText(helper_date.formatDateDisplay(comment.getDate()));
        } else {
            holder.tvPubDate.setText(helper_date.formatDateDisplay(""));
        }

        if (user != null && user.getId().equals(comment.getUser().getId())) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        }
        if (comment.getUriImg() == null && (comment.getImg() == null || comment.getImg().isEmpty())) {
            holder.imgComment.setVisibility(View.GONE);
        } else {
            holder.imgComment.setVisibility(View.VISIBLE);
            if (comment.getUriImg() != null) {
                holder.imgComment.setImageURI(comment.getUriImg());
            } else if (comment.getImg() != null) {
                Picasso.get()
                        .load(URL_IMG + "comment/" + type + "/" + comment.getImg())
                        .into(holder.imgComment);
            }
        }

        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_common.dialogViewProfile(context, comment.getUser());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment(comment);
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
        private ImageView imgDelete, imgComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_comment_imgAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.row_comment_tvUserName);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_comment_tvPubDate);
            tvComment = (TextView) itemView.findViewById(R.id.row_comment_tvComment);
            imgDelete = (ImageView) itemView.findViewById(R.id.row_comment_imgDelete);
            imgComment = (ImageView) itemView.findViewById(R.id.row_comment_imgComment);
        }
    }

    private void deleteComment(Comment comment) {
        int index = commentList.indexOf(comment);
        commentList.remove(comment);
        notifyDataSetChanged();
        dao_comment.deleteComment(helper_sp.getAccessToken(), comment.get_id(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
            }

            @Override
            public void failedReq(String msg) {
                commentList.add(index, comment);
                notifyDataSetChanged();
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    dao_auth.refreshToken(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            deleteComment(comment);
                        }
                        @Override
                        public void failedReq(String msg) {
                            helper_common.logOut(context);
                        }
                    });
                } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                    helper_common.logOut(context);
                } else {
                    Toast.makeText(context, "Lỗi hệ thống xóa bình luận không thành công, vui lòng thử lại", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
