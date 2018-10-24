package com.example.root.nsb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.*;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




public class news extends Activity {
    private ListView listView;
    private static final  String TAG = "MyApp";
    //
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //
    GestureDetector gestureDetector;
    //Button
    private Button newsButton;
    private Button searchButton;
    private Button forumButton;
    private Button profileButton;
    private newsListAdapter listAdapter;
    private Transition listItemTransition;
    //endButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //Adapter
        Log.d(TAG, "onCreate: Start");
        //endAdapter
        //Button
        newsButton=findViewById(R.id.newsButton);
        searchButton=findViewById(R.id.searchButton);
        forumButton=findViewById(R.id.forumButton);
        profileButton=findViewById(R.id.profileButton);
        //endButton
        //swipe update
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_update);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new webReader().execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        //end swipe update
        //Update list
        new webReader().execute();
        //
        //
        Log.d(TAG,"onCreate: Successful");
    }
    @Override
    protected void onStart()
    {
        Log.d(TAG, "onStart: Start");
        super.onStart();
        Log.d(TAG, "onStart: Successful");
    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    public class webReader extends AsyncTask<Void,Void,Void> {
        private ArrayList<_newsCh> arrayList=new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"webReader: Start");
            String url = "http://moyaokruga.ru/bur-gaz/";
            String strImageName;
            String IMAGE_DESTINATION_FOLDER = "/home/dmitry/imgbuffer/";
            Document document=null;
            try
            {
                document = Jsoup.connect(url).get();
                Elements titles=document.select("div.txt-articles > header > h3 > a");
                Elements intro = document.select("div.txt-articles > p");
                Elements data=document.select("div.txt-articles > header > p >time");
                Elements category=document.select("div.txt-articles > header > p> a");
                Elements _imgbitmap = document.select("div.news-container > article.articles.clearfix > a > img");
                Element title;
                Element introItem;
                Element dateItem;
                Element categoryItem;
                Element imgbitmap;
                String src;
                Bitmap myBitmap;
                _newsCh bufO;

                for(int i=0;i<titles.size();i++)
                {
                    bufO=new _newsCh();
                    title=titles.get(i);
                    introItem=intro.get(i);
                    dateItem=data.get(i);
                    categoryItem=category.get(i);
                    imgbitmap=_imgbitmap.get(i);
                    bufO.setTitle(title.text());
                    bufO.setIntro(introItem.text());
                    bufO.setCategory(categoryItem.text());
                    bufO.setDate(dateItem.text());
                    src ="http://moyaokruga.ru"+imgbitmap.attr("src");

                    try {
                        URL _url = new URL(src);
                        HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(input);
                        bufO.setImg(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Add to array
                    arrayList.add(bufO);
                }
                Log.d(TAG,"webReader: Successful");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG,"webReader.onPostExecute: Start ");
            super.onPostExecute(result);
            if (arrayList != null)
            {
                Log.d(TAG,"webReader.onPostExecute: arraylist!=null");
                listAdapter = new newsListAdapter(news.this,arrayList);
                listView = findViewById(R.id.NewsList);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG,"onClickListView listener: pressed:"+position);
                        listTransitionToNews(position);
                    }
                });
            }
            Log.d(TAG,"webReader.onPostExecute: Successful ");
        }
    }
    private void listTransitionToNews(int position)
    {
        ViewGroup currentScene = (ViewGroup) findViewById(R.id.frame);
        Scene scene = Scene.getSceneForLayout(currentScene,R.layout.news_page,news.this);
        TransitionSet set=new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new ChangeBounds())
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .setInterpolator(new AccelerateInterpolator());
        TransitionManager.go(scene,set);
        gestureDetector = initGestureDetector();
        View view = findViewById(R.id.listItem);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            }
        });
    }
    private void NewsTransitionToList()
    {
        ViewGroup currentScene = (ViewGroup) findViewById(R.id.listItem);
        Scene scene = Scene.getSceneForLayout(currentScene,R.layout.activity_news,news.this);
        TransitionSet set=new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new ChangeBounds())
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .setInterpolator(new AccelerateInterpolator());
        TransitionManager.go(scene,set);
        new webReader().execute();
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
                        NewsTransitionToList();
                    }
                } catch (Exception e) {} //for now, ignore
                return false;
            }

            private void showToast(String phrase){
                Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

