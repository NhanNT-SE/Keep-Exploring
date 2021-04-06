package com.example.keep_exploring.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_Comment;
import com.example.keep_exploring.adapter.Adapter_LV_Content;
import com.example.keep_exploring.adapter.Adapter_LV_PostUser;

import com.example.keep_exploring.model.Comment;
import com.example.keep_exploring.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Blog_Detail extends Fragment {

    private View view;
    private ImageView  imgPost, imgContent, imgComment;
    private CircleImageView imgAvatar;
    private TextView tvTitle, tvPubDate, tvDescription, tvAddress, tvEmail;
    private FirebaseUser currentUser;
    private Post post;
    private Adapter_LV_Comment adapterComment;
    private Adapter_LV_Content adapterContent;


    public Fragment_Blog_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog_detail, container, false);
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        post = (Post) bundle.getSerializable(Adapter_LV_PostUser.POST);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        tvEmail = (TextView) view.findViewById(R.id.fDetail_tvEmail);
        tvPubDate = (TextView) view.findViewById(R.id.fDetail_tvPubDate);
        tvTitle = (TextView) view.findViewById(R.id.fDetail_tvTitle);
        tvAddress = (TextView) view.findViewById(R.id.fDetail_tvAddress);
        tvDescription = (TextView) view.findViewById(R.id.fDetail_tvDescription);
        imgAvatar = (CircleImageView) view.findViewById(R.id.fDetail_imgAvatarUser);
        imgPost = (ImageView) view.findViewById(R.id.fDetail_imgPost);
        imgContent = (ImageView) view.findViewById(R.id.fDetail_imgContents);
        imgComment = (ImageView) view.findViewById(R.id.fDetail_imgComments);

        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogContents();
            }
        });

        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComments();
            }
        });

//        String email = post.getDisplayName();
//        String pubDate = post.getPubDate();
//        String title = post.getTittle();
//        String address = post.getAddress();
//        String description = post.getDescription();
//        String uriAvatar = post.getUrlAvatarUser();
//        String uriPost = post.getUrlImage();
//
//        tvEmail.setText(email);
//        tvPubDate.setText(pubDate);
//        tvTitle.setText(title);
//        tvAddress.setText(address);
//        tvDescription.setText(description);
//        Picasso.get().load(Uri.parse(uriAvatar)).into(imgAvatar);
//        Picasso.get().load(Uri.parse(uriPost)).into(imgPost);

    }

    private void dialogContents() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Material_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_content_post);
        TextView tvDone = (TextView) dialog.findViewById(R.id.dContentPost_tvDone);
        final TextView tvNothing = (TextView) dialog.findViewById(R.id.dContentPost_tvNothing);

        final ListView listView = (ListView) dialog.findViewById(R.id.dContentPost_lvContent);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void dialogComments(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_comment_post);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final Comment comment = new Comment();
        if (currentUser != null){
            comment.setDisplayName(currentUser.getDisplayName());
            comment.setUriAvatarUser(String.valueOf(currentUser.getPhotoUrl()));
            comment.setIdUser(currentUser.getUid());
            comment.setEmailUser(currentUser.getEmail());
        }

        comment.setPubDate(stringPubDate());
        comment.setLongPubDate(longPubDate());
        final EditText etComment = (EditText) dialog.findViewById(R.id.dCommentPost_etComment);
        TextView tvDone = (TextView) dialog.findViewById(R.id.dCommentPost_tvDone);
        final TextView tvNothing = (TextView) dialog.findViewById(R.id.dCommentPost_tvNothing);
        ImageView imgPost = (ImageView) dialog.findViewById(R.id.dCommentPost_imgPost);
        LinearLayout layoutComment = (LinearLayout) dialog.findViewById(R.id.dCommentPost_layoutComment);
        final ListView lvComment = (ListView) dialog.findViewById(R.id.dCommentPost_lvComment);

        if (currentUser == null){
            layoutComment.setVisibility(View.GONE);
        }
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentCmt = etComment.getText().toString();
                if (contentCmt.isEmpty()){
                    toast("Vui lòng viết bình luận");
                }else {
                    comment.setContentComment(contentCmt);
                    etComment.setText("");
                }
            }
        });

        dialog.show();

    }

    private String stringPubDate() {
        String pubDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        pubDate = format.format(calendar.getTime());
        return pubDate;
    }

    private long longPubDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}
