package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_User;
import com.example.keep_exploring.R;
import com.example.keep_exploring.activities.SignInActivity;
import com.example.keep_exploring.adapter.Adapter_Tab_User;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Tab_UserInfo extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvUpdateProfile, tvFeedBack;
    private TextView tvName, tvTotalPost, tvTotalBlog, tvTotal;
    private ImageView imgAvatar;
    private CircleImageView imgAvatarUser;
    private Dialog spotDialog;
    private MaterialButton mBtn_submit;
    //DAO & Helper
    private DAO_User dao_user;
    private DAO_Auth dao_auth;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Date helper_date;
    private Helper_Image helper_image;
    //Variable
    private User user;
    private String gender;
    private String idUser;
    private String imageUser;
    private boolean isValidRequire, isValidMatch;

    public Fragment_Tab_UserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }
        view = inflater.inflate(R.layout.fragment_tab_user, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        tabLayout = (TabLayout) view.findViewById(R.id.fTabUser_tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.fTabUser_viewPager);
        tvName = (TextView) view.findViewById(R.id.fTabUser_tvDisplayName);
        tvTotal = (TextView) view.findViewById(R.id.fTabUser_tvTotal);
        tvTotalPost = (TextView) view.findViewById(R.id.fTabUser_tvTotalPost);
        tvTotalBlog = (TextView) view.findViewById(R.id.fTabUser_tvTotalBlog);
        tvUpdateProfile = (TextView) view.findViewById(R.id.fTabUser_tvUpdateProfile);
        imgAvatar = (ImageView) view.findViewById(R.id.fTabUser_imgAvatar);
        spotDialog = new SpotsDialog(getActivity());
    }

    private void initVariable() {
        imageUser = "";
        isValidRequire = false;
        isValidMatch = false;
        user = new User();
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(getContext());
        helper_date = new Helper_Date();
        helper_image = new Helper_Image(getContext());
        dao_auth = new DAO_Auth(getContext());
        viewPager.setAdapter(new Adapter_Tab_User(getChildFragmentManager(), 1));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_post_black);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_blog_black);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idUser = bundle.getString("idUser");

        } else {
            idUser = helper_sp.getUser().getId();
        }

        if (idUser.equals(helper_sp.getUser().getId())){
            tvUpdateProfile.setVisibility(View.VISIBLE);
        }else {
            tvUpdateProfile.setVisibility(View.GONE);

        }
    }

    private void handlerEvent() {
        helper_common.toggleBottomNavigation(getContext(), true);
        dao_user = new DAO_User(getContext());
        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    dialogEdit();
                }
            }
        });
        helper_common.setBadgeNotify(getContext());
        loadData();
    }

    private void showInfo() {
        if (user != null) {
            tvName.setText(user.getDisplayName());
            int totalPost = user.getPostList().size();
            int totalBlog = user.getBlogList().size();
            int total = totalPost + totalBlog;
            tvTotalPost.setText(String.valueOf(totalPost));
            tvTotalBlog.setText(String.valueOf(totalBlog));
            tvTotal.setText(String.valueOf(total));
            Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatar);
            gender = user.getGender();
        }

    }


    private void dialogChangePassword() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_change_password);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextInputLayout tIL_currentPassword = (TextInputLayout) dialog.findViewById(R.id.dChangePassword_tILCurrentPassword);
        TextInputLayout tIL_newPassword = (TextInputLayout) dialog.findViewById(R.id.dChangePassword_tILNewPassword);
        TextInputLayout tIL_confirmPassword = (TextInputLayout) dialog.findViewById(R.id.dChangePassword_tILConfirmPassword);
        mBtn_submit = (MaterialButton) dialog.findViewById(R.id.dChangePassword_btnSubmit);
        MaterialButton mBtn_cancel = (MaterialButton) dialog.findViewById(R.id.dChangePassword_btnCancel);
        toggleEnableButton();
        validateRequiredAndLength(tIL_currentPassword);
        validateRequiredAndLength(tIL_newPassword);
        validateMatchPassword(tIL_confirmPassword, tIL_newPassword);
        mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = tIL_currentPassword.getEditText().getText().toString();
                String newPass = tIL_newPassword.getEditText().getText().toString();
                spotDialog.show();
                dao_user.changePassword(helper_sp.getAccessToken(), oldPass, newPass, new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        toast("Đổi mật khẩu thành công, vui lòng đăng nhập lại để tiếp tục");
                        spotDialog.dismiss();
                        dialog.dismiss();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getContext().startActivity(new Intent(getContext(), SignInActivity.class));
                            }
                        }, 2000);
                    }

                    @Override
                    public void failedReq(String msg) {
                        spotDialog.dismiss();
                        toast(msg);
                    }
                });
            }
        });

        dialog.show();
    }
    private void dialogEdit() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etName, etBirthday, etAddress;
        Button btnUpdate, btnChangePassword, btnCancel;
        RadioButton radioMale, radioFemale;
        imgAvatarUser = (CircleImageView) dialog.findViewById(R.id.dEditInfo_imgAvatar);
        etName = (EditText) dialog.findViewById(R.id.dEditInfo_etDisplayName);
        etBirthday = (EditText) dialog.findViewById(R.id.dEditInfo_etBirthday);
        etAddress = (EditText) dialog.findViewById(R.id.dEditInfo_etAddress);
        btnUpdate = (Button) dialog.findViewById(R.id.dEditInfo_btnUpdate);
        btnChangePassword = (Button) dialog.findViewById(R.id.dEditInfo_btnChangePassword);
        btnCancel = (Button) dialog.findViewById(R.id.dEditInfo_btnCancel);
        radioMale = (RadioButton) dialog.findViewById(R.id.dEditInfo_radioMale);
        radioFemale = (RadioButton) dialog.findViewById(R.id.dEditInfo_radioFemale);
        if (gender.equals("male")) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
        if (user != null) {
            etName.setText(user.getDisplayName());
            if (user.getAddress() != null) {
                etAddress.setText(user.getAddress());
            }
            if (user.getBirthday() != null) {
                etBirthday.setText(helper_date.formatDateDisplay(user.getBirthday()));
            }
            Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        }
        radioMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gender = "male";
                }

            }
        });

        radioFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gender = "female";
                }

            }
        });

        etBirthday.setKeyListener(null);
        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etBirthday);
            }
        });


        imgAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 5);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDay = etBirthday.getText().toString();
                String address = etAddress.getText().toString();
                String displayName = etName.getText().toString();
                if (displayName == null || displayName.isEmpty()) {
                    toast("Tên hiển thị là bắt buộc");
                } else {
                    HashMap<String, RequestBody> map = new HashMap<>();
                    RequestBody bGender = helper_common.createPartFromString(gender);
                    RequestBody bDisplayName = helper_common.createPartFromString(displayName);
                    if (birthDay != null) {
                        String birthDaySubmit = helper_date.convertStringToIsoDate(birthDay);
                        RequestBody bBirthDay = helper_common.createPartFromString(birthDaySubmit);
                        map.put("bod", bBirthDay);
                    }
                    if (address != null) {
                        RequestBody bAddress = helper_common.createPartFromString(address);
                        map.put("address", bAddress);
                    }
                    map.put("gender", bGender);
                    map.put("displayName", bDisplayName);
                    spotDialog.show();
                    dao_user.updateProfile(helper_sp.getAccessToken(), imageUser, map, new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            toast("Cập nhật thông tin cá nhân thành công");
                            spotDialog.dismiss();
                            dialog.dismiss();
                            loadData();
                        }

                        @Override
                        public void failedReq(String msg) {
                            toast(msg);
                            spotDialog.dismiss();
                        }
                    });
                }


            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangePassword();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void datePicker(final EditText et) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                et.setText(format.format(calendar.getTime()));
            }
        }, year, month, day);

        dialog.show();
    }

    private void toggleEnableButton() {
        mBtn_submit.setEnabled(isValidMatch && isValidRequire);
    }
    private void validateMatchPassword(TextInputLayout tIL_confirmPass, TextInputLayout tIL_newPass) {

        tIL_confirmPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String match = tIL_newPass.getEditText().getText().toString();
                if (s.toString().equals(match)) {
                    tIL_confirmPass.setErrorEnabled(false);
                    isValidMatch = true;
                } else {
                    tIL_confirmPass.setError("Mật khẩu xác nhận của bạn không khớp với mật khẩu mới");
                    tIL_confirmPass.setErrorEnabled(true);
                    isValidMatch = false;
                }
                toggleEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void validateRequiredAndLength(TextInputLayout textInputLayout) {
        String length = textInputLayout.getEditText().getText().toString();
        if (length.isEmpty()) {
            textInputLayout.setError("Đây là trường bắt buộc");
            textInputLayout.setErrorEnabled(true);
            isValidRequire = false;
        }
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    textInputLayout.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    textInputLayout.setErrorEnabled(true);
                    isValidRequire = false;
                } else {
                    textInputLayout.setErrorEnabled(false);
                    isValidRequire = true;
                }
                toggleEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadData() {
        dao_user.getProfile(helper_sp.getAccessToken(),idUser,new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                user = (User) response;
                showInfo();
            }

            @Override
            public void failedReq(String msg) {
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken();
                } else {
                    helper_common.logOut(getContext());
                }
            }
        } );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5 && data != null) {
            imgAvatarUser.setImageURI(data.getData());
            imageUser = helper_image.getPathFromUri(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void refreshToken() {
        dao_auth.refreshToken(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                loadData();
            }

            @Override
            public void failedReq(String msg) {
                helper_common.logOut(getContext());
            }
        });
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
