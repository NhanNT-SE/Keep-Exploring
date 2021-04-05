package com.example.project01_backup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project01_backup.R;
import com.example.project01_backup.helpers.Helper_Common;
import com.example.project01_backup.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_PostUser extends BaseAdapter {
    private Context context;
    private List<Post> postList;
    public static final String POST = "post";
    private final Helper_Common helper_common;
    public Adapter_LV_PostUser(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        helper_common = new Helper_Common();
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String URL_IMAGE = helper_common.getBaseUrlImage();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.raw_post, null);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_post_tvPubDate);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.raw_post_tvTitle);
        TextView tvUser = (TextView) convertView.findViewById(R.id.raw_post_tvUser);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.raw_post_tvAddress);
        ImageView imgPost = (ImageView) convertView.findViewById(R.id.raw_post_imgPost);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_post_imgAvatarUser);
        final Post post = postList.get(position);
        tvUser.setText(post.getOwner().getDisplayName());
        tvPubDate.setText(post.getCreated_on());
        tvTitle.setText(post.getTitle());
        tvAddress.setText(post.getAddress());
        Picasso.get().load(URL_IMAGE + "post/" + post.getImgs().get(0)).into(imgPost);
        Picasso.get().load(URL_IMAGE + "user/" + post.getOwner().getImgUser()).into(imgAvatar);
        return convertView;
    }
}
