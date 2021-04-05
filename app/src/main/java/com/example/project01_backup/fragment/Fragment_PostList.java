package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_PostUser;

import com.example.project01_backup.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_PostList extends Fragment {
    private View view;
    private ListView lvPost;
    private TextView tvNothing;
    private Adapter_LV_PostUser adapterPost;
    private List<Post> listPost;

    private FirebaseUser currentUser;
    private int index = -1;

    public Fragment_PostList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_list, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        lvPost = (ListView) view.findViewById(R.id.fPostList_lvPost);
        tvNothing = (TextView) view.findViewById(R.id.fPostList_tvNothing);


        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment_Blog_Detail fragment_blog_detail = new Fragment_Blog_Detail();
                Bundle bundle = new Bundle();
                Post post = listPost.get(position);
                bundle.putSerializable("post", post);
                fragment_blog_detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout, fragment_blog_detail)
                        .addToBackStack(null)
                        .commit();
            }
        });
        lvPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                index = position;
                dialogLongClick();
                return true;
            }
        });
        return view;
    }
    private void dialogLongClick(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_longclick);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Post post = listPost.get(index);
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
                final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getActivity());
                dialogDelete.setMessage("Delete an article?");
                dialogDelete.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        dialog.dismiss();
                    }
                });
                dialogDelete.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog.dismiss();
                    }
                });
                dialogDelete.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_EditBlog fragment_editBlog = new Fragment_EditBlog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                fragment_editBlog.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout, fragment_editBlog)
                        .addToBackStack(null)
                        .commit();


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void toast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
