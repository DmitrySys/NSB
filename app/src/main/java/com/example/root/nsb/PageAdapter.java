package com.example.root.nsb;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.List;

public class PageAdapter extends PagerAdapter {
    private List<View> pages=null;
    public PageAdapter(List<View> pages)
    {
        this.pages=pages;
    }
    @Override
    public Object instantiateItem(View collection,int position)
    {
        View v = pages.get(position);
        ((ViewPager) collection).addView(v,0);
        return v;
    }
    @Override
    public void destroyItem(View collection, int position, Object view){
        ((ViewPager) collection).removeView((View) view);
    }

    public int getCount(){
        return pages.size();
    }

    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }
    @Override
    public void finishUpdate(View arg0){
    }

    public void restoreState(Parcelable arg0, ClassLoader arg1){
    }

    public Parcelable saveState(){
        return null;
    }
    @Override
    public void startUpdate(View arg0){
    }
}

