package com.example.keep_exploring.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_User;
import com.example.keep_exploring.R;

import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.model.Feedback;
import com.example.keep_exploring.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserInfo extends Fragment {
    private View view;
    private Button btnEdit, btnFeedBack;
    private TextView tvName, tvEmail, tvGender, tvBirthday;
    private TextView tvAddress, tvTotalPost, tvTotalBlog, tvCreatedOn;
    private ImageView imgAvatar;
    private CircleImageView imgAvatarUser;
    private DAO_User dao_user;
    private User user;
    private Dialog spotDialog;
    private Helper_Common helper_common;


    public Fragment_UserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_infor, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }


    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
        tvName = (TextView) view.findViewById(R.id.fUserInfo_tvDisplayName);
        tvEmail = (TextView) view.findViewById(R.id.fUserInfo_tvEmail);
        tvGender = (TextView) view.findViewById(R.id.fUserInfo_tvGender);
        tvAddress = (TextView) view.findViewById(R.id.fUserInfo_tvAddress);
        tvTotalPost = (TextView) view.findViewById(R.id.fUserInfo_tvTotalPost);
        tvTotalBlog = (TextView) view.findViewById(R.id.fUserInfo_tvTotalBlog);
        tvCreatedOn = (TextView) view.findViewById(R.id.fUserInfo_tvCreatedOn);
        tvBirthday = (TextView) view.findViewById(R.id.fUserInfo_tvBirthday);


        btnEdit = (Button) view.findViewById(R.id.fUserInfo_btnEdit);
        btnFeedBack = (Button) view.findViewById(R.id.fUserInfo_btnFeedBack);
        imgAvatar = (ImageView) view.findViewById(R.id.fUserInfo_imgAvatar);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit();
            }
        });


    }
    private void initVariable() {
        user = new User();
        helper_common = new Helper_Common();
    }
    private void handlerEvent() {
        dao_user = new DAO_User(getContext());
        dao_user.getMyProfile(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                user = (User) response;
                showInfo();
            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void showInfo(){
        if (user != null){
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
            tvGender.setText(user.getGender());
            tvTotalPost.setText(String.valueOf(user.getPostList().size()));
            tvTotalBlog.setText(String.valueOf(user.getBlogList().size()));
            tvCreatedOn.setText(user.getCreated_on());
            if (user.getAddress() != null){
                tvAddress.setText(user.getAddress());
            }
            if (user.getBirthday() != null){
                tvBirthday.setText(user.getBirthday());
            }
            Picasso.get().load(helper_common.getBaseUrlImage()+"user/" + user.getImgUser()).into(imgAvatar);
        }

    }

//    private void dialogFeedback(){
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialog_add_feedback);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        final EditText etFeedback = (EditText) dialog.findViewById(R.id.dAddFeedback_etFeedback);
//        ImageView imgPost = (ImageView) dialog.findViewById(R.id.dAddFeedback_imgPost);
//        final Feedback feedback = new Feedback();
//        imgPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (etFeedback.getText().toString().isEmpty()){
//                    toast("Add your feedback");
//                }else {
//                    feedback.setContentFeedBack(etFeedback.getText().toString());
//                    feedback.setStringPubDate(stringPubDate());
//                    feedback.setLongPubDate(longPubDate());
//                    feedback.setEmailUser(currentUser.getEmail());
//                    feedback.setUriAvatarUser(String.valueOf(currentUser.getPhotoUrl()));
//                    feedback.setIdUser(currentUser.getUid());
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        dialog.show();
//    }

    private void dialogEdit() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etName, etPass, etDOB, etPhone, etAddress;
        TextView tvEmail;
        Button btnUpdate, btnClear, btnCancel;
        imgAvatarUser = (CircleImageView) dialog.findViewById(R.id.dEditInfo_imgAvatar);
        etName = (EditText) dialog.findViewById(R.id.dEditInfo_etName);
        tvEmail = (TextView) dialog.findViewById(R.id.dEditInfo_tvEmail);
        etPass = (EditText)      dialog.findViewById(R.id.dEditInfo_etPass);
        etDOB = (EditText) dialog.findViewById(R.id.dEditInfo_etDOB);
        etPhone = (EditText) dialog.findViewById(R.id.dEditInfo_etPhone);
        etAddress = (EditText) dialog.findViewById(R.id.dEditInfo_etAddress);
        btnUpdate = (Button) dialog.findViewById(R.id.dEditInfo_btnUpdate);
        btnClear = (Button) dialog.findViewById(R.id.dEditInfo_btnClear);
        btnCancel = (Button) dialog.findViewById(R.id.dEditInfo_btnCancel);

        etDOB.setKeyListener(null);
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etDOB);
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

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etPass.setText("");
                etDOB.setText("");
                etPhone.setText("");
                etAddress.setText("");
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
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                et.setText(format.format(calendar.getTime()));
            }
        }, year, month, day);

        dialog.show();
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private String stringPubDate() {
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(calendar.getTime());
        return date;
    }

    private long longPubDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5 && data != null) {
            imgAvatarUser.setImageURI(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void log(String s) {
        Log.d("log", s);
    }
}
