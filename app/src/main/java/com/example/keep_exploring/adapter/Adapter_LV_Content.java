package com.example.keep_exploring.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.model.Blog_Details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_LV_Content extends BaseAdapter {
    private Context context;
    private List<Blog_Details> blogDetailsList;
    private LayoutInflater layoutInflater;

    public Adapter_LV_Content(Context context, List<Blog_Details> blogDetailsList) {
        this.context = context;
        this.blogDetailsList = blogDetailsList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return blogDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView imgContent;
        TextView tvDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_content, null);
            holder = new ViewHolder();
            holder.tvDescription = (TextView) convertView.findViewById(R.id.row_content_tvDescription);
            holder.imgContent = (ImageView) convertView.findViewById(R.id.row_content_imgContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Blog_Details blogDetails = blogDetailsList.get(position);
        holder.tvDescription.setText(blogDetails.getContent());
        if (blogDetails.getUriImage() != null) {
            holder.imgContent.setImageURI(blogDetails.getUriImage());
        } else if (blogDetails.getImg() != null) {
            Picasso.get().load(Uri.parse(blogDetails.getImg())).into(holder.imgContent);

        }
        return convertView;
    }
}
