package com.example.keep_exploring;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.keep_exploring.model.Blog_Details;
import com.example.keep_exploring.model.ImageDisplay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Test_Firebase extends AppCompatActivity {
    private ImageView img;
    private Button btn;
    private List<Uri> uriList;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);
        storage = FirebaseStorage.getInstance();
        img = (ImageView) findViewById(R.id.aTest_img);
        btn = (Button) findViewById(R.id.aTest_btnUpload);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select picture"),
                        1);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        List<Blog_Details> blog_detailsList = new ArrayList<>();
        for (int i = 0 ; i < uriList.size(); i++) {
            Uri uri = uriList.get(i);
            storageRef = storage.getReference("Images/Blog Details/IdBlog/" + i);
            UploadTask uploadTask = storageRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Blog_Details blog_details = new Blog_Details();
                            blog_details.setImg(uri.toString());
                            blog_detailsList.add(blog_details);
                            if (blog_detailsList.size() == uriList.size()){
                                Log.d("log", "Blog Details List: " + blog_detailsList.toString());

                            }

                        }
                    });
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        uriList = new ArrayList<>();
        if (requestCode == 1 && data != null) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                img.setImageURI(uri);
                uriList.add(uri);
            }
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    uriList.add(uri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}