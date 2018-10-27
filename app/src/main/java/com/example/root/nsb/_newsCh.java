package com.example.root.nsb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class _newsCh implements Serializable {
    private String title;
    private String intro;
    private byte[] img;
    private String date;
    private String category;
    private String link;
    public _newsCh(){}
    public _newsCh(String title,String intro,byte[] img,String date,String category, String link)
    {
        this.title=title;
        this.img=img;
        this.date=date;
        this.category=category;
        this.intro=intro;
        this.link=link;
    }

    public byte[] getImg() {
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

    public void setImg(byte[] img) {
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

    public void setLink(String link) {
        this.link = link;
    }
    public String getLink() {
        return link;
    }

    public ArrayList<_newsCh> pullData(String[] title,String[] intro,String[] link,byte[][] img,String[] category,String[] date)
    {
        ArrayList<_newsCh> result=new ArrayList<>();
        _newsCh buffer;
        for(int i=0;i<title.length;i++)
        {
            buffer=new _newsCh();
            buffer.setTitle(title[i]);
            buffer.setIntro(intro[i]);
            buffer.setCategory(category[i]);
            buffer.setDate(date[i]);
            buffer.setImg(img[i]);
            buffer.setLink(link[i]);
            result.add(buffer);
        }
        return result;
    }
    public byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public Bitmap getBitmapfromByteArray(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap , 0, bitmap.length);
    }
    public byte[][] getArrayByteArrayFromBitmap(Bitmap[] bitmaps)
    {
        byte[][] result=new byte[bitmaps.length][];
        int i=0;
        for (Bitmap item:bitmaps)
        {
            result[i]=getByteArrayfromBitmap(item);
            i++;
        }
        return result;
    }
}

