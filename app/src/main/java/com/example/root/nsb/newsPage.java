package com.example.root.nsb;

import android.graphics.Bitmap;

public class newsPage {
    private String title;
    private String preIntro;
    private String intro;
    private String date;
    private String author;
    private String catrgory;
    private Bitmap[] imglist;

    public newsPage(){}
    public newsPage(String title, String preIntro,String intro,String date, String author, String catrgory)
    {
        this(title,preIntro,intro,date,author,catrgory,null);
    }
    public newsPage(String title, String preIntro,String intro,String date, String author, String catrgory, Bitmap[] imglist)
    {
        this.title=title;
        this.preIntro=preIntro;
        this.intro=intro;
        this.date=date;
        this.author=author;
        this.catrgory=catrgory;
        this.imglist=imglist;
    }

    public Bitmap[] getImglist() {
        return imglist;
    }

    public String getAuthor() {
        return author;
    }

    public String getCatrgory() {
        return catrgory;
    }

    public String getDate() {
        return date;
    }

    public String getIntro() {
        return intro;
    }

    public String getPreIntro() {
        return preIntro;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCatrgory(String catrgory) {
        this.catrgory = catrgory;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImglist(Bitmap[] imglist) {
        this.imglist = imglist;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setPreIntro(String preIntro) {
        this.preIntro = preIntro;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
