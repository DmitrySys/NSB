package com.example.root.nsb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryGridViewAdapter extends BaseAdapter {
    private Context mContext;

    public CategoryGridViewAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView;
        if (convertView == null) {
            convertView=layoutInflater.inflate(R.layout.category_item,parent,false);
        }
        ((TextView) convertView.findViewById(R.id.categoryItemText)).setText(mTitles[position]);
        ((ImageView) convertView.findViewById(R.id.categoryItemImage)).setImageResource(mThumbIds[position]);
        return convertView;
    }

    // references to our images
     public	Integer[] mThumbIds = {R.drawable.official,R.drawable.secity,R.drawable.economics,R.drawable.art,R.drawable.education,R.drawable.medics};
    public  String[] mTitles = {"Официально","Общество","Экономика","Культура","Образование","Здоровье","Спецпроекты"};
}