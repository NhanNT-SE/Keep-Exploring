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
import com.example.keep_exploring.helpers.Helper_Event;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EditBlog extends Fragment {
    //    View
    private View view;
    private EditText etTitle;
    private TextView tvUser, tvPubDate;
    private FloatingActionButton fabAddContent;
    private ImageView imgBlog, imgContent;
    private CircleImageView imgAvatarUser;
    //    Variables
    public static final int CHOOSE_IMAGE_BLOG = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private String idBlog;
    private ListView lvContent;
    private Blog_Details blogDetails;
    private List<Blog_Details> blogDetailsList;
    private List<Blog_Details> deleteDetailList;
    private Adapter_LV_Content adapterContent;
    private int index = -1;
    private String imageBlog = "";
    private User user;
    //    DAO & Helpers
    private DAO_Blog dao_blog;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Image helper_image;
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
        tvUser = (TextView) view.findViewById(R.id.fEditBlog_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fEditBlog_tvPubDate);
        etTitle = (EditText) view.findViewById(R.id.fEditBlog_etTitle);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fEditBlog_fabAddContent);
        imgBlog = (ImageView) view.findViewById(R.id.fEditBlog_imgBlog);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fEditBlog_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fEditBlog_lvContent);
    }

    private void initVariable() {
        dao_blog = new DAO_Blog(getContext());
        helper_sp = new Helper_SP(view.getContext());
        helper_common = new Helper_Common();
        helper_image = new Helper_Image(getContext());
        blogDetailsList = new ArrayList<>();
        deleteDetailList = new ArrayList<>();
        user = helper_sp.getUser();
        reloadData();
    }

    private void handlerEvent() {
        helper_common.formatDate(tvPubDate);
        tvUser.setText(user.getDisplayName());
        Picasso.get().load(helper_common.getBaseUrlImage() + "user/" + user.getImgUser()).into(imgAvatarUser);
        refreshListView();
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
                index = position;
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

    private void dialogAddContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_modify_content);
        blogDetails = new Blog_Details();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dModifyContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dModifyContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
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
        Button btnAdd = (Button) dialog.findViewById(R.id.dModifyContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dModifyContent_btnCancel);
        TextView dTvTitle = (TextView) dialog.findViewById(R.id.dModifyContent_tvTitle);
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
    private void uploadData() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (etTitle.getText().toString().isEmpty()) {
            toast("Vui lòng thêm thông tin mô tả cho bài viết");

        } else if (blogDetailsList.size() == 0) {
            toast("Vui lòng thêm ít nhất 1 nội dung chi tiết cho bài viết");
        } else {
            dialog.setTitle("Bạn có muốn cập nhật lại bài viết?");

            dialog.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = etTitle.getText().toString();

                    dao_blog.updateBlog(idBlog, title, folder_storage, imageBlog, blogDetailsList, new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            toast("Cập nhật bài viết thành công, bài viết hiện đang trong quá trình kiểm duyệt");
                            dao_blog.deleteFolderImage(folder_storage,deleteDetailList);
                            reloadData();
                        }

                        @Override
                        public void failedReq(String msg) {

                        }
                    });
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
//    private void currentFragment(String current) {
//        if (current.equalsIgnoreCase("Restaurants")) {
//            replaceFragment(new Fragment_Restaurant());
//        } else if (current.equalsIgnoreCase("Accommodations")) {
//            replaceFragment(new Fragment_Accommodations());
//        } else if (current.equalsIgnoreCase("Beautiful Places")) {
//            replaceFragment(new Fragment_BeautifulPlaces());
//        } else {
//            replaceFragment(new Fragment_JourneyDiary());
//        }
//
//    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_FrameLayout, fragment)
                .addToBackStack(null)
                .commit();

    }


    private void clearBlog() {
        deleteDetailList.clear();
        etTitle.setText("");
        imgBlog.setImageResource(R.drawable.add_image);
        imageBlog = "";
        blogDetailsList.clear();
        adapterContent.notifyDataSetChanged();
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
                uploadData();
                break;
            case R.id.menu_post_clear:
                reloadData();
                break;
            case R.id.menu_post_delete:
                String message = "Bạn muốn xóa bài viết cùng toàn bộ nội dung liên quan?";
                helper_common.alertDialog(getContext(), message, new Helper_Event() {
                    @Override
                    public void onSubmitAlertDialog() {
                        dao_blog.deleteBlog(idBlog, new Helper_Callback() {
                            @Override
                            public void successReq(Object data) {
                                if (data != null) {
                                    toast("Đã xóa bài viết");
                                    dao_blog.deleteFolderImage(folder_storage,blogDetailsList);
                                    clearBlog();

                                }
                            }

                            @Override
                            public void failedReq(String msg) {

                            }
                        });
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_BLOG && data != null) {
            imgBlog.setImageURI(data.getData());
            imageBlog = helper_image.getPathFromUri(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
            imgContent.setImageURI(data.getData());
            blogDetails.setUriImage(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reloadData() {
        dao_blog.getBlogById("607301792f992d3c302ab66d", new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                deleteDetailList.clear();
                Blog blog = (Blog) response;
                blogDetailsList = blog.getBlogDetails();
                Picasso.get().load(helper_common.getBaseUrlImage() + "blog/" + blog.getImage()).into(imgBlog);
                etTitle.setText(blog.getTitle());
                idBlog = blog.get_id();
                folder_storage = blog.getFolder_storage();
                refreshListView();
            }

            @Override
            public void failedReq(String msg) {
            }
        });
    }

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}
