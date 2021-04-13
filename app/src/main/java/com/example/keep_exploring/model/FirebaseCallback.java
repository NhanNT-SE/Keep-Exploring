package com.example.keep_exploring.model;

import java.util.List;

public class FirebaseCallback {
    public void placesList (List<Places> placesList){}
    public void commentList (List<Comment> commentList){}
    public void postListAdmin (List<Post> postList){}
    public void postListUser (List<Post> postList){}
    public void postListPlace (List<Post> postList){}
    public void contentListAdmin (List<Blog_Details> blogDetailsList){}
    public void contentListUser (List<Blog_Details> blogDetailsList){}
    public void userList (List<User> userList){}
    public void feedbackList(List<Feedback> feedbackList){}
    public void getUser(User user){};
}