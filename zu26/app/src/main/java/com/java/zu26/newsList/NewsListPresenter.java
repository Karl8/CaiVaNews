package com.java.zu26.newsList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.zu26.R;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.utils.News;

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

    public NewsListPresenter(@NonNull NewsRepository newsRepository, @NonNull NewsListContract.View newsView) {
        mNewsRepository = checkNotNull(newsRepository, "newsRepository cannot be null");
        mNewsView = checkNotNull(newsView, "newsView cannot be null!");

        mNewsView.setPresenter(this);
    }



    @Override
    public void loadNews(int page, int category, boolean forceUpdate) {
        loadTasks(page, category, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link NewsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(final int page, final int category, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            mNewsView.setLoadingIndicator(true);
        }
        if(forceUpdate) {
            mNewsRepository.refreshNews();
        }

        mNewsRepository.getNewsList(page, category, new NewsDataSource.LoadNewsCallback() {

            @Override
            public void onNewsLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);

                if(!mNewsView.isActive()) {
                    return;
                }

                if(showLoadingUI) {
                    mNewsView.setLoadingIndicator(false);
                }

                processNews(page, category, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void processNews(int page, int category, ArrayList<News> newslist) {
        if(newslist.isEmpty()) {
            mNewsView.showNoNews(page, category, newslist);
        }
        else {
            mNewsView.showNews(page, category, newslist);
        }
    }

    @Override
    public void start() {
        loadNews(0, 0, false);
    }



}