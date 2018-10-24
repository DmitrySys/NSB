package com.example.root.nsb;

import android.graphics.Bitmap;

public class _newsCh {
    private String title;
    private String intro;
    private Bitmap img;
    private String date;
    private String category;
    public _newsCh(){}
    public _newsCh(String title,String intro,Bitmap img,String date,String category)
    {
        this.title=title;
        this.img=img;
        this.date=date;
        this.category=category;
        this.intro=intro;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}

