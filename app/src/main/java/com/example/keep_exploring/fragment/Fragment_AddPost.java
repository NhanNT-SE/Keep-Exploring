package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_Post;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.ImageDisplay;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddPost extends Fragment {
    //    View
    private View view;
    private EditText etTitle, etDescription;
    private TextView tvUser, tvPubDate, tvAddress, tvCategory;
    private FloatingActionButton fabAddContent;
    private ViewPager2 viewPager;
    private CircleImageView imgAvatarUser;
    private RatingBar ratingBar;
    private Dialog spotDialog;
    private CircleMenuView circleMenuView;
    //    Helper & DAO
    private DAO_Auth dao_auth;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private Helper_Date helper_date;
    private Helper_Post helper_post;
    private DAO_Post dao_post;
    //    Variable
    public static final int CHOOSE_IMAGE_POST = 1;
    private List<String> imagesSubmitList;
    private List<ImageDisplay> imageDisplayList;
    private User user;


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
        handlerEvent();
        return view;
    }

    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
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
        circleMenuView = (CircleMenuView) view.findViewById(R.id.fAddPost_circleMenu);

    }

    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_post = new DAO_Post(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        helper_post = new Helper_Post(getContext());
        imagesSubmitList = new ArrayList<>();
        imageDisplayList = new ArrayList<>();
        user = helper_sp.getUser();
    }

    private void handlerEvent() {
        helper_common.toggleBottomNavigation(getContext(), false);
        tvPubDate.setText(helper_date.formatDateDisplay(""));
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
                startActivityForResult(
                        Intent.createChooser(intent, "Select picture"),
                        CHOOSE_IMAGE_POST);
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


    private void showCircleMenu() {
        circleMenuView.setVisibility(View.VISIBLE);
        circleMenuView.open(true);
    }

    private void hideCircleMenu() {
        circleMenuView.close(false);
        circleMenuView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        MenuItem deleteItem = menu.findItem(R.id.menu_post_delete);
        deleteItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                createPost();
                break;
            case R.id.menu_post_clear:
                tvAddress.setText("");
                tvCategory.setText("");
                ratingBar.setRating(0);
                imageDisplayList.clear();
                imagesSubmitList.clear();
                etDescription.setText("");
                etTitle.setText("");
                refreshViewPager();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createPost() {
        imagesSubmitList.clear();
        for (ImageDisplay item : imageDisplayList) {
            imagesSubmitList.add(item.getImageString());
        }
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
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("address", bAddress);
                map.put("category", bCategory);
                map.put("title", bTitle);
                map.put("desc", bDescription);
                map.put("rating", bRating);
                spotDialog.show();

                dao_post.createPost(helper_sp.getAccessToken(), map, imagesSubmitList, new Helper_Callback() {
                    @Override
                    public void successReq(Object data) {
                        toast("Tạo bài viết thành công");
                        refreshViewPager();
                        spotDialog.dismiss();
                        helper_common.replaceFragment(getContext(), new Fragment_Category());
                    }

                    @Override
                    public void failedReq(String msg) {
                        if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                            refreshToken();
                        } else if (msg.equalsIgnoreCase(helper_common.LOG_OUT())) {
                            spotDialog.dismiss();
                            helper_common.logOut(getContext());
                        } else {
                            spotDialog.dismiss();
                            toast("Có lỗi xảy ra, tạo bài viết không thành công, vui lòng thử lại sau ít phút");

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

    private void refreshToken() {
        dao_auth.refreshToken(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                createPost();

            }

            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
                helper_common.logOut(getContext());
            }
        });
    }


    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
