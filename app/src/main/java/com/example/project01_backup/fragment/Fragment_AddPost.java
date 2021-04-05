package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.DAO.DAO_Address;
import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_Content;

import com.example.project01_backup.helpers.Helper_Callback;
import com.example.project01_backup.helpers.Helper_Common;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddPost extends Fragment {
    private View view;
    private EditText etTitle, etDescription;
    private TextView tvUser, tvPubDate, tvAddress, tvCategory;
    private FloatingActionButton fabAddContent;
    private ImageView imgPost;
    private CircleImageView imgAvatarUser;
    private User user;
    public static final int CHOOSE_IMAGE_POST = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    private DAO_Address dao_address;
    private String categorySubmit;
    private String addressSubmit;
    private String additionalAddress;

    public Fragment_AddPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        initView();
        return view;
    }

    private void initView() {
        dao_address = new DAO_Address(getContext());
        helper_sp = new Helper_SP(getContext());
        helper_common = new Helper_Common();
        user = helper_sp.getUser();
        tvUser = (TextView) view.findViewById(R.id.fAddPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fAddPost_tvPubDate);
        etDescription = (EditText) view.findViewById(R.id.fAddPost_etDescription);
        etTitle = (EditText) view.findViewById(R.id.fAddPost_etTitle);
        tvAddress = (TextView) view.findViewById(R.id.fAddPost_tvAddress);
        tvCategory = (TextView) view.findViewById(R.id.fAddPost_tvCategory);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fAddPost_fabAddContent);
        imgPost = (ImageView) view.findViewById(R.id.fAddPost_imgPost);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fAddPost_imgAvatarUser);
        setPubDate(tvPubDate);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
            }
        });


        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddAddress();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            imgPost.setImageURI(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        String selectedProvince = dSpProvince.getSelectedItem().toString();

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
                dao_address.getAddressList(selectedProvince, dDistrictList.get(position), new Helper_Callback() {
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
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                break;
            case R.id.menu_post_clear:
                etDescription.setText("");
                tvAddress.setText("");
                etTitle.setText("");
                imgPost.setImageResource(R.drawable.add_image);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private void log(String s) {
        Log.d("log", s);
    }


}
