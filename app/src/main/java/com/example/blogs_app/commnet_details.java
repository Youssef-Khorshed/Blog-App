package com.example.blogs_app;

public class commnet_details {
    private String comment,posyt_id,comment_id,commnet_date,user_name,user_id,user_img;

    public commnet_details(String comment, String posyt_id, String comment_id, String commnet_date, String user_name, String user_id, String user_img) {
        this.comment = comment;
        this.posyt_id = posyt_id;
        this.comment_id = comment_id;
        this.commnet_date = commnet_date;
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_img = user_img;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPosyt_id() {
        return posyt_id;
    }

    public void setPosyt_id(String posyt_id) {
        this.posyt_id = posyt_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCommnet_date() {
        return commnet_date;
    }

    public void setCommnet_date(String commnet_date) {
        this.commnet_date = commnet_date;
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

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }
}
