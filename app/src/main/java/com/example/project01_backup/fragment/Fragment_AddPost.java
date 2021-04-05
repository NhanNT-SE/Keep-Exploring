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
import com.example.project01_backup.helpers.Helper_SP;
import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Places;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

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
    private ViewPager2 viewPagerImagePost;
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
    private List<Uri> uriImageList;
    private Adapter_RV_Images_Post adapter_rv_images_post;
    private CompositePageTransformer compositePageTransformer;


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
        viewPagerImagePost = (ViewPager2) view.findViewById(R.id.fAddPost_vPPost);
        ratingBar = (RatingBar) view.findViewById(R.id.fAddPost_ratingBar);
    }

    private void initVariable() {
        dao_address = new DAO_Address(getContext());
        dao_post = new DAO_Post(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        imagesSubmitList = new ArrayList<>();
        uriImageList = new ArrayList<>();
        user = helper_sp.getUser();
    }

    private void handlerFunction() {
        setPubDate(tvPubDate);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        viewPagerImagePost.setClipToPadding(false);
        viewPagerImagePost.setClipChildren(false);
        viewPagerImagePost.setOffscreenPageLimit(3);
        viewPagerImagePost.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPagerImagePost.setPageTransformer(compositePageTransformer);
        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionPost();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                String realPath = helper_image.getPathFromUri(uri);
                imagesSubmitList.add(realPath);
                uriImageList.add(uri);
            }
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    String realPath = helper_image.getPathFromUri(uri);
                    imagesSubmitList.add(realPath);
                    uriImageList.add(uri);
                }
            }
        }
        refreshViewPager();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dialogActionPost() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_action_post);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dBtnAddress = (Button) dialog.findViewById(R.id.dActionPost_btnAddress);
        Button dBtnImages = (Button) dialog.findViewById(R.id.dActionPost_btnImages);
        Button dBtnCancel = (Button) dialog.findViewById(R.id.dActionPost_btnCancel);
        dBtnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogAddAddress();
            }
        });

        dBtnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
                dialog.dismiss();
            }
        });

        dBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void dialogAddAddress() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_address);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<String> dProvinceList = new ArrayList<>();
        List<String> dDistrictList = new ArrayList<>();
        List<String> dWardList = new ArrayList<>();
        List<String> dCategoryList = new ArrayList<>();
        dCategoryList.add("food");
        dCategoryList.add("hotel");
        dCategoryList.add("check_in");
        dProvinceList.addAll(helper_sp.getProvinceList());
        TextView dTvAddress = (TextView) dialog.findViewById(R.id.dAddress_tvAddress);
        TextView dTvCategory = (TextView) dialog.findViewById(R.id.dAddress_tvCategory);

        Spinner dSpCategory = (Spinner) dialog.findViewById(R.id.dAddress_spCategory);
        Spinner dSpProvince = (Spinner) dialog.findViewById(R.id.dAddress_spProvince);
        Spinner dSpDistrict = (Spinner) dialog.findViewById(R.id.dAddress_spDistrict);
        Spinner dSpWard = (Spinner) dialog.findViewById(R.id.dAddress_spWard);

        Button dBtnSubmit = (Button) dialog.findViewById(R.id.dAddress_btnSubmit);
        Button dBtnCancel = (Button) dialog.findViewById(R.id.dAddress_btnCancel);

        EditText dEdtAdditional = (EditText) dialog.findViewById(R.id.dAddress_edtAdditional);

        dEdtAdditional.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    additionalAddress = addressSubmit;
                } else {
                    additionalAddress = s + ", " + addressSubmit;
                }
                dTvAddress.setText(additionalAddress);

            }
        });

        setSpinner(dCategoryList, dSpCategory);
        setSpinner(dProvinceList, dSpProvince);

        dSpProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dao_address.getAddressList(dProvinceList.get(position), "", new Helper_Callback() {
                    @Override
                    public void addressList(List<String> districtList, List<String> wardList) {
                        dDistrictList.clear();
                        dWardList.clear();
                        dDistrictList.addAll(districtList);
                        dWardList.addAll(wardList);
                        setSpinner(dDistrictList, dSpDistrict);
                        setSpinner(dWardList, dSpWard);
                        setAddressToDisPLay(
                                ""
                                , "",
                                dSpProvince.getSelectedItem().toString()
                        );
                        dEdtAdditional.setText("");
                        dTvAddress.setText(addressSubmit);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dSpDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dao_address.getAddressList(dSpProvince.getSelectedItem().toString(), dDistrictList.get(position), new Helper_Callback() {
                    @Override
                    public void addressList(List<String> districtList, List<String> wardList) {
                        dWardList.clear();
                        dWardList.addAll(wardList);
                        setSpinner(dWardList, dSpWard);
                        setAddressToDisPLay(
                                dSpWard.getSelectedItem().toString()
                                , dSpDistrict.getSelectedItem().toString(),
                                dSpProvince.getSelectedItem().toString()
                        );
                        dEdtAdditional.setText("");
                        dTvAddress.setText(addressSubmit);

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dSpWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAddressToDisPLay(
                        dSpWard.getSelectedItem().toString()
                        , dSpDistrict.getSelectedItem().toString(),
                        dSpProvince.getSelectedItem().toString()
                );
                dEdtAdditional.setText("");
                dTvAddress.setText(addressSubmit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dSpCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySubmit = dCategoryList.get(position);
                dTvCategory.setText(categorySubmit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressSubmit = dTvAddress.getText().toString();
                tvAddress.setText(addressSubmit);
                tvCategory.setText(categorySubmit);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setSpinner(List<String> spinnerList, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                spinnerList);
        spinner.setAdapter(adapter);
    }

    private void setAddressToDisPLay(String ward, String district, String province) {
        String sWard;
        String sDistrict;
        additionalAddress = "";
        if (!ward.isEmpty()) {
            sWard = ward + ", ";
        } else {
            sWard = "";
        }
        if (!district.isEmpty()) {
            sDistrict = district + ", ";
        } else {
            sDistrict = "";
        }
        addressSubmit = additionalAddress + sWard + sDistrict + province;
    }

    private void setPubDate(TextView tv) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        tv.setText(format.format(calendar.getTime()));
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

    private long longPubDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
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
        String addressSubmit = tvAddress.getText().toString();
        String categorySubmit = tvCategory.getText().toString();
        String titleSubmit = etTitle.getText().toString();
        String descriptionSubmit = etDescription.getText().toString();
        int ratingSubmit = Math.round(ratingBar.getRating());

        if (imagesSubmitList.size() == 0) {
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
                dao_post.createPost(map, imagesSubmitList, new Helper_Callback(){
                    @Override
                    public void successReq(JSONObject data) {
                        if(data != null){
                            toast("Tạo bài viết thành công");
                            uriImageList = new ArrayList<>();
                            imagesSubmitList = new ArrayList<>();
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
        adapter_rv_images_post = new Adapter_RV_Images_Post(uriImageList, imagesSubmitList);
        viewPagerImagePost.setAdapter(adapter_rv_images_post);
    }
    private void log(String s) {
        Log.d("log", s);
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
