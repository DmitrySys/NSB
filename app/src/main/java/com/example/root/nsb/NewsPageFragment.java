package com.example.root.nsb;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsPageFragment extends Fragment {
    TextView preIntro;
    TextView intro;
    ImageView imageSwitcher;

        public NewsPageFragment() {
        }
        public static news_list newInstance(String param) {
            news_list fragment = new news_list();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =
                    inflater.inflate(R.layout.news_page, container, false);
            preIntro =rootView.findViewById(R.id.news_page_preIntro);
            intro = rootView.findViewById(R.id.news_page_Intro);
            imageSwitcher = rootView.findViewById(R.id.news_page_ImageSwitcher);
            return rootView;
        }
    protected void pageApapter(newsPage page)
    {
        preIntro.setText(page.getPreIntro());
        intro.setText(page.getIntro());
        Bitmap img = page.getImglist()[0];
        imageSwitcher.setImageBitmap(img);
    }
    private GestureDetector initGestureDetector() {
        return new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

            private swipeDetector detector = new swipeDetector();

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                try {
                    if (detector.isSwipeDown(e1, e2, velocityY)) {
                        return false;
                    } else if (detector.isSwipeUp(e1, e2, velocityY)) {
                    }else if (detector.isSwipeLeft(e1, e2, velocityX)) {
                    } else if (detector.isSwipeRight(e1, e2, velocityX)) {
                      //  NewsTransitionToList();
                    }
                } catch (Exception e) {} //for now, ignore
                return false;
            }

            private void showToast(String phrase){
             //   Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
