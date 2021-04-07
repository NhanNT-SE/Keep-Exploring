package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_Content;

import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private View view;
    private EditText etTitle, etAddress, etDescription;
    private TextView tvUser, tvPubDate;
    private AutoCompleteTextView acPlace;
    private Spinner spnCategory;
    private FloatingActionButton fabAddContent;
    private ImageView imgPost, imgContent;
    private CircleImageView imgAvatarUser;
    private ListView lvContent;
    private FirebaseUser user;

    private Post post, oldPost;
    private List<Blog_Details> listBlogDetails;
    private List<String> nameList;
    private Adapter_LV_Content adapterContent;
    private String idPost;

    public static final int CHOOSE_IMAGE_POST = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private int index = -1;
    private Blog_Details blogDetails;

    public Fragment_EditBlog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_blog, container, false);
        initView();
        return view;
    }

    private void initView() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        post = new Post();
        Bundle bundle = getArguments();
        listBlogDetails = new ArrayList<>();
        spnCategory = (Spinner) view.findViewById(R.id.fEditPost_spnCategory);
        acPlace = (AutoCompleteTextView) view.findViewById(R.id.fEditPost_acPlace);
        tvUser = (TextView) view.findViewById(R.id.fEditPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fEditPost_tvPubDate);
        etDescription = (EditText) view.findViewById(R.id.fEditPost_etDescription);
        etTitle = (EditText) view.findViewById(R.id.fEditPost_etTitle);
        etAddress = (EditText) view.findViewById(R.id.fEditPost_etAddress);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fEditPost_fabAddContent);
        imgPost = (ImageView) view.findViewById(R.id.fEditPost_imgPost);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fEditPost_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fEditPost_lvContent);

        adapterContent = new Adapter_LV_Content(getActivity(), listBlogDetails);
        lvContent.setAdapter(adapterContent);
        nameList = new ArrayList<>();
        String[] categoryList = {"Restaurants", "Accommodations", "Beautiful Places", "Journey Diary"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, categoryList);
        spnCategory.setAdapter(adapterSpinner);
        acPlace.setThreshold(1);


        setPubDate(tvPubDate);
        tvUser.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(imgAvatarUser);

        if (bundle != null) {
            oldPost = (Post) bundle.getSerializable("post");
            String category = null;
            String categoryOldPost = oldPost.getCategory();
            if (categoryOldPost.equalsIgnoreCase("restaurants")) {
                category = "Ăn Uống";
            } else if (categoryOldPost.equalsIgnoreCase("accommodations")) {
                category = "Chỗ Ở";
            } else if (categoryOldPost.equalsIgnoreCase("beautiful places")) {
                category = "Check in";
            } else if (categoryOldPost.equalsIgnoreCase("journey diary")) {
                category = "Trải Nghiệm";
            }
//            idPost = oldPost.getIdPost();
//            int position = adapterSpinner.getPosition(category);
//            acPlace.setText(oldPost.getPlace());
//            spnCategory.setSelection(position);
//            etTitle.setText(oldPost.getTittle());
//            etAddress.setText(oldPost.getAddress());
//            etDescription.setText(oldPost.getDescription());
//            Picasso.get().load(Uri.parse(oldPost.getUrlImage())).into(imgPost);
        }

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
                    listBlogDetails.add(blogDetails);
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
        final Blog_Details update = listBlogDetails.get(index);
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
                String description = etDescription.getText().toString();
                if (imgContent.getDrawable() == null) {
                    toast("Please, choose a picture");

                } else if (description.isEmpty()) {
                    toast("Please, add a description");
                } else {
                    blogDetails.setContent(dEtDescription.getText().toString());
                    listBlogDetails.add(index, blogDetails);
                    listBlogDetails.remove(update);
                    adapterContent.notifyDataSetChanged();
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
        final Blog_Details delete = listBlogDetails.get(index);
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
                listBlogDetails.remove(delete);
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

    private void uploadData() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final String categoryNode = spnCategory.getSelectedItem().toString();
        final String placeNode = acPlace.getText().toString();
//        post.setIdPost(oldPost.getIdPost());
//        post.setAddress(etAddress.getText().toString());
//        post.setDescription(etDescription.getText().toString());
//        post.setPubDate(tvPubDate.getText().toString());
//        post.setEmailUser(tvUser.getText().toString());
//        post.setTittle(etTitle.getText().toString());
//        post.setLongPubDate(longPubDate());
//        post.setUrlAvatarUser(String.valueOf(user.getPhotoUrl()));
//        post.setIdUser(user.getUid());
//        post.setDisplayName(user.getDisplayName());


        if (etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() ||
                etAddress.getText().toString().isEmpty() || (imgPost.getDrawable() == null)) {
            toast("Please, fill up the form");
        } else if (listBlogDetails.size() == 0) {
            dialog.setMessage("The article has no detailed description. Still submit?.");


            dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } else {
            dialog.setTitle("Submit an Article?");

            dialog.setNegativeButton("SUBMIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < listBlogDetails.size(); i++) {
                        Blog_Details upload = new Blog_Details();
                        Uri uri = listBlogDetails.get(i).getUriImage();
                        upload.setImg(listBlogDetails.get(i).getImg());
                        upload.setContent(listBlogDetails.get(i).getContent());
                    }
                    currentFragment(categoryNode);

                    toast("Pending moderation!");
                }
            });
            dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }


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
                .addToBackStack(null)
                .commit();

    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private long longPubDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    private void clearPost() {
        etTitle.setText("");
        etAddress.setText("");
        etDescription.setText("");
        imgPost.setImageResource(R.drawable.add_image);
        listBlogDetails.clear();
        acPlace.setText("");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                uploadData();
                break;
            case R.id.menu_post_clear:
                clearPost();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            imgPost.setImageURI(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
            imgContent.setImageURI(data.getData());
            blogDetails.setUriImage(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
