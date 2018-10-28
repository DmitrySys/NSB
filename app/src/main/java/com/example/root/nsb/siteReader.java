package com.example.root.nsb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class siteReader {
    private Document document;
    public siteReader(Document document){this.document=document;}

    public String[] readTexts(String cssQuery) {
        Elements elements = document.select(cssQuery);
        String[] result=new String[elements.size()];
        int i=0;
        for(Element element:elements)
        {
            result[i]=element.text();
            i++;
        }

        return result;
    }

    public Bitmap[] readImageToBitmap(String domain,String cssQuery)
    {
        Elements elements = document.select(cssQuery);
        Bitmap[] result=new Bitmap[elements.size()];
        try {
            int i=0;
            for(Element element:elements)
            {
                URL _url = new URL(domain+element.attr("src"));
                HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                result[i] = BitmapFactory.decodeStream(input);
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    public String[] readHref(String cssQuery)
    {
        Elements elements = document.select(cssQuery);
        String[] result=new String[elements.size()];
        int i=0;
        for(Element element:elements)
        {
            result[i]=element.attr("href");
            i++;
        }
        return result;
    }
    public String readText(String cssQuery) {
        Elements elements = document.select(cssQuery);
        String result=new String();
        if(elements!=null)
        {
            Element element = elements.get(0);
            result=element.text();
        }

        return result;
    }

    public String readIntroNewsPage(String cssQuery) {
        Elements elements = document.select(cssQuery);
        String result=new String();
        String buffer=new String();
        int i=0;
        for(Element element:elements)
        {   buffer=element.text();
            if(buffer.length()!=0)
                result+=element.text();
            else
                result+="\n\n   ";
            i++;
        }

        return result;
    }
}
