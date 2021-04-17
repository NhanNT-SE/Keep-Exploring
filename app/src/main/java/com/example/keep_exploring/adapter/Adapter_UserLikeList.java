package com.example.keep_exploring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_UserLikeList extends RecyclerView.Adapter<Adapter_UserLikeList.ViewHoHolder> {
    private Context context;
    private List<User> userList;
    private Helper_Common helper_common = new Helper_Common();

    public Adapter_UserLikeList(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.raw_user_like, parent, false);
        return new Adapter_UserLikeList.ViewHoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoHolder holder, int position) {
        User user = userList.get(position);

        String URL_IMG = helper_common.getBaseUrlImage();
        holder.tvUserName.setText(user.getDisplayName());
        Picasso.get().load(URL_IMG + "user/" + user.getImgUser()).into(holder.civUser);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHoHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName;

        public ViewHoHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.raw_user_like_civUser);
            tvUserName = (TextView) itemView.findViewById(R.id.raw_user_like_tvUserName);
        }
    }
}
