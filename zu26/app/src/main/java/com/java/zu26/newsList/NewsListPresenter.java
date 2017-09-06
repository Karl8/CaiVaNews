package com.java.zu26.newsList;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;

import java.util.ArrayList;


import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by lucheng on 2017/9/3.
 *
 */

public class NewsListPresenter implements NewsListContract.Presenter{

    private NewsRepository mNewsRepository;

    private NewsListContract.View mNewsView;

    private boolean mFirstLoad;

    private int mPage;

    private int mCategory;

    final Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Log.d("TAG", "handleMessage: ");
            ArrayList<News> newsToShow = (ArrayList<News>)(message.obj);
            Log.d("TAG", "handleMessage: " + newsToShow.get(1).getTitle());
            processNews(mPage, mCategory, newsToShow);
        }
    };

    public NewsListPresenter(@NonNull NewsRepository newsRepository, @NonNull NewsListContract.View newsView) {
        mNewsRepository = checkNotNull(newsRepository, "newsRepository cannot be null");
        mNewsView = checkNotNull(newsView, "newsView cannot be null!");

        mNewsView.setPresenter(this);
    }



    @Override
    public void loadNews(int page, int category, boolean forceUpdate) {
        mPage = page;
        mCategory = category;
        loadNewsList(page, category, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link NewsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNewsList(final int page, final int category, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            mNewsView.setLoadingIndicator(true);
        }
        if(forceUpdate) {
            //mNewsRepository.refreshNews();
        }
        ArrayList<News> newsList = new ArrayList<>();
        newsList.add(new News("123456", "123456", "123456", "123456", "123456", "123456", "123456", "123456", "123456"));
        processNews(1, 1, newsList);
        mNewsRepository.getNewsList(page, category, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);

                if(!mNewsView.isActive()) {
                    return;
                }

                if(showLoadingUI) {
                    mNewsView.setLoadingIndicator(false);
                }
                Message message = new Message();
                message.obj = newsToShow;
                handler.sendMessage(message);
                Log.d("TAG", "send message!!! ");
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void processNews(int page, int category, ArrayList<News> newslist) {

        if(newslist.isEmpty()) {
            Log.d("TAG", "empty: ");
            mNewsView.showNoNews(page, category, newslist);
        }
        else {
            Log.d("TAG", "not empty: ");
            mNewsView.showNews(page, category, newslist);
        }
    }

    @Override
    public void start()
    {
        mPage = 1;
        mCategory = 1;
        loadNews(mPage, mCategory, false);
    }



}