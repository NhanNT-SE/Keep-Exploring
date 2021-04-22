package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Images_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Event;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_Post;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.ImageDisplay;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;


public class Fragment_EditPost extends Fragment {
    //  VIEW
    private View view;
    private ViewPager2 viewPager;
    private EditText etTitle, etDescription;
    private TextView tvUser, tvPubDate, tvAddress, tvCategory;
    private FloatingActionButton fabAddContent;
    private CircleImageView imgAvatarUser;
    private RatingBar ratingBar;
    private Dialog spotDialog;
    private CircleMenuView circleMenuView;

    //  Helper & DAO
    private DAO_Auth dao_auth;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Image helper_image;
    private Helper_Post helper_post;
    private Helper_Date helper_date;
    private DAO_Post dao_post;
    //  VARIABLE
    public static final int CHOOSE_IMAGE_POST = 1;
    private String idPost;
    private User user;
    private List<ImageDisplay> imageDisplayList;
    private List<String> imagesSubmitList, imageDeleteList, imageDefaultList;
    public Fragment_EditPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
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
        circleMenuView = (CircleMenuView) view.findViewById(R.id.fEditPost_circleMenu);
    }
    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_post = new DAO_Post(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_date = new Helper_Date();
        helper_post = new Helper_Post(getContext());
        imagesSubmitList = new ArrayList<>();
        imageDeleteList = new ArrayList<>();
        imageDisplayList = new ArrayList<>();
        imageDefaultList = new ArrayList<>();
        user = helper_sp.getUser();
        idPost = "6075c0fb6e5a5e20e8241100";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idPost = bundle.getString("idPost");
        }
    }

    private void handlerEvent() {
        helper_common.toggleBottomNavigation(getContext(), false);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        helper_common.configTransformerViewPager(viewPager);
        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCircleMenu();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCircleMenu();
                helper_post.dialogAddAddress(tvAddress);
            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCircleMenu();
            }
        });
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuOpenAnimationStart(view);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                switch (buttonIndex) {
                    case 0:
                        tvCategory.setText("Ăn uống");
                        view.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvCategory.setText("Khách sạn");
                        view.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvCategory.setText("Địa điểm");
                        view.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuCloseAnimationStart(view);
                view.setVisibility(View.GONE);
            }

        });
        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hideCircleMenu();
                }
            }
        });
        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hideCircleMenu();
                }
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                hideCircleMenu();
            }
        });
        hideCircleMenu();
        loadData();
    }

    private void showCircleMenu() {
        circleMenuView.setVisibility(View.VISIBLE);
        circleMenuView.open(true);
    }

    private void hideCircleMenu() {
        circleMenuView.close(false);
        circleMenuView.setVisibility(View.INVISIBLE);
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
                spotDialog.show();
                updatePost();
                break;
            case R.id.menu_post_clear:
                loadData();
                break;
            case R.id.menu_post_delete:
                String message = "Bạn muốn xóa bài viết cùng toàn bộ nội dung liên quan?";
                helper_common.alertDialog(getContext(), message, new Helper_Event() {
                    @Override
                    public void onSubmitAlertDialog() {
                        spotDialog.show();
                        deletePost();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePost() {
        dao_post.deletePost(helper_sp.getAccessToken(), idPost, new Helper_Callback() {
            @Override
            public void successReq(Object data) {
                toast("Đã xóa bài viết");
                spotDialog.dismiss();
                helper_common.replaceFragment(getContext(), new Fragment_Tab_UserInfo());
            }

            @Override
            public void failedReq(String msg) {
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            deletePost();
                        }

                        @Override
                        public void failedReq(String msg) {
                            spotDialog.dismiss();
                            helper_common.logOut(getContext());
                        }
                    });
                } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                    spotDialog.dismiss();
                    helper_common.logOut(getContext());
                } else {
                    spotDialog.dismiss();
                    toast("Có lỗi xảy ra, xóa bài viết không thành công, vui lòng thử lại sau ít phút");
                }

            }
        });

    }

    private void updatePost() {
        imagesSubmitList.clear();
        for (ImageDisplay item : imageDisplayList) {
            imagesSubmitList.add(item.getImageString());
        }
        imageDefaultList.forEach(item -> imagesSubmitList.remove(item));
        String addressSubmit = tvAddress.getText().toString();
        String categorySubmit = helper_common.convertCategorySubmit(tvCategory.getText().toString());
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
                RequestBody bCreated_on = helper_common.createPartFromString(helper_date.getIsoDate());
                RequestBody bImageDelete = helper_common.createPartFromString(String.join(",",imageDeleteList));
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("address", bAddress);
                map.put("category", bCategory);
                map.put("title", bTitle);
                map.put("desc", bDescription);
                map.put("rating", bRating);
                map.put("imgs_deleted",bImageDelete);
                map.put("created_on",bCreated_on);
                dao_post.updatePost(helper_sp.getAccessToken(), map, idPost, imagesSubmitList, new Helper_Callback() {
                    @Override
                    public void successReq(Object data) {
                        toast("Đã cập nhật bài viết, bài viết hiện đang trong quá trình kiểm duyệt");
                        spotDialog.dismiss();
                        helper_common.replaceFragment(getContext(), new Fragment_Tab_UserInfo());

                    }

                    @Override
                    public void failedReq(String msg) {
                        if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                            refreshToken(new Helper_Callback() {
                                @Override
                                public void successReq(Object response) {
                                    updatePost();
                                }

                                @Override
                                public void failedReq(String msg) {
                                    spotDialog.dismiss();
                                    helper_common.logOut(getContext());
                                }
                            });
                        } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                            spotDialog.dismiss();
                            helper_common.logOut(getContext());
                        } else {
                            spotDialog.dismiss();
                            toast("Có lỗi xảy ra, cập nhật bài viết không thành công, vui lòng thử lại sau ít phút");
                        }

                    }
                });
            }
        }
    }

    private void loadData() {
        imageDefaultList.clear();
        imagesSubmitList.clear();
        imageDeleteList.clear();
        imageDisplayList.clear();
        if (!spotDialog.isShowing()) {
            spotDialog.show();
        }
        dao_post.getPostById(idPost, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                Post post = (Post) response;
                List<String> imageList = post.getImgs();
                int sizeList = imageList.size();
                for (int i = 0; i < sizeList; i++) {
                    ImageDisplay imageDisplay = new ImageDisplay();
                    imageDisplay.setImageString(imageList.get(i));
                    imageDisplayList.add(imageDisplay);
                    imageDefaultList.add(imageDisplay.getImageString());
                }
                tvCategory.setText(helper_common.convertCategoryDisplay(post.getCategory()));
                etTitle.setText(post.getTitle());
                etDescription.setText(post.getDesc());
                ratingBar.setRating(post.getRating());
                tvAddress.setText(post.getAddress());
                tvPubDate.setText(helper_date.formatDateDisplay(post.getCreated_on()));
                refreshViewPager();
                spotDialog.dismiss();
            }

            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
            }
        });

    }

    private void refreshViewPager() {
        Adapter_RV_Images_Post adapter_rv_images_post = new Adapter_RV_Images_Post(imageDisplayList, imageDeleteList);
        viewPager.setAdapter(adapter_rv_images_post);
    }

    private void refreshToken(Helper_Callback callback) {
        dao_auth.refreshToken(callback);
    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}