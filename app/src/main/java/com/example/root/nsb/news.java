package com.example.root.nsb;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.DrawableRes;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    //Layouts
    FrameLayout button_layout;
    LinearLayout iconsLayout;
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
    private int  currentFragment;
    GestureDetector gestureDetector;
    //Button
    private ImageView homeIcon;
    private ImageView categoryIcon;
    private ImageView searchIcon;
    private ImageView forumIcon;
    private ImageView profileIcon;
    private Button newsButton;
    private Button searchButton;
    private Button forumButton;
    private Button profileButton;
    private Button categoryButton;
    //
    private ArrayList<_newsCh> GL_arrayList_TO_NEWS;
    //endButton

    //Transitions
    private void listTransitionToNews(int position)
    {
        start_load_screen_animation_IN();
        fragmentsManager(15);
        new load_newspage().execute(GL_arrayList_TO_NEWS.get(position));

    }

    //End Transitions
    private int createNewsList(final ArrayList<_newsCh> arrayList)
    {
        if (arrayList != null)
        {
            GL_arrayList_TO_NEWS=new ArrayList<>();
            GL_arrayList_TO_NEWS=arrayList;
            newsList.setContext(context);
            listView = newsList.setListView(arrayList);

        }
        return 0;
    }
    //Overrides methods
    @Override
    public void setTitleText(String name,int resid) {
        if(name!=null)
            TitleBar.setText(name);
        else
           if(resid!=0)TitleBar.setText(resid);
    }

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
        button_layout=findViewById(R.id.buttons);
        iconsLayout=findViewById(R.id.buttons_icon);
        //Icons
        homeIcon=findViewById(R.id.home_icon);
        categoryIcon=findViewById(R.id.category_icon);
        searchIcon=findViewById(R.id.search_icon);
        forumIcon=findViewById(R.id.forum_icon);
        profileIcon=findViewById(R.id.profile_icon);
        //endIcons
        //endButton
        TitleBar = findViewById(R.id.TitleText);
        TitleBar.setText(R.string.ActionBarNewsButton);

        setButtonsOnClickListeners();

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
    public void onClickNewsListItem(int position) {
        listTransitionToNews(position);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 )
            {
                getFragmentManager().popBackStack();
            }
        else
            {
            super.onBackPressed();
            }
    }
    //
    //UI манипуляции
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
            fragmentsManager(10);
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
            createNewsList(GL_arrayList_TO_NEWS);
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
        animation = AnimationUtils.loadAnimation(context,R.anim.fadeout);
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

    private void showButtons(int b)
    {
        switch (b)
        {
            case View.VISIBLE:
            {   button_layout.setVisibility(b);
                break;
            }
            case View.GONE:
            {
                button_layout.setVisibility(b);
                break;
            }
        }
    }
    private void iconAnimation(final String Tag)
    {
        switch (Tag)
        {
            case "category":
            {
                categoryIcon.setBackgroundResource(R.drawable.category);
                categoryIcon.setBackgroundResource(R.drawable.cateogry_icon_animation);
                AnimationDrawable frameAnimation = (AnimationDrawable) categoryIcon.getBackground();
                frameAnimation.start();
                break;
            }
            case "-a GONE":
            {
                homeIcon.setVisibility(View.GONE);
                categoryIcon.setVisibility(View.GONE);
                searchIcon.setVisibility(View.GONE);
                forumIcon.setVisibility(View.GONE);
                profileIcon.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void setButtonsOnClickListeners()
    {
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentsManager(11);
            }
        });
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentsManager(20);
            }
        });
    }

    private void fragmentsManager(int fragmentId)
    {
        switch (fragmentId)
        {
            case 10:
            {
                currentFragment=10;
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragment, newsList,"NewsList")
                        .commit();
                break;
            }
            case 11:
            {   if(currentFragment!=11)
                {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack("NewsList")
                            .replace(R.id.fragment, newsList, "NewsList")
                            .commit();
                }
                break;
            }
            case 20:
            {
                if(currentFragment!=20)
                {
                    currentFragment=20;
                    fragmentManager.beginTransaction()
                            .addToBackStack("Category")
                            .replace(R.id.fragment,categoryFragment,"Category")
                            .commit();
                    iconAnimation("category");
                    TitleBar.setText(R.string.ActionBarCategoryButton);
                }
                break;
            }
            case 15:
            {
                currentFragment=15;
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment,newsPageFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            }
        }
    }
    //
}


