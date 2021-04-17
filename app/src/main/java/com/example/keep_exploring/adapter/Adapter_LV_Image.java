package com.example.keep_exploring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.keep_exploring.R;
import com.example.keep_exploring.model.Blog_Details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_LV_Image extends BaseAdapter {
    private Context context;
    private List<Blog_Details> blog_detailsList;
    private LayoutInflater layoutInflater;

    public Adapter_LV_Image(Context context, List<Blog_Details> blog_detailsList) {
        this.context = context;
        this.blog_detailsList = blog_detailsList;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return blog_detailsList.size();
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
        ImageView imageView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_image_grid_view, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.row_image_gird_view_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Blog_Details blog_details = blog_detailsList.get(position);
        holder.imageView.getLayoutParams().height =300;

        Picasso.get().load(blog_details.getImg()).into(holder.imageView);


        return convertView;
    }
}
