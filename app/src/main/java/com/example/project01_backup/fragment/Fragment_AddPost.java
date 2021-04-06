package com.example.project01_backup.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.DAO.DAO_Address;
import com.example.project01_backup.DAO.DAO_Post;
import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_Content;

import com.example.project01_backup.adapter.Adapter_RV_Images_Post;
import com.example.project01_backup.helpers.Helper_Callback;
import com.example.project01_backup.helpers.Helper_Common;
import com.example.project01_backup.helpers.Helper_Image;
import com.example.project01_backup.helpers.Helper_Post;
import com.example.project01_backup.helpers.Helper_SP;
import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.ImageDisplay;
import com.example.project01_backup.model.Places;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddPost extends Fragment {
    private View view;
    private EditText etTitle, etDescription;
    private TextView tvUser, tvPubDate, tvAddress, tvCategory;
    private FloatingActionButton fabAddContent;
    private ViewPager2 viewPager;
    private CircleImageView imgAvatarUser;
    private RatingBar ratingBar;
    private User user;
    public static final int CHOOSE_IMAGE_POST = 1;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private DAO_Address dao_address;
    private DAO_Post dao_post;
    private String categorySubmit;
    private String addressSubmit;
    private String additionalAddress;
    private List<String> imagesSubmitList;
    private List<ImageDisplay> imageDisplayList;

    private Helper_Post helper_post;

    public Fragment_AddPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        initView();
        initVariable();
        handlerFunction();
        return view;
    }

    private void initView() {
        tvUser = (TextView) view.findViewById(R.id.fAddPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fAddPost_tvPubDate);
        etDescription = (EditText) view.findViewById(R.id.fAddPost_etDescription);
        etTitle = (EditText) view.findViewById(R.id.fAddPost_etTitle);
        tvAddress = (TextView) view.findViewById(R.id.fAddPost_tvAddress);
        tvCategory = (TextView) view.findViewById(R.id.fAddPost_tvCategory);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fAddPost_fabAddContent);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fAddPost_imgAvatarUser);
        viewPager = (ViewPager2) view.findViewById(R.id.fAddPost_viewPager);
        ratingBar = (RatingBar) view.findViewById(R.id.fAddPost_ratingBar);
    }

    private void initVariable() {
        dao_address = new DAO_Address(getContext());
        dao_post = new DAO_Post(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_post = new Helper_Post(getContext(), additionalAddress, addressSubmit, categorySubmit);
        imagesSubmitList = new ArrayList<>();
        imageDisplayList = new ArrayList<>();
        user = helper_sp.getUser();
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
                        startActivityForResult(
                                Intent.createChooser(intent, "Select picture"),
                                CHOOSE_IMAGE_POST);
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
                imageDisplayList.add(imageDisplay);
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
                    imageDisplayList.add(imageDisplay);
                }
            }
        }
        refreshViewPager();
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void currentFragment(String current) {
        if (current.equalsIgnoreCase("Restaurants")) {
            replaceFragment(new Fragment_Restaurant());
        } else if (current.equalsIgnoreCase("Accommodations")) {
            replaceFragment(new Fragment_Accommodations());
        } else if (current.equalsIgnoreCase("Beautiful Places")) {
            replaceFragment(new Fragment_BeautifulPlaces());
        } else {
            replaceFragment(new Fragment_JourneyDiary());
        }

    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_FrameLayout, fragment)
                .commit();
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
    private void submit() {
        imagesSubmitList.clear();
        for (ImageDisplay item : imageDisplayList) {
            imagesSubmitList.add(item.getImageString());
        }
        String addressSubmit = tvAddress.getText().toString();
        String categorySubmit = tvCategory.getText().toString();
        String titleSubmit = etTitle.getText().toString();
        String descriptionSubmit = etDescription.getText().toString();
        int ratingSubmit = Math.round(ratingBar.getRating());
        if (imageDisplayList.size() == 0) {
            toast("Vui lòng chọn ít nhất 1 hình ảnh cho bài viết");
        } else {
            if (addressSubmit.isEmpty() || categorySubmit.isEmpty()) {
                toast("Vui lòng chọn danh mục và địa chỉ cho bài viết");
            } else if (titleSubmit.isEmpty()) {
                toast("Vui lòng nhập tiêu đề cho bài viết");
            } else if (descriptionSubmit.isEmpty()) {
                toast("Vui lòng nhập nội dung chi tiết cho bài viết");
            } else {
                RequestBody bAddress = helper_common.createPartFromString(addressSubmit);
                RequestBody bCategory = helper_common.createPartFromString(categorySubmit);
                RequestBody bTitle = helper_common.createPartFromString(titleSubmit);
                RequestBody bDescription = helper_common.createPartFromString(descriptionSubmit);
                RequestBody bRating = helper_common.createPartFromString(String.valueOf(ratingSubmit));
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("address", bAddress);
                map.put("category", bCategory);
                map.put("title", bTitle);
                map.put("desc", bDescription);
                map.put("rating", bRating);
                dao_post.createPost(map, imagesSubmitList, new Helper_Callback() {
                    @Override
                    public void successReq(JSONObject data) {
                        if (data != null) {
                            toast("Tạo bài viết thành công");
                            imagesSubmitList.clear();
                            imageDisplayList.clear();
                            tvAddress.setText("");
                            tvCategory.setText("");
                            etTitle.setText("");
                            etDescription.setText("");
                            ratingBar.setRating(0f);
                            refreshViewPager();
                        }
                    }
                });
            }
        }
    }

    private void refreshViewPager() {
        Adapter_RV_Images_Post adapter_rv_images_post = new Adapter_RV_Images_Post(imageDisplayList);
        viewPager.setAdapter(adapter_rv_images_post);
    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
