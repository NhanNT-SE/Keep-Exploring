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
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Blog;
import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Adapter_LV_Content adapterContent;
    private int index = -1;
    private String imageBlog = "";
    private User user;
    //    DAO & Helpers
    private DAO_Blog dao_blog;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    private Helper_Image helper_image;

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
        user = helper_sp.getUser();
        dao_blog.getBlogById("606eabd45250620a240fa59d", new Helper_Callback(){
            @Override
            public void successReq(Object response) {
                Blog blog = (Blog) response;
                blogDetailsList = blog.getBlogDetails();
                Picasso.get().load(helper_common.getBaseUrlImage()+"blog/"+blog.getImage()).into(imgBlog);
                etTitle.setText(blog.getTitle());
                refreshListView();
            }
            @Override
            public void failedReq(String msg) {
            }
        });
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
        dialog.setContentView(R.layout.dialog_add_content);
        blogDetails = new Blog_Details();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);

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
                    toast("Please, choose a picture");

                } else if (description.isEmpty()) {
                    toast("Please, add a description");
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
        dialog.setContentView(R.layout.dialog_add_content);
        blogDetails = new Blog_Details();
        final Blog_Details update = blogDetailsList.get(index);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);
        dEtDescription.setText(update.getContent());
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
                if (imgContent.getDrawable() == null) {
                    toast("Please, choose a picture");

                } else if (title.isEmpty()) {
                    toast("Please, add a description");
                } else {
//                    blogDetails.setContent(dEtDescription.getText().toString());
//                    blogDetailsList.add(index, blogDetails);
//                    blogDetailsList.remove(update);
//                    adapterContent.notifyDataSetChanged();
//                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void dialogLongClick() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_longclick);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Blog_Details delete = blogDetailsList.get(index);
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
                blogDetailsList.remove(delete);
                adapterContent.notifyDataSetChanged();
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

        if (imgBlog.getDrawable().getConstantState() ==
                getContext().getDrawable(R.drawable.add_image).getConstantState()) {
            toast("Vui lòng chọn hình ảnh đại diện cho bài viết");
        } else if (etTitle.getText().toString().isEmpty()) {
            toast("Vui lòng thêm thông tin mô tả cho bài viết");

        } else if (blogDetailsList.size() == 0) {
            toast("Vui lòng thêm ít nhất 1 nội dung chi tiết cho bài viết");
        } else {
            dialog.setTitle("Bạn có cập nhật lại bài viết?");

            dialog.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

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
                .addToBackStack(null)
                .commit();

    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void clearBlog() {
        etTitle.setText("");
        blogDetailsList.clear();
        adapterContent.notifyDataSetChanged();
        imgBlog.setImageResource(R.drawable.add_image);
        imageBlog = "";
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
//                uploadData();
                break;
            case R.id.menu_post_clear:
                clearBlog();
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

    private void refreshListView() {
        adapterContent = new Adapter_LV_Content(getActivity(), blogDetailsList);
        lvContent.setAdapter(adapterContent);
    }
    private void log(String s) {
        Log.d("log", s);
    }
}
