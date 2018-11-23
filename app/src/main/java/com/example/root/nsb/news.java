package com.example.root.nsb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    Context context = news.this;
    //Views
    ViewGroup start_scene;
    //endViews
    CacheManager managerCashe;
    //
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //
    private Animation animation;
    //
    GestureDetector gestureDetector;
    //Button
    private Button newsButton;
    private Button searchButton;
    private Button forumButton;
    private Button profileButton;
    private newsListAdapter listAdapter;
    private ArrayList<_newsCh> GL_arrayList_TO_NEWS;
    //endButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Log.d(TAG, "onCreate: Start");
        //Button
        newsButton=findViewById(R.id.newsButton);
        searchButton=findViewById(R.id.searchButton);
        forumButton=findViewById(R.id.forumButton);
        profileButton=findViewById(R.id.profileButton);
        //endButton
        //Swipe update
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
        //Start this Activity

        start_scene = (ViewGroup) findViewById(R.id.start);
        start_scene.setVisibility(View.VISIBLE);
        showButtons(View.GONE);

        new start_load_cashe().execute();// Start blue scene
        //ensStart this Activity
        //
        Log.d(TAG,"onCreate: Successful");
    }
    public class webReader extends AsyncTask<Void,Void,Void> {
        private ArrayList<_newsCh> arrayList=new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"webReader: Start");
            String url = "http://moyaokruga.ru/bur-gaz/";
            Document document=null;
            try
            {
                document = Jsoup.connect(url).get();
                siteReader sR= new siteReader(document);
                _newsCh array = new _newsCh();
                arrayList=array.pullData(sR.readTexts("div.txt-articles > header > h3 > a")
                        , sR.readTexts("div.txt-articles > p")
                        , sR.readHref("div.txt-articles > header > h3 > a")
                        ,array.getArrayByteArrayFromBitmap(sR.readImageToBitmap("http://moyaokruga.ru","div.news-container > article.articles.clearfix > a > img"))
                        ,sR.readTexts("div.txt-articles > header > p> a")
                        ,sR.readTexts("div.txt-articles > header > p >time")
                        );
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
            new putCashe().execute(arrayList);
            super.onPostExecute(result);
            setAdapterToNewsList(arrayList);
            Log.d(TAG,"webReader.onPostExecute: Successful ");
        }
    }
    private void listTransitionToNews(int position)
    {
        new load_newspage().execute(GL_arrayList_TO_NEWS.get(position));
        ViewGroup currentScene = (ViewGroup) findViewById(R.id.frame);
        Scene scene = Scene.getSceneForLayout(currentScene,R.layout.news_page,news.this);
        TransitionSet set=new TransitionSet()
                .addTransition(new Fade())
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .setInterpolator(new AccelerateInterpolator());
        TransitionManager.go(scene,set);
        gestureDetector = initGestureDetector();
        View view = findViewById(R.id.news_page);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    private void NewsTransitionToList()
    {
        ViewGroup currentScene = (ViewGroup) findViewById(R.id.news_page);
        Scene scene = new Scene((ViewGroup) findViewById(R.id.frame));
        TransitionSet set=new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new ChangeBounds())
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .setInterpolator(new AccelerateInterpolator());
        TransitionManager.go(scene,set);
        start_scene.setVisibility(View.VISIBLE);

        setAdapterToNewsList(GL_arrayList_TO_NEWS);
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

    private int setAdapterToNewsList(final ArrayList<_newsCh> arrayList)
    {
        if (arrayList != null)
        {
            Log.d(TAG,"webReader.onPostExecute: arraylist!=null");
            //   listAdapter.notifyDataSetChanged();
            listAdapter = new newsListAdapter(news.this,arrayList);
            GL_arrayList_TO_NEWS=new ArrayList<>();
            GL_arrayList_TO_NEWS=arrayList;
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
        return 0;
    }
    public class putCashe extends AsyncTask<ArrayList<_newsCh>,Void,Void>
    {
        @Override
        protected Void doInBackground(ArrayList<_newsCh>... arrayLists) {
            for(ArrayList<_newsCh> item:arrayLists) {
                ArrayList<_newsCh> ArrayList = item;
                managerCashe = new CacheManager(context, "listCashe", 52428800L);
                int i = 0;
                for (_newsCh Item:item) {
                    managerCashe.CashedData(Item, i + ".csh");
                    i++;
                }
            }
            return null;
        }
    }
    public class getCashe extends AsyncTask<String,Void,Void>
    {
        private ArrayList<_newsCh> arrayList=new ArrayList<>();
        @Override
        protected Void doInBackground(String... filenames)
        {
            if(managerCashe.check_cash())
                new webReader().execute();
            return  null;
        }
        protected void onPostExecute(Void result) {
            Log.d(TAG,"getCashe.onPostExecute: Start ");
            super.onPostExecute(result);
            Log.d(TAG,"getCashe.onPostExecute: Successful ");
        }
    }
    public class start_load_cashe extends AsyncTask<Void,Void,ArrayList<_newsCh>>
    {
        @Override
        protected ArrayList<_newsCh> doInBackground(Void ... args)
        {
            managerCashe = new CacheManager(context, "listCashe", 52428800L);
            if(managerCashe.check_cash()){return managerCashe.startScreenRead();}
            else {new webReader().execute();return null;}
        }
        @Override
        protected void onPostExecute(ArrayList<_newsCh> result) {
            ViewGroup buttons = (ViewGroup) findViewById(R.id.buttons);
            animation = AnimationUtils.loadAnimation(news.this,R.anim.scale_animator);
            start_scene.startAnimation(animation);
            Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                start_scene.setVisibility(View.GONE);
                showButtons(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };
            animation.setAnimationListener(animationListener);
            if(result!=null)
            {
                setAdapterToNewsList(result);
            }
        }
    }
    public class load_newspage extends AsyncTask<_newsCh,Void,newsPage>
    {
        newsPage page;
        @Override
        protected newsPage doInBackground(_newsCh... newsChes) {
            String url = newsChes[0].getLink();
            Document document=null;
            Bitmap[] buf = new Bitmap[1];
            buf[0] = newsChes[0].getBitmapfromByteArray(newsChes[0].getImg());
            try {
                document = Jsoup.connect(url).get();
                siteReader sR = new siteReader(document);
                page= new newsPage(newsChes[0].getTitle()
                        ,sR.readText("section.articles-main > article > p")
                        ,sR.readIntroNewsPage("div#MainMasterContentPlaceHolder_InsidePlaceHolder_articleText > p")
                        ,newsChes[0].getDate()
                        ,sR.readText("a#MainMasterContentPlaceHolder_InsidePlaceHolder_authorName.red")
                        ,newsChes[0].getCategory()
                        ,buf
                );
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
                return page;
            }

        @Override
        protected void onPostExecute(newsPage page) {
            super.onPostExecute(page);
            pageApapter(page);

        }
    }
    private void showButtons(int b)
    {
        newsButton.setVisibility(b);
        searchButton.setVisibility(b);
        forumButton.setVisibility(b);
        profileButton.setVisibility(b);
    }
    protected void pageApapter(newsPage page)
    {
        TextView title = findViewById(R.id.news_page_Title);
        TextView preIntro = findViewById(R.id.news_page_preIntro);
        TextView intro = findViewById(R.id.news_page_Intro);
        ImageView imageSwitcher = findViewById(R.id.news_page_ImageSwitcher);
        title.setText(page.getTitle());
        preIntro.setText(page.getPreIntro());
        intro.setText(page.getIntro());
        Bitmap img = page.getImglist()[0];
        imageSwitcher.setImageBitmap(img);
    }

}

