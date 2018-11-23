package com.example.root.nsb;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;


public class news extends Activity implements onSomeEventListener {
    TextView TitleBar;
    private ListView listView;
    private static final  String TAG = "MyApp";
    //Booleans
    private boolean isAnimationNowLoadScreen=true;
    //
    FrameLayout button_layout;
    //
    Context context = news.this;
    //Views
    View frame;
    ViewGroup start_scene;
    //endViews
    //
    FragmentManager fragmentManager = getFragmentManager();
    news_list newsList = new news_list();
    CategoryFragment categoryFragment = new CategoryFragment();
    NewsPageFragment newsPageFragment = new NewsPageFragment();
    //
    CacheManager managerCashe;
    //
    private Animation animation;
    //
    GestureDetector gestureDetector;
    //Button
    private Button newsButton;
    private Button searchButton;
    private Button forumButton;
    private Button profileButton;
    private Button categoryButton;
    private ViewGroup buttons;
    //
    private newsListAdapter listAdapter;
    private ArrayList<_newsCh> GL_arrayList_TO_NEWS;
    //endButton

    private void listTransitionToNews(int position)
    {
        start_load_screen_animation_IN();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,newsPageFragment)
                .addToBackStack(null)
                .commit();
        new load_newspage().execute(GL_arrayList_TO_NEWS.get(position));

    }

    private int setAdapterToNewsList(final ArrayList<_newsCh> arrayList)
    {
        if (arrayList != null)
        {
            Log.d(TAG,"webReader.onPostExecute: arraylist!=null");
            listAdapter = new newsListAdapter(news.this,arrayList);
            GL_arrayList_TO_NEWS=new ArrayList<>();
            GL_arrayList_TO_NEWS=arrayList;
            newsList.setContext(context);
            listView = newsList.setListView(listAdapter,arrayList);
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
    protected void toCategory()
    {
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment,categoryFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }
    protected void toNews()
    {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        }
    }
    //Overrides methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_news);
        Log.d(TAG, "onCreate: Start");
        //Button
        newsButton=findViewById(R.id.newsButton);
        searchButton=findViewById(R.id.searchButton);
        forumButton=findViewById(R.id.forumButton);
        profileButton=findViewById(R.id.profileButton);
        categoryButton=findViewById(R.id.categoryButton);
        buttons= (ViewGroup) findViewById(R.id.buttons);
        button_layout=findViewById(R.id.buttons);
        //endButton
        TitleBar = findViewById(R.id.TitleText);
        TitleBar.setText(R.string.ActionBarNewsButton);
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNews();
            }
        });
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCategory();
                TitleBar.setText(R.string.ActionBarCategoryButton);
            }
        });
        //Swipe update
        //end swipe update
        //Start this Activity
        showButtons(View.GONE);
        frame = findViewById(R.id.frame);
        start_load_screen_animation_IN();
        new start_load_cashe().execute();// Start blue scene
        //ensStart this Activity
        Log.d(TAG,"onCreate: Successful");
    }

    @Override
    public void someEvent(String s) {
        if(s.equals("Waiting"))new waitingAnimationFinish().execute();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    //
    //UI манипуляции

    //Get int b: GONE,VISIBLE.
    private void showButtons(int b)
    {
        button_layout.setVisibility(b);
    }

    //
    // AsyncTasks
    public class start_load_cashe extends AsyncTask<Void,Void,ArrayList<_newsCh>>
    {
        @Override
        protected ArrayList<_newsCh> doInBackground(Void ... args)
        {
            managerCashe = new CacheManager(context, "listCashe", 52428800L);
            if(managerCashe.check_cash()){return managerCashe.startScreenRead();}
            else return null;
        }
        @Override
        protected void onPostExecute(ArrayList<_newsCh> result) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment, newsList,"NewsList")
                    .commit();
            if(result!=null)
            {
                GL_arrayList_TO_NEWS = result;
                new waitingAnimationFinish().execute();
            }
            else newsList.webRead();
        }
    }
    //

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
            newsPageFragment.pageApapter(page);
            start_load_screen_animation_OUT();
            TitleBar.setText(page.getTitle());

        }
    }

    protected class waitingAnimationFinish extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            start_load_screen_animation_OUT();
            setAdapterToNewsList(GL_arrayList_TO_NEWS);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (isAnimationNowLoadScreen);
            return null;
        }
    }
    //

    // Animations
    protected void start_load_screen_animation_IN()
    {
        isAnimationNowLoadScreen=true;
        frame.setBackgroundColor(getResources().getColor(R.color.blueActionBar));
        start_scene = (ViewGroup) findViewById(R.id.view_frame);
        start_scene.setVisibility(View.VISIBLE);
        start_scene = findViewById(R.id.load_icon);
        animation = AnimationUtils.loadAnimation(context,R.anim.fadein);
        start_scene.startAnimation(animation);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationNowLoadScreen=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
        animation.setAnimationListener(animationListener);
    }
    protected void start_load_screen_animation_OUT()
    {
        frame.setBackgroundColor(getResources().getColor(R.color.white));
        start_scene = (ViewGroup) findViewById(R.id.view_frame);
        animation = AnimationUtils.loadAnimation(news.this,R.anim.fadeout);
        start_scene.startAnimation(animation);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationNowLoadScreen=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start_scene.setVisibility(View.GONE);
                showButtons(View.VISIBLE);
                isAnimationNowLoadScreen=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation.setAnimationListener(animationListener);
    }
    private void start_circle_icon_rotate()
    {
        isAnimationNowLoadScreen=true;
        View circle_icon = findViewById(R.id.load_icon_back_text);
        animation = AnimationUtils.loadAnimation(context,R.anim.rotate_icon);
        circle_icon.startAnimation(animation);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationNowLoadScreen=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation.setAnimationListener(animationListener);
    }
    //
}


