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

    public Adapter_LV_Content(Context context, List<Blog_Details> blogDetailsList) {
        this.context = context;
        this.blogDetailsList = blogDetailsList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.raw_content, null);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.raw_content_tvDescription);
        ImageView imgContent = (ImageView) convertView.findViewById(R.id.raw_content_imgContent);

        Blog_Details blogDetails = blogDetailsList.get(position);
        tvDescription.setText(blogDetails.getContent());
        if (blogDetails.getImg() != null){
            Picasso.get().load(Uri.parse(blogDetails.getImg())).into(imgContent);
        }else {
            imgContent.setImageURI(blogDetails.getUriImage());
        }

        return convertView;
    }
}
