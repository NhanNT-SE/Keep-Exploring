package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_Content;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Event;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EditBlog extends Fragment {
    //    View
    private View view;
    private EditText etTitle;
    private TextView tvUser, tvPubDate;
    private FloatingActionButton fabAddContent;
    private ImageView imgBlog, imgContent, imgExpanded;
    private CircleImageView imgAvatarUser;
    private LinearLayout layoutPickImgContent;
    private Dialog spotDialog;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    //    DAO & Helpers
    private DAO_Auth dao_auth;
    private DAO_Blog dao_blog;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Image helper_image;
    private Helper_Date helper_date;
    //    Variables
    public static final int CHOOSE_IMAGE_BLOG = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private String idBlog;
    private ListView lvContent;
    private Blog_Details blogDetails;
    private List<Blog_Details> blogDetailsList;
    private List<Blog_Details> deleteDetailList;
    private Adapter_LV_Content adapterContent;
    private String imageBlog, title;
    private User user;
    private String folder_storage;


    public Fragment_EditBlog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_blog, container, false);
        initView();
        initVariable();
        handlerEvent();
        return view;
    }

    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
        tvUser = (TextView) view.findViewById(R.id.fEditBlog_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fEditBlog_tvPubDate);
        etTitle = (EditText) view.findViewById(R.id.fEditBlog_etTitle);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fEditBlog_fabAddContent);
        imgBlog = (ImageView) view.findViewById(R.id.fEditBlog_imgBlog);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fEditBlog_imgAvatarUser);
        imgExpanded = (ImageView) view.findViewById(R.id.fEditBlog_imgExpanded);
        lvContent = (ListView) view.findViewById(R.id.fEditBlog_lvContent);
        appBar = (AppBarLayout) view.findViewById(R.id.fEditBlog_appBar);
        toolbar = (Toolbar) view.findViewById(R.id.fEditBlog_toolbar);
    }

    private void initVariable() {
        dao_auth = new DAO_Auth(getContext());
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(view.getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        blogDetailsList = new ArrayList<>();
        deleteDetailList = new ArrayList<>();
        user = helper_sp.getUser();
        imageBlog = "";
        title = "";
        idBlog = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idBlog = bundle.getString("idBlog");
        }
    }

    private void handlerEvent() {
        helper_common.toggleBottomNavigation(getContext(), false);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        refreshListView();
        appBar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    imgExpanded.setVisibility(View.VISIBLE);
                } else {
                    imgExpanded.setVisibility(View.GONE);
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBar.setExpanded(true, true);

            }
        });
        imgBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_BLOG);
            }
        });

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                blogDetails = blogDetailsList.get(position);
                dialogLongClick();
            }
        });
        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddContent();
            }
        });
        loadData();

    }

    private void dialogAddContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_modify_content);
        blogDetails = new Blog_Details();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dModifyContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dModifyContent_imgContent);
        layoutPickImgContent = (LinearLayout) dialog.findViewById(R.id.dModifyContent_layoutPickImgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
        dTvTitle.setText("Thêm nội dung chi tiết");
        btnAdd.setText("Thêm");

        layoutPickImgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_CONTENT);
            }
        });
        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_CONTENT);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                String description = dEtDescription.getText().toString();
                if (imgContent.getVisibility() != View.VISIBLE) {
                    toast("Vui lòng chọn hình ảnh để hiển thị");
                } else if (description.isEmpty()) {
                    toast("Vui lòng thêm nội dung miêu tả");
                } else {
                    blogDetails.setContent(dEtDescription.getText().toString());
                    blogDetailsList.add(blogDetails);
                    adapterContent.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }
    private void dialogUpdateContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_modify_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dModifyContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dModifyContent_imgContent);
        layoutPickImgContent = (LinearLayout) dialog.findViewById(R.id.dModifyContent_layoutPickImgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
        imgContent.setVisibility(View.VISIBLE);
        layoutPickImgContent.setVisibility(View.GONE);
        dTvTitle.setText("Chỉnh sửa nội dung chi tiết");
        btnAdd.setText("Cập nhật");
        dEtDescription.setText(blogDetails.getContent());
        if (blogDetails.getUriImage() != null) {
            imgContent.setImageURI(blogDetails.getUriImage());
        } else {
            Picasso.get().load(blogDetails.getImg()).into(imgContent);
        }
        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_CONTENT);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                if (title.isEmpty()) {
                    toast("Vui lòng thêm nội dung miêu tả");
                } else {
                    blogDetails.setContent(dEtDescription.getText().toString());
                    refreshListView();
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
    }

    private void dialogLongClick() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_longclick);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btnEdit = (Button) dialog.findViewById(R.id.dLongClick_btnEdit);
        Button btnDelete = (Button) dialog.findViewById(R.id.dLongClick_btnDelete);
        Button btnCancel = (Button) dialog.findViewById(R.id.dLongClick_btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogDetailsList.remove(blogDetails);
                if (blogDetails.getFileName() != null) {
                    deleteDetailList.add(blogDetails);
                }
                refreshListView();
                dialog.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdateContent();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void dialogUpdateBlog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        title = etTitle.getText().toString();
        if (title.isEmpty()) {
            toast("Vui lòng thêm thông tin mô tả cho bài viết");

        } else if (blogDetailsList.size() == 0) {
            toast("Vui lòng thêm ít nhất 1 nội dung chi tiết cho bài viết");
        } else {
            dialog.setTitle("Bạn có muốn cập nhật lại bài viết?");

            dialog.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spotDialog.show();
                    updateBlog();
                }
            });
            dialog.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }

    private void deleteBlog() {
        dao_blog.deleteBlog(helper_sp.getAccessToken(), idBlog, new Helper_Callback() {
            @Override
            public void successReq(Object data) {
                toast("Đã xóa bài viết");
                dao_blog.deleteFolderImage(folder_storage, blogDetailsList);
                spotDialog.dismiss();
                helper_common.replaceFragment(getContext(), new Fragment_Tab_UserInfo());
            }

            @Override
            public void failedReq(String msg) {
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refreshToken(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            deleteBlog();
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

    private void updateBlog() {
        dao_blog.updateBlog(helper_sp.getAccessToken(), idBlog, title,
                folder_storage, imageBlog, blogDetailsList, new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        spotDialog.dismiss();
                        toast("Cập nhật bài viết thành công, bài viết hiện đang trong quá trình kiểm duyệt");
                        dao_blog.deleteFolderImage(folder_storage, deleteDetailList);
                        helper_common.replaceFragment(getContext(), new Fragment_Tab_UserInfo());
                    }

                    @Override
                    public void failedReq(String msg) {
                        if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                            refreshToken(new Helper_Callback() {
                                @Override
                                public void successReq(Object response) {
                                    updateBlog();
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



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                dialogUpdateBlog();
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
                        deleteBlog();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadData() {
        if (!spotDialog.isShowing()) {
            spotDialog.show();
        }
        dao_blog.getBlogById(idBlog, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                deleteDetailList.clear();
                Blog blog = (Blog) response;
                blogDetailsList = blog.getBlogDetails();
                Picasso.get().load(helper_common.getBaseUrlImage() + "blog/" + blog.getImage()).into(imgBlog);
                etTitle.setText(blog.getTitle());
                folder_storage = blog.getFolder_storage();
                tvPubDate.setText(helper_date.formatDateDisplay(blog.getCreated_on()));
                refreshListView();
                spotDialog.dismiss();
            }

            @Override
            public void failedReq(String msg) {
                spotDialog.dismiss();
            }
        });
    }

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_BLOG && data != null) {
            imgBlog.setImageURI(data.getData());
            imageBlog = helper_image.getPathFromUri(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
            layoutPickImgContent.setVisibility(View.GONE);
            imgContent.setVisibility(View.VISIBLE);
            imgContent.setImageURI(data.getData());
            blogDetails.setUriImage(data.getData());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void refreshToken(Helper_Callback callback) {
        dao_auth.refreshToken(callback);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}
