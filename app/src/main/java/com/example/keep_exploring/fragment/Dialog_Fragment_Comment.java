package com.example.keep_exploring.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_RV_Comment;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.User;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Fragment_Comment extends DialogFragment {
    //    View
    private View view;
    private RelativeLayout layoutImage;
    private RecyclerView rvComment;
    private TextView tvNothing, tvDone;
    private EditText etContent;
    private ImageView imgComment, imgDeleteImage;
    private ImageView imgSend, imgCamera;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    DAO & Helper
    private DAO_Comment dao_comment;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private Helper_Date helper_date;
    private Helper_SP helper_sp;
    //    Variable
    private static final int PICK_IMAGE_CODE = 1;
    private List<Comment> commentList;
    private Adapter_RV_Comment adapterComment;
    private String path;
    private Uri uriImage;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_comment, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        layoutImage = (RelativeLayout) view.findViewById(R.id.dComment_layoutImage);
        etContent = (EditText) view.findViewById(R.id.dComment_etComment);
        imgSend = (ImageView) view.findViewById(R.id.dComment_imgSend);
        imgCamera = (ImageView) view.findViewById(R.id.dComment_imgCamera);
        imgComment = (ImageView) view.findViewById(R.id.dComment_imgComment);
        imgDeleteImage = (ImageView) view.findViewById(R.id.dComment_imgDeleteImage);
        tvNothing = (TextView) view.findViewById(R.id.dComment_tvNothing);
        tvDone = (TextView) view.findViewById(R.id.dComment_tvDone);
        rvComment = (RecyclerView) view.findViewById(R.id.dComment_rvComment);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dComment_refreshLayout);

    }

    private void initVariable() {
        dao_comment = new DAO_Comment(getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        helper_sp = new Helper_SP(getContext());
        commentList = new ArrayList<>();
        path = "";
        user = helper_sp.getUser();
    }

    private void handlerEvent() {
        helper_common.configRecycleView(getContext(), rvComment);
        helper_common.showSkeleton(rvComment,adapterComment,R.layout.row_skeleton_comment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imgDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path = "";
                layoutImage.setVisibility(View.GONE);
            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_CODE);
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                if (content.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập nội dùng bình luận", Toast.LENGTH_SHORT).show();
                } else {
                    addComment(content);
                }
            }
        });
        imgSend.setEnabled(false);
        imgSend.setImageResource(R.drawable.ic_comment_send_disbled);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    imgSend.setEnabled(false);
                    imgSend.setImageResource(R.drawable.ic_comment_send_disbled);
                }else {
                    imgSend.setEnabled(true);
                    imgSend.setImageResource(R.drawable.ic_comment_send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        loadData();
    }


    private void loadData() {
        dao_comment.getCommentPost("6045e732ed9eac2464ee2ad8", new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                commentList = (List<Comment>) response;
                refreshRV();
                swipeRefreshLayout.setRefreshing(false);

            }
            @Override
            public void failedReq(String msg) {
                log(msg);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void addComment(String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUriImg(uriImage);
        comment.setDate(helper_date.convertStringToIsoDate(""));
        comment.setUser(user);
        commentList.add(0, comment);
        refreshRV();
        dao_comment.addCommentPost(content, "6045e732ed9eac2464ee2ad8", path, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                log(response.toString());
                layoutImage.setVisibility(View.GONE);
                path = "";
                etContent.setText("");
                uriImage = null;

            }

            @Override
            public void failedReq(String msg) {
                commentList.remove(comment);
                refreshRV();
                Toast.makeText(getContext(),
                        "Lỗi hệ thống, thêm bình luận không thành công, vui lòng thử lại",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshRV() {
        adapterComment = new Adapter_RV_Comment(getContext(), commentList);

        rvComment.setAdapter(adapterComment);
        if (commentList.size() > 0) {
            tvNothing.setVisibility(View.GONE);
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        log("on Resume");
    }

    @Override
    public void onStop() {
        super.onStop();
        log("on stop");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data.getData() != null) {
            uriImage = data.getData();
            imgComment.setImageURI(uriImage);
            path = helper_image.getPathFromUri(uriImage);
            layoutImage.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getTheme() {
        return R.style.DialogCommentTheme;
    }

    private void log(String s) {
        Log.d("log", s);
    }
}
