package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Blog;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_Content;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddBlog extends Fragment {
    //    VIEW
    private View view;
    private EditText etTitle;
    private TextView tvUser, tvPubDate;
    private FloatingActionButton fabAddContent;
    private ImageView imgPost, imgContent;
    private CircleImageView imgAvatarUser;
    private ListView lvContent;
    private Dialog spotDialog;
    //    VARIABLES
    private User user;
    private Blog_Details blogDetails;
    private List<Blog_Details> blogDetailsList;
    private Adapter_LV_Content adapterContent;
    public static final int CHOOSE_IMAGE_POST = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private String imageBlog = "";
    //    DAO & HELPER
    private DAO_Blog dao_blog;
    private Helper_SP helper_sp;
    private Helper_Common helper_common;
    private Helper_Image helper_image;
    private Helper_Date helper_date;

    public Fragment_AddBlog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_blog, container, false);
        initView();
        initVariables();
        handlerEvents();
        return view;
    }

    private void initView() {
        spotDialog = new SpotsDialog(getActivity());
        tvUser = (TextView) view.findViewById(R.id.fAddBlog_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fAddBlog_tvPubDate);
        etTitle = (EditText) view.findViewById(R.id.fAddBlog_etTitle);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fAddBlog_fabAddContent);
        imgPost = (ImageView) view.findViewById(R.id.fAddBlog_imgPost);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fAddBlog_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fAddBlog_lvContent);
    }

    private void initVariables() {
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(view.getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        helper_date = new Helper_Date();
        blogDetailsList = new ArrayList<>();
        user = helper_sp.getUser();
    }

    private void handlerEvents() {
        helper_common.toggleBottomNavigation(getContext(),false);
        tvPubDate.setText(helper_date.formatDateDisplay(""));
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        refreshListView();
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
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
    }

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
    }

    private void dialogAddContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_modify_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        blogDetails = new Blog_Details();
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dModifyContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dModifyContent_imgContent);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        dTvTitle.setText("Thêm nội dung chi tiết");
        btnAdd.setText("Thêm");
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
                if (imgContent.getDrawable().getConstantState() ==
                        getContext().getDrawable(R.drawable.add_image).getConstantState()) {
                    toast("Vui lòng chọn hình ảnh để hiển thị");
                } else if (description.isEmpty()) {
                    toast("Vui lòng thêm nội dung miêu tả");
                } else {
                    blogDetails.setContent(dEtDescription.getText().toString());
                    blogDetailsList.add(blogDetails);
                    refreshListView();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            imgPost.setImageURI(data.getData());
            imageBlog = helper_image.getPathFromUri(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
            imgContent.setImageURI(data.getData());
            blogDetails.setUriImage(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dialogUpdateContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_modify_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dModifyContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dModifyContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
        dTvTitle.setText("Chỉnh sửa nội dung chi tiết");
        btnAdd.setText("Cập nhật");
        imgContent.setImageURI(blogDetails.getUriImage());
        dEtDescription.setText(blogDetails.getContent());

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
                String description = dEtDescription.getText().toString();
                if (description.isEmpty()) {
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
    private void uploadData() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (imgPost.getDrawable().getConstantState() ==
                getContext().getDrawable(R.drawable.add_image).getConstantState()
        ) {
            toast("Vui lòng chọn hình ảnh đại diện cho bài viết");
        } else if (etTitle.getText().toString().isEmpty()) {
            toast("Vui lòng chọn tiêu đề");
        } else if (blogDetailsList.size() == 0) {
            toast("Vui lòng thêm ít nhất 1 nội dung chi tiết cho bài viết");
        } else {
            dialog.setTitle("Bạn có muốn tạo bài viết?");
            dialog.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = etTitle.getText().toString();
                    spotDialog.show();
                    dao_blog.createBlog(blogDetailsList, title, imageBlog, new Helper_Callback() {
                        @Override
                        public void successReq(Object data) {
                            toast("Tạo bài viết thành công, bài viết hiện trong quá trình kiểm duyệt");
                            clearBlog();
                            refreshListView();
                            spotDialog.dismiss();
                        }

                        @Override
                        public void failedReq(String msg) {
                            spotDialog.dismiss();
                            toast("Có lỗi xảy ra, tạo bài viết không thành công, vui lòng thử lại sau ít phút");
                        }
                    });
                }
            });
            dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                uploadData();

                break;
            case R.id.menu_post_clear:
                    clearBlog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        menu.findItem(R.id.menu_post_delete).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void clearBlog() {
        etTitle.setText("");
        blogDetailsList.clear();
        adapterContent.notifyDataSetChanged();
        imgPost.setImageResource(R.drawable.add_image);
        imageBlog = "";
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

    private void log(String msg) {
        Log.d("log", msg);
    }
}
