package com.example.project01_backup.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project01_backup.DAO.DAO_Address;
import com.example.project01_backup.DAO.DAO_Post;
import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_RV_Images_Post;
import com.example.project01_backup.helpers.Helper_Callback;
import com.example.project01_backup.helpers.Helper_Common;
import com.example.project01_backup.helpers.Helper_Image;
import com.example.project01_backup.helpers.Helper_Post;
import com.example.project01_backup.helpers.Helper_SP;
import com.example.project01_backup.model.ImageDisplay;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Edit_Post extends Fragment {

    private View view;
    public static final int CHOOSE_IMAGE_POST = 1;
    //  DAO
    private DAO_Post dao_post;
    private DAO_Address dao_address;
    //  HELPER
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Image helper_image;
    private Helper_Post helper_post;
    //  VIEW
    private ViewPager2 viewPager;
    private EditText etTitle, etDescription;
    private TextView tvUser, tvPubDate, tvAddress, tvCategory;
    private FloatingActionButton fabAddContent;
    private CircleImageView imgAvatarUser;
    private RatingBar ratingBar;
    //  VARIABLE
    private String categorySubmit;
    private String addressSubmit;
    private String additionalAddress;
    private String idPost;
    private User user;
    private List<ImageDisplay> imageDisplayList;
    private List<String> imagesSubmitList, imageDeleteList,imageDefaultList;
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
        handlerFunction();
        return view;
    }

    private void initView() {
        viewPager = (ViewPager2) view.findViewById(R.id.fEditPost_viewPager);
        tvUser = (TextView) view.findViewById(R.id.fEditPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fEditPost_tvPubDate);
        etDescription = (EditText) view.findViewById(R.id.fEditPost_etDescription);
        etTitle = (EditText) view.findViewById(R.id.fEditPost_etTitle);
        tvAddress = (TextView) view.findViewById(R.id.fEditPost_tvAddress);
        tvCategory = (TextView) view.findViewById(R.id.fEditPost_tvCategory);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fEditPost_fabAddContent);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fEditPost_imgAvatarUser);
        ratingBar = (RatingBar) view.findViewById(R.id.fEditPost_ratingBar);
    }

    private void initVariable() {
        dao_address = new DAO_Address(getContext());
        dao_post = new DAO_Post(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_post = new Helper_Post(getContext(), additionalAddress, addressSubmit, categorySubmit);
        imagesSubmitList = new ArrayList<>();
        imageDeleteList = new ArrayList<>();
        imageDisplayList = new ArrayList<>();
        imageDefaultList = new ArrayList<>();
        user = helper_sp.getUser();

        dao_post.getPostById("606bcbb6ef85ad3828e19b9c", new Helper_Callback() {
            @Override
            public void getPostById(Post post) {
                List<String> imageList = post.getImgs();
                int sizeList = imageList.size();
                for (int i = 0; i < sizeList; i++) {
                    ImageDisplay imageDisplay = new ImageDisplay();
                    imageDisplay.setImageString(imageList.get(i));
                    imageDisplayList.add(imageDisplay);
                    imageDefaultList.add(imageDisplay.getImageString());
                }
                tvCategory.setText(post.getCategory());
                etTitle.setText(post.getTitle());
                etDescription.setText(post.getDesc());
                ratingBar.setRating(post.getRating());
                tvAddress.setText(post.getAddress());
                idPost = post.get_id();
                refreshViewPager();

            }
        });
    }

    private void handlerFunction() {
        helper_common.formatDate(tvPubDate);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        helper_common.setTransformerViewPager(viewPager);
        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_post.dialogActionPost(tvAddress, tvCategory, new Helper_Callback() {
                    @Override
                    public void selectImage() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            if (data.getData() != null) {
                ImageDisplay imageDisplay = new ImageDisplay();
                Uri uri = data.getData();
                String realPath = helper_image.getPathFromUri(uri);
                imageDisplay.setImageString(realPath);
                imageDisplay.setImageUri(uri);
                imageDisplayList.add(0, imageDisplay);
            }
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    ImageDisplay imageDisplay = new ImageDisplay();
                    Uri uri = item.getUri();
                    String realPath = helper_image.getPathFromUri(uri);
                    imageDisplay.setImageString(realPath);
                    imageDisplay.setImageUri(uri);
                    imageDisplayList.add(0, imageDisplay);
                }
            }
        }
        refreshViewPager();
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                submit();
                break;
            case R.id.menu_post_clear:
                etDescription.setText("");
                tvAddress.setText("");
                etTitle.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void submit(){
        imagesSubmitList.clear();
        for (ImageDisplay item : imageDisplayList) {
            imagesSubmitList.add(item.getImageString());
        }
        imageDefaultList.forEach(item->imagesSubmitList.remove(item));

    }
    private void refreshViewPager() {
        Adapter_RV_Images_Post adapter_rv_images_post = new Adapter_RV_Images_Post(imageDisplayList, imageDeleteList);
        viewPager.setAdapter(adapter_rv_images_post);
    }
    private void log(String s) {
        Log.d("log", s);
    }
}