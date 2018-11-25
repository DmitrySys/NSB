package com.example.root.nsb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;


public class news_list extends Fragment {
    public news_list() {
        // Required empty public constructor
    }
    private ListView listView;
    private CacheManager managerCashe;
    private ArrayList<_newsCh> GL_arrayList_TO_NEWS;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private onSomeEventListener someEventListener;
    private newsListAdapter listAdapter;
    private boolean swipe=false;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_list_news, container, false);
        listView = rootView.findViewById(R.id.NewsList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_update);
        swipeRefreshNewsSet();
        context=getActivity();
        if(GL_arrayList_TO_NEWS!=null)
        {
            someEventListener.setTitleText(null,R.string.ActionBarNewsButton);
            setNewAdapter(GL_arrayList_TO_NEWS);
        }
        return rootView;
    }

    public void webRead(){new webReader().execute();}

    public ListView setListView(ArrayList<_newsCh> arrayList)
    {
        setNewAdapter(arrayList);
        return listView;
    }

    public void setContext(Context context){this.context=context;}

    public  SwipeRefreshLayout getSwipeRefreshLayout()
    {
        return mSwipeRefreshLayout;
    }

    protected void swipeRefreshNewsSet()
    {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipe=true;
                new webReader().execute();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private class webReader extends AsyncTask<Void,Void,Void> {
        private ArrayList<_newsCh> arrayList=new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
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
            super.onPostExecute(result);
            setNewAdapter(arrayList);
            new putCashe().execute(arrayList);
            GL_arrayList_TO_NEWS=arrayList;
            if(swipe)
            {
                swipe=!swipe;
                mSwipeRefreshLayout.setRefreshing(false);
            }
            else {
                someEventListener.someEvent("Waiting");
            }
        }
    }

    private class putCashe extends AsyncTask<ArrayList<_newsCh>,Void,Void>
    {
        @Override
        protected Void doInBackground(ArrayList<_newsCh>... arrayLists) {
            for(ArrayList<_newsCh> item:arrayLists) {
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

    private void setNewAdapter(ArrayList<_newsCh> listnews)
    {
        listAdapter = new newsListAdapter(context,listnews);
        GL_arrayList_TO_NEWS=new ArrayList<>();
        GL_arrayList_TO_NEWS=listnews;
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                someEventListener.onClickNewsListItem(position);
            }
        });
    }
}
