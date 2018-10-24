package com.example.root.nsb;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {
    private long maxCacheSize;
    private File dir;
    public CacheManager(Context context,long maxCacheSize)
    {
        this(context,"/default/",maxCacheSize);
    }
    public CacheManager(Context context,String subDir, long maxCacheSize)
    {
        this.maxCacheSize=maxCacheSize;
        this.dir = new File(context.getCacheDir(),subDir);
    }
    public int CashedData(Object data,String cashefilename)
    {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir,cashefilename));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
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
            FileInputStream fileInputStream = new FileInputStream(new File(dir,cashefilename));
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

}
