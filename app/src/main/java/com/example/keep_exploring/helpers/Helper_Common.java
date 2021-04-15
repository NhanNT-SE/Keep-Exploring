package com.example.keep_exploring.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ethanhua.skeleton.Skeleton;
import com.example.keep_exploring.R;
import com.example.keep_exploring.activities.SignInActivity;
import com.example.keep_exploring.animations.Anim_Bottom_Navigation;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.model.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Helper_Common {
    public String getBaseUrl() {
        String URL_LOCAL = "http://10.0.2.2:3000";
        String URL_GLOBAL = "http://ec2-18-223-15-195.us-east-2.compute.amazonaws.com:3000";
        return URL_LOCAL;
    }

    public String getBaseUrlImage() {
        String URL_LOCAL = "http://10.0.2.2:3000/images/";
        String URL_GLOBAL = "http://ec2-18-223-15-195.us-east-2.compute.amazonaws.com:3000/images/";
        return URL_LOCAL;
    }

    public Helper_Common() {
    }

    @NonNull
    public RequestBody createPartFromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    public void configTransformerViewPager(ViewPager2 viewPager) {
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);
    }


    public void configRecycleView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
    }


    public void toggleBottomNavigation(Context context, boolean isIsShow) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) ((Activity) context).findViewById(R.id.main_coordinatorLayout);
        if (isIsShow) {
            coordinatorLayout.setVisibility(View.VISIBLE);
        } else {
            coordinatorLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void configAnimBottomNavigation(Context context, RecyclerView recyclerView) {
        recyclerView.setOnTouchListener(new Anim_Bottom_Navigation(context,
                ((Activity) context).findViewById(R.id.main_coordinatorLayout)));
    }



    public void showSkeleton(RecyclerView recyclerView, RecyclerView.Adapter adapter, int layout) {
        Skeleton.bind(recyclerView)
                .adapter(adapter)
                .load(layout)
                .shimmer(false)
                .show();
    }


    public void runtimePermission(Context context) {
        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.d("log", "All permission granted");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    public void alertDialog(Context context, String message, Helper_Event event) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                event.onSubmitAlertDialog();
            }
        });

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void dialogViewProfile(Context context, User owner) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_view_profile);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvDisplayName, tvTotalPost, tvTotalBlog, tvTotal, tvViewProfile, tvCancel;
        Helper_SP helper_sp = new Helper_SP(context);
        User user = helper_sp.getUser();
        int totalPost = owner.getPostList().size();
        int totalBlog = owner.getBlogList().size();
        int total = totalBlog + totalPost;

        CircleImageView imgAvatar = (CircleImageView) dialog.findViewById(R.id.dViewProfile_imgAvatar);
        tvDisplayName = (TextView) dialog.findViewById(R.id.dViewProfile_tvDisplayName);
        tvTotalPost = (TextView) dialog.findViewById(R.id.dViewProfile_tvTotalPost);
        tvTotalBlog = (TextView) dialog.findViewById(R.id.dViewProfile_tvTotalBlog);
        tvTotal = (TextView) dialog.findViewById(R.id.dViewProfile_tvTotal);
        tvViewProfile = (TextView) dialog.findViewById(R.id.dViewProfile_tvViewProfile);
        tvCancel = (TextView) dialog.findViewById(R.id.dViewProfile_tvCancel);

        tvDisplayName.setText(owner.getDisplayName());
        tvTotalPost.setText(String.valueOf(totalPost));
        tvTotalBlog.setText(String.valueOf(totalBlog));
        tvTotal.setText(String.valueOf(total));
        Picasso.get().load(getBaseUrlImage() + "user/" + owner.getImgUser()).into(imgAvatar);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    dialogRequireLogin(context);
                } else {
                    dialog.dismiss();
                    Fragment fragment = new Fragment_Tab_UserInfo();
                    Bundle bundle = new Bundle();
                    bundle.putString("idUser", owner.getId());
                    fragment.setArguments(bundle);
                    replaceFragment(context, fragment);
                }
            }
        });
        dialog.show();
    }

    public void dialogRequireLogin(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Vui lòng đăng nhập để thực hiện chức năng này");
        dialog.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, SignInActivity.class));
            }
        });

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void replaceFragment(Context context, Fragment fragment) {
        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_FrameLayout, fragment)
                .commit();
    }
}