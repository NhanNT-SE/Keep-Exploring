package com.example.project01_backup.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.project01_backup.DAO.DAO_Post;
import com.example.project01_backup.R;
import com.example.project01_backup.helpers.Helper_Callback;
import com.example.project01_backup.helpers.Helper_Common;
import com.example.project01_backup.helpers.Helper_Image;
import com.example.project01_backup.model.Post;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Fragment_Edit_Post extends Fragment {

    private View view;
    private DAO_Post dao_post;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private String URL_IMAGE;
    private ImageView imageView;
    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            log("onBitmapLoaded\n" + bitmap);
            Uri uri = getUriFormBitmap(getContext(),bitmap);
            imageView.setImageURI(uri);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            log("onBitmapFailed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            log("onPrepareLoad");
        }
    };

    public Fragment_Edit_Post() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        initView();
        initVariable();
        return view;
    }

    private void initView() {
        imageView = (ImageView) view.findViewById(R.id.fEditPost_img);
        //        String urlImage = URL_IMAGE + "post/1615018348605-Candy-HD-Wallpaper-36743.jpg";

    }

    private void initVariable() {
        dao_post = new DAO_Post(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        URL_IMAGE = helper_common.getBaseUrlImage();
        String urlImage = URL_IMAGE + "post/1617501282384-avatar-hacker.png";
        getBitmapFromUrl(urlImage);
        dao_post.getPostById("606b1aad23618107b4608eb0", new Helper_Callback() {
            @Override
            public void getPostById(Post post) {
//                getBitmap(urlImage);

            }
        });
    }

    private void getBitmapFromUrl(String url){
        Picasso.get().load(url).into(target);
    }
    public Uri getUriFormBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void log(String s) {
        Log.d("log", s);
    }

}