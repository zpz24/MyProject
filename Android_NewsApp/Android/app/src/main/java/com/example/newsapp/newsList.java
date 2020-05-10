package com.example.newsapp;

import java.util.List;

public class newsList {

    private String title;
    private String date;
    private String section;
    private String img;
    private String shareUrl;
    private String id;

//    public newsList(){
//
//    }

    public newsList(String title, String date, String section, String img, String shareUrl, String id){
        this.title = title;
        this.date = date;
        this.section = section;
        this.img = img;
        this.shareUrl = shareUrl;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getSection(){
        return section;
    }

    public String getImg(){
        return img;
    }

    public String getShareUrl() { return shareUrl;}

    public String getId() {return id;}
}
