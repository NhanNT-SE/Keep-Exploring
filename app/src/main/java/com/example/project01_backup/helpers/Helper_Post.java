package com.example.project01_backup.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.DAO.DAO_Address;
import com.example.project01_backup.DAO.DAO_Post;
import com.example.project01_backup.R;
import com.example.project01_backup.model.ImageDisplay;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class Helper_Post {
    private Context context;
    public static final int CHOOSE_IMAGE_POST = 1;
    private Helper_SP helper_sp;
    private String additionalAddress, addressSubmit, categorySubmit;
    private DAO_Address dao_address;
    private Helper_Common helper_common;
    private DAO_Post dao_post;

    public Helper_Post() {
    }

    public Helper_Post(Context context, String additionalAddress, String addressSubmit, String categorySubmit) {
        this.context = context;
        this.additionalAddress = additionalAddress;
        this.addressSubmit = addressSubmit;
        this.categorySubmit = categorySubmit;
        dao_address = new DAO_Address(context);
        dao_post = new DAO_Post(context);
        helper_sp = new Helper_SP(context);
        helper_common = new Helper_Common();
    }

    public void dialogActionPost(TextView tvAddress, TextView tvCategory, Helper_Callback callback) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_action_post);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dBtnAddress = (Button) dialog.findViewById(R.id.dActionPost_btnAddress);
        Button dBtnImages = (Button) dialog.findViewById(R.id.dActionPost_btnImages);
        Button dBtnCancel = (Button) dialog.findViewById(R.id.dActionPost_btnCancel);
        dBtnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogAddAddress(tvAddress, tvCategory);
            }
        });

        dBtnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectImage();
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

    private void dialogAddAddress(TextView tvAddress, TextView tvCategory) {
        final Dialog dialog = new Dialog(context);
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
                context,
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

    private HashMap<String, RequestBody> submit(List<String> imagesSubmitList,
                                                List<String> imageDeleteList,
                                                TextView tvAddress, TextView tvCategory,
                                                EditText etTitle, EditText etDescription,
                                                RatingBar ratingBar) {
        imagesSubmitList.clear();

        String addressSubmit = tvAddress.getText().toString();
        String categorySubmit = tvCategory.getText().toString();
        String titleSubmit = etTitle.getText().toString();
        String descriptionSubmit = etDescription.getText().toString();
        int ratingSubmit = Math.round(ratingBar.getRating());
        HashMap<String, RequestBody> map;
        if (imagesSubmitList.size() == 0) {
            toast("Vui lòng chọn ít nhất 1 hình ảnh cho bài viết");
            return null;
        } else {
            if (addressSubmit.isEmpty() || categorySubmit.isEmpty()) {
                toast("Vui lòng chọn danh mục và địa chỉ cho bài viết");
                return null;
            }
            if (titleSubmit.isEmpty()) {
                toast("Vui lòng nhập tiêu đề cho bài viết");
                return null;
            }
            if (descriptionSubmit.isEmpty()) {
                toast("Vui lòng nhập nội dung chi tiết cho bài viết");
                return null;
            }
            map = new HashMap<>();
            RequestBody bAddress = helper_common.createPartFromString(addressSubmit);
            RequestBody bCategory = helper_common.createPartFromString(categorySubmit);
            RequestBody bTitle = helper_common.createPartFromString(titleSubmit);
            RequestBody bDescription = helper_common.createPartFromString(descriptionSubmit);
            RequestBody bRating = helper_common.createPartFromString(String.valueOf(ratingSubmit));
            map.put("address", bAddress);
            map.put("category", bCategory);
            map.put("title", bTitle);
            map.put("desc", bDescription);
            map.put("rating", bRating);

            return map;
        }
    }

//    private void createPost() {
//        dao_post.createPost(map, imagesSubmitList, new Helper_Callback() {
//            @Override
//            public void successReq(JSONObject data) {
//                if (data != null) {
//                    toast("Tạo bài viết thành công");
//                    imagesSubmitList.clear();
//                    imageDisplayList.clear();
//                    tvAddress.setText("");
//                    tvCategory.setText("");
//                    etTitle.setText("");
//                    etDescription.setText("");
//                    ratingBar.setRating(0f);
//                    refreshViewPager();
//                }
//            }
//        });
//    }

    private void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
