package com.example.keep_exploring.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.R;
import com.example.keep_exploring.adapter.Adapter_LV_PostUser;

import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Restaurant extends Fragment {
    private View view;

    private List<String> placeNames;
    private TextView tvTitle, tvNothing;
    private ListView listView;
    private Adapter_LV_PostUser adapterPost;
    private FirebaseUser user;
    private FloatingActionButton fbaAdd;
    private List<Post> listPost;
    private String categoryNode;
    private DAO_Post dao_post;


    public Fragment_Restaurant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        initView();
        dao_post = new DAO_Post(view.getContext());
        dao_post.getPostByCategory("", new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Post> postList = (List<Post>) response;
                log(postList.toString());
                refreshLV(postList);
            }

            @Override
            public void failedReq(String msg) {
            }
        });
        return view;
    }
    private void initView() {
        tvTitle = (TextView) view.findViewById(R.id.fRestaurant_tvTitle);
        tvNothing = (TextView) view.findViewById(R.id.fRestaurant_tvNothing);
        fbaAdd = (FloatingActionButton) view.findViewById(R.id.fRestaurant_fabAddPost);
        listView = (ListView) view.findViewById(R.id.fRestaurant_lvPost);
        categoryNode = "Restaurants";
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        if (user == null) {
            fbaAdd.setVisibility(View.GONE);
        }
        fbaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_AddPost addPost = new Fragment_AddPost();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout, addPost)
                        .addToBackStack(null)
                        .commit();
            }
        });

        placeNames = new ArrayList<>();
    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void refreshLV(List<Post> postList) {
        listPost = new ArrayList<>(postList);
        adapterPost = new Adapter_LV_PostUser(getContext(), listPost);
        listView.setAdapter(adapterPost);
        if (postList.size() > 0) {
            tvNothing.setVisibility(View.GONE);
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_places, menu);
        MenuItem search = menu.findItem(R.id.menu_search_places);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Please, enter a location");
        final SearchView.SearchAutoComplete autoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoComplete.setTextColor(Color.WHITE);
        autoComplete.setDropDownBackgroundResource(android.R.color.white);
        autoComplete.setThreshold(1);
        final MenuItem refresh = menu.findItem(R.id.menu_search_refresh);
        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
