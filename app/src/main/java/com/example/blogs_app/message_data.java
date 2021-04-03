package com.example.blogs_app;


import android.net.Uri;

public class message_data {

    private String title,description,user_name,user_id,post_image,user_image,email;
    private  String post_key, date;

    public message_data()
    {

    }

    public message_data(String title, String description, String user_name, String user_id, String post_image, String user_image, String email, String post_key, String date) {
        this.title = title;
        this.description = description;
        this.user_name = user_name;
        this.user_id = user_id;
        this.post_image = post_image;
        this.user_image = user_image;
        this.email = email;
        this.post_key = post_key;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
