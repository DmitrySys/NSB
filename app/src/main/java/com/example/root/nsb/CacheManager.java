package com.example.root.nsb;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CacheManager {
    private long maxCacheSize;
    private File dir;
    Context context;
    private ObjectOutputStream objectOutputStream;
    public CacheManager(Context context,long maxCacheSize)
    {
        this(context,"/default/",maxCacheSize);
    }
    public CacheManager(Context context,String subDir, long maxCacheSize)
    {
        this.context=context;
        this.maxCacheSize=maxCacheSize;
        this.dir = new File(context.getCacheDir(),subDir);
    }
    public int CashedData(Object data,String cashefilename)
    {
        try{
            OutputStream fileOutputStream = context.openFileOutput(cashefilename, MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
            InputStream inputStream = context.openFileInput(cashefilename);
            return 0;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    public Object CasheRead(String cashefilename)
    {
        try {
            InputStream fileInputStream = context.openFileInput(cashefilename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object result = objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return result;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public Boolean check_cash()
    {
        Boolean result;
        try{InputStream inputStream = context.openFileInput("0.csh");result=true;}catch (IOException e){result=false;}
        return result;
    }
    public ArrayList<_newsCh> startScreenRead()
    {
        ArrayList<_newsCh> result = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            result.add((_newsCh) CasheRead(i+".csh"));
        }
        return result;
    }

}
