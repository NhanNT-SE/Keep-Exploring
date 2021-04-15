package com.example.keep_exploring.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.DAO.DAO_Notification;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Notification;
import com.example.keep_exploring.model.Post;

import java.util.List;

public class Adapter_RV_Notify extends RecyclerView.Adapter<Adapter_RV_Notify.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private DAO_Notification dao_notification;

    public Adapter_RV_Notify(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        helper_common = new Helper_Common();
        helper_date = new Helper_Date();
        dao_notification = new DAO_Notification(context);
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
        String[] contents = getContentNotify(notification);
        String title = contents[0];
        String desc = contents[1];
        holder.tvPubDate.setText(helper_date.formatDateDisplay(notification.getCreated_on()));
        holder.tvTitle.setText(title);
        holder.tvDesc.setText(desc);
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuNotify(holder.imgMenu, notification);
            }
        });
        if (notification.getStatus().equals("new")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#EBEBEC"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvPubDate, tvDesc;
        private ImageView imgMenu;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.row_notify_mainLayout);
            tvTitle = (TextView) itemView.findViewById(R.id.row_notify_tvTitle);
            tvPubDate = (TextView) itemView.findViewById(R.id.row_notify_tvPubDate);
            tvDesc = (TextView) itemView.findViewById(R.id.row_notify_tvDesc);
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
                        Log.d("log", "new");
                        break;
                    case R.id.menu_notify_delete:
                        Log.d("log", "delete");
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private String[] getContentNotify(Notification notify) {
        Blog blog = notify.getBlog();
        Post post = notify.getPost();
        String[] contents = new String[2];
        String title;
        String desc = "";
        if (post != null){
            title = "Bài viết";
        }
        String content = notify.getContent();
        if (notify.getContentAdmin() != null) {
            title = "Thông báo từ hệ thống:";
            desc = notify.getContentAdmin();
        } else {
            title = "Thông báo về bài viết:";
            switch (content) {
                case "like": {
                    desc = "Vừa có người yêu thích về bài viết của bạn!";
                    break;
                }
                case "comment": {
                    desc = "Vừa có người bình luận về bài viết của bạn!";
                    break;
                }
                case "moderated": {
                    desc = "Bài viết của bạn đã kiểm duyệt xong!";
                    break;
                }
                case "unmoderated": {
                    desc = "Bài viết của bạn đang trong quá trình kiểm duyệt!";
                    break;
                }
            }
        }
        desc = desc + " Vui lòng bấm vào thông báo để xem nội dung chi tiết.";
        contents[0] = title;
        contents[1] = desc;
        return contents;
    }
}
