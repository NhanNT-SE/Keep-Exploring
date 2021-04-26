package com.example.keep_exploring.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Notification;
import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Blog_Detail;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Notification;
import com.example.keep_exploring.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_RV_Notify extends RecyclerView.Adapter<Adapter_RV_Notify.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private DAO_Notification dao_notification;
    private Helper_SP helper_sp;
    private DAO_Auth dao_auth;

    public Adapter_RV_Notify(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        dao_notification = new DAO_Notification(context);
        helper_sp = new Helper_SP(context);
        dao_auth = new DAO_Auth(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        Blog blog = notification.getBlog();
        Post post = notification.getPost();
        String desc = getContentNotify(notification);
        holder.tvPubDate.setText(helper_date.formatDateDisplay(notification.getCreated_on()));
        holder.tvDesc.setText(desc);
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuNotify(holder.imgMenu, notification);
            }
        });
        if (notification.getStatus().equals("new")) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EBEBEC"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post != null) {
                    Fragment_Post_Details fragment_post_details = new Fragment_Post_Details();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("idPost", post.get_id());
                    fragment_post_details.setArguments(bundle);
                    helper_common.replaceFragment(context, fragment_post_details);
                } else if (blog != null) {
                    Fragment_Blog_Detail fragment_blog_details = new Fragment_Blog_Detail();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("idBlog", blog.get_id());
                    fragment_blog_details.setArguments(bundle);
                    helper_common.replaceFragment(context, fragment_blog_details);
                }
            }
        });
        setImageNotify(notification, holder.imgNotify);
        setImageType(notification, holder.cvType, holder.imgType);
    }
    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPubDate, tvDesc;
        private ImageView imgNotify, imgMenu;
        private CardView cvNotify, cvType;
        private RelativeLayout imgType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_notify_tvPubDate);
            tvDesc = (TextView) itemView.findViewById(R.id.row_notify_tvDesc);
            imgNotify = (ImageView) itemView.findViewById(R.id.row_notify_imgNotify);
            cvNotify = (CardView) itemView.findViewById(R.id.row_notify_cvNotify);
            cvType = (CardView) itemView.findViewById(R.id.row_notify_cvType);
            imgType = (RelativeLayout) itemView.findViewById(R.id.row_notify_imgType);
            imgMenu = (ImageView) itemView.findViewById(R.id.row_notify_imgMore);
        }


    }
    private void menuNotify(View view, Notification notification) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_notify, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_notify_makeNew:
                        changeStatusNotify(notification);
                        break;
                    case R.id.menu_notify_delete:
                        deleteNotify(notification);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
    private void deleteNotify(Notification notification) {
        int index = notificationList.indexOf(notification);
        notificationList.remove(notification);
        notifyDataSetChanged();

        dao_notification.deleteNotify(helper_sp.getAccessToken(), notification.getId(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
            }

            @Override
            public void failedReq(String msg) {
                notificationList.set(index, notification);
                notifyDataSetChanged();
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            deleteNotify(notification);
                        }

                        @Override
                        public void failedReq(String msg) {
                            helper_common.logOut(context);
                        }
                    });
                } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                    helper_common.logOut(context);
                } else {
                    Toast.makeText(context,
                            "Lỗi hệ thống, xóa thông báo không thành công. Vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void changeStatusNotify(Notification notification) {
        notification.setStatus("new");
        notifyDataSetChanged();
        dao_notification.changeNewStatusNotify(helper_sp.getAccessToken(), notification.getId(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
            }

            @Override
            public void failedReq(String msg) {
                notification.setStatus("seen");
                notifyDataSetChanged();
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            changeStatusNotify(notification);
                        }
                        @Override
                        public void failedReq(String msg) {
                            helper_common.logOut(context);
                        }
                    });
                } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                    helper_common.logOut(context);
                } else {
                    Toast.makeText(context,
                            "Lỗi hệ thống, cập nhật thông báo không thành công. Vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setImageNotify(Notification notify, ImageView imgNotify) {
        Blog blog = notify.getBlog();
        Post post = notify.getPost();
        if (post != null) {
            Picasso.get()
                    .load(helper_common.getBaseUrlImage() + "post/" + post.getImgs().get(0))
                    .into(imgNotify);
        } else if (blog != null) {
            Picasso.get()
                    .load(helper_common.getBaseUrlImage() + "blog/" + blog.getImage())
                    .into(imgNotify);
        } else {
            imgNotify.setImageResource(R.drawable.ic_custom_notify_system);
        }

    }

    private void setImageType(Notification notification, CardView cardView, RelativeLayout imgType) {
        String content = notification.getContent();
        String contentAdmin = notification.getContentAdmin();
        String status = "";
        Blog blog = notification.getBlog();
        Post post = notification.getPost();
        if (blog != null && notification.getStatusBlog() != null) {
            status = notification.getStatusBlog();
        }
        if (post != null && notification.getStatusPost() != null) {
            status = notification.getStatusPost();
        }
        if (contentAdmin != null) {
            cardView.setCardBackgroundColor(Color.parseColor("#B1AFAF"));
            imgType.setBackgroundResource(R.drawable.ic_custom_notify_system);
        } else if (content != null) {
            switch (content) {
                case "like": {
                    cardView.setCardBackgroundColor(Color.parseColor("#FA3B9A"));
                    imgType.setBackgroundResource(R.drawable.ic_notify_like);
                    break;
                }
                case "comment": {
                    cardView.setCardBackgroundColor(Color.parseColor("#02A8A8"));
                    imgType.setBackgroundResource(R.drawable.ic_notify_comment);
                    break;
                }
                case "moderated": {
                    cardView.setCardBackgroundColor(Color.parseColor("#056BE6"));
                    imgType.setBackgroundResource(R.drawable.ic_notify_done);
                    break;
                }
                case "unmoderated": {
                    if (status.equals("pending")) {
                        cardView.setCardBackgroundColor(Color.parseColor("#E1BB01"));
                        imgType.setBackgroundResource(R.drawable.ic_notify_pending);
                        break;
                    }
                    cardView.setCardBackgroundColor(Color.parseColor("#D80000"));
                    imgType.setBackgroundResource(R.drawable.ic_notify_need_update);
                    break;
                }
            }

        }
    }

    private String getContentNotify(Notification notify) {
        Blog blog = notify.getBlog();
        Post post = notify.getPost();
        StringBuilder desc = new StringBuilder();
        String status = "";
        String content = notify.getContent();

        if (notify.getContentAdmin() != null) {
            desc = desc.append("Thông báo từ hệ thống: ").append(notify.getContentAdmin());
        } else {
            if (post != null ) {
                if(notify.getStatusPost() != null){
                    status = notify.getStatusPost();

                }
                desc = desc.append("Bài viết '").append(post.getTitle()).append("' của bạn.");

            }
            if (blog != null) {
                if (notify.getStatusBlog() != null) {
                    status = notify.getStatusBlog();

                }
                desc = desc.append("Bài viết '").append(blog.getTitle()).append("' của bạn.");
            }
            switch (content) {
                case "like": {
                    desc.insert(0, " Vừa có người yêu thích về ");
                    break;
                }
                case "comment": {
                    desc.insert(0, " Vừa có người bình luận về ");
                    break;
                }
                case "moderated": {
                    desc.append(" đã được kiểm duyệt");
                    break;
                }
                case "unmoderated": {
                    if (status.equals("pending")) {
                        desc.append(" đang được kiểm duyệt");
                    } else if (status.equals("need_update")) {
                        desc.append(" cần được chỉnh sủa");
                    }
                    break;
                }
            }
        }
        return desc.toString();
    }



    private void refreshToken(Helper_Callback callback) {
        dao_auth.refreshToken(callback);
    }

}
