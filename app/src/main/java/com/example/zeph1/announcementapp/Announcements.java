package com.example.zeph1.announcementapp;

/**
 * Created by zeph1 on 4/4/2018.
 */

public class Announcements {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String title;
    private String author;

    //Constructor that is used to create an instance of the Announcement object
    public Announcements(String title, String author){
        this.title = title;
        this.author = author;
    }




}
