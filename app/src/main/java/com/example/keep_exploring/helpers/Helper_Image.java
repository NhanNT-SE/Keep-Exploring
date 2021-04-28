package com.example.keep_exploring.helpers;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.example.keep_exploring.model.Blog_Details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Helper_Image {
    private Context context;
    private StorageReference storageBlog;
    private Helper_Date helper_date;
    private int countUpdate;

    public Helper_Image(Context context) {
        this.context = context;
    }

    public Helper_Image() {
        helper_date = new Helper_Date();
    }



    @SuppressLint("Recycle")
    public String getPathFromUri(Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        if (DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public MultipartBody.Part uploadSingle(String path, String nameUpload) {
        RequestBody requestBody;
        if (path.isEmpty()) {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        } else {
            File file = new File(path);
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        return MultipartBody.Part.createFormData(nameUpload, path, requestBody);
    }

    public List<MultipartBody.Part> uploadMulti(List<String> imageSubmitList, String nameUpload) {
        List<MultipartBody.Part> imageList = new ArrayList<>();
        for (int i = 0; i < imageSubmitList.size(); i++) {
            File file = new File(imageSubmitList.get(i));
            String path = imageSubmitList.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part singleImage = MultipartBody.Part.createFormData(nameUpload, path, requestBody);
            imageList.add(singleImage);
        }
        return imageList;
    }

    public void uploadImageBlogDetail(StorageReference storageRef,
                                      String folder_storage,
                                      List<Blog_Details> blog_detailsList,
                                      Helper_Callback callback) {
        for (int i = 0; i < blog_detailsList.size(); i++) {
            Uri uri = blog_detailsList.get(i).getUriImage();
            String fileName = helper_date.getMillisTime() + "";
            storageBlog = storageRef.child(folder_storage + "/" + fileName);
            UploadTask uploadTask = storageBlog.putFile(uri);
            int k = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadUri = task.getResult();
                            Blog_Details blog_details = blog_detailsList.get(k);
                            blog_details.setImg(downloadUri.toString());
                            blog_details.setFileName(fileName);
                            if (k + 1 == blog_detailsList.size()) {
                                callback.successReq(blog_detailsList);
                            }
                        }
                    });
                }
            });
        }
    }

    public void updateImageBlogDetail(StorageReference storageRef,
                                      String folder_storage,
                                      List<Blog_Details> blog_detailsList,
                                      Helper_Callback callback) {
        String childStorage;
        countUpdate = 0;
        List<Blog_Details> updateList = blog_detailsList.stream()
                .filter(p -> p.getUriImage() != null).collect(Collectors.toList());
        int sideList = blog_detailsList.size();
        if (updateList.size() > 0) {
            for (int i = 0; i < sideList; i++) {
                Blog_Details blog_details = blog_detailsList.get(i);
                int k = i;
                Uri uri = blog_detailsList.get(i).getUriImage();
                String fileName = helper_date.getMillisTime() + "";
                if (uri != null) {
                    countUpdate++;
                    if (blog_details.getFileName() != null) {
                        childStorage = blog_details.getFileName();
                    } else {
                        childStorage = fileName;

                    }
                    storageBlog = storageRef.child(folder_storage + "/" + childStorage);
                    if (blog_details.getFileName() == null) {
                        blog_details.setFileName(fileName);
                    }
                    UploadTask uploadTask = storageBlog.putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri downloadUri = task.getResult();
                                    Blog_Details blog_details = blog_detailsList.get(k);
                                    blog_details.setImg(downloadUri.toString());
                                    if (blog_details.getFileName() == null) {
                                        blog_details.setFileName(fileName);
                                    }
                                    if (countUpdate == updateList.size()) {
                                        callback.successReq(blog_detailsList);
                                    }
                                }
                            });
                        }
                    });
                }

            }
        } else {
            callback.successReq(blog_detailsList);
        }
    }

    public void deleteFolderImage(StorageReference storageRef, String folder_storage, List<Blog_Details> deleteList) {
        storageBlog = storageRef.child(folder_storage);
        int sizeList = deleteList.size();
        for (int i = 0; i < sizeList; i++) {
            Blog_Details blog_details = deleteList.get(i);
            StorageReference storageChild = storageBlog.child(blog_details.getFileName());
            storageChild.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

    }
}
