package com.example.root.nsb;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.TextView;
public class newsListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<_newsCh> items;
//    LayoutInflater layoutInflater;
    int length;
    public newsListAdapter(Context mContext,ArrayList<_newsCh> items)
    {
        this.mContext=mContext;
        this.items=items;
        length=items.size();
    }
    public _newsCh getItem(int position) {
        return items.get(position);
    }

    public int getCount() {
        return length;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
           convertView=layoutInflater.inflate(R.layout.newslistitem,parent,false);
        }
            _newsCh bufObj = getItem(position);
            byte[] image=bufObj.getImg();
            ((TextView) convertView.findViewById(R.id.date)).setText(bufObj.getDate());
            ((TextView) convertView.findViewById(R.id.TitleItem)).setText(bufObj.getTitle());
            ((ImageView) convertView.findViewById(R.id.ImageItem)).setImageBitmap(BitmapFactory.decodeByteArray(image , 0,image.length));
            ((TextView) convertView.findViewById(R.id.NewsIntroItem)).setText("     "+bufObj.getIntro());
            ((TextView) convertView.findViewById(R.id.category)).setText(bufObj.getCategory());
            return convertView;
    }
}