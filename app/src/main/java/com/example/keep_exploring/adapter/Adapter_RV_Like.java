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

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RV_Like extends RecyclerView.Adapter<Adapter_RV_Like.ViewHolder> {
    private Context context;
    private List<User> userList;
    private Helper_Common helper_common;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    private String URL_Image;

    public Adapter_RV_Like(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(context);
        URL_Image = helper_common.getBaseUrlImage();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_like, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user =  userList.get(position);
        holder.tvUserName.setText(user.getDisplayName());
        Picasso.get().load(URL_Image+"user/"+user.getImgUser()).into(holder.civUser);
        holder.civUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getId().equals(helper_sp.getUser().getId())) {
                    helper_common.replaceFragment(context, new Fragment_Tab_UserInfo());
                } else {
                    helper_common.dialogViewProfile(context, user);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civUser;
        private TextView tvUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civUser = (CircleImageView) itemView.findViewById(R.id.row_like_civUser);
            tvUserName = (TextView) itemView.findViewById(R.id.row_like_tvUserName);
        }
    }

}
