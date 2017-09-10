package com.java.zu26.favorite;

import android.support.annotation.NonNull;
import android.util.Log;

import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.search.SearchContract;

import java.util.ArrayList;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by kaer on 2017/9/8.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {
    private NewsRepository mNewsRepository;

    private FavoriteContract.View mFavoriteView;

    private boolean mFirstLoad = true;

    public FavoritePresenter(@NonNull NewsRepository newsRepository, @NonNull FavoriteContract.View newsView) {
        mNewsRepository = checkNotNull(newsRepository, "newsRepository cannot be null");
        mFavoriteView = checkNotNull(newsView, "newsView cannot be null!");
        mFavoriteView.setPresenter(this);
    }



    @Override
    public void loadNews(int page, boolean forceUpdate) {
        loadNewsList(page, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the  data in the {@link NewsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNewsList(final int page, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            mFavoriteView.setLoadingIndicator(true);
        }
        if(!forceUpdate) {
            if(showLoadingUI) {
                mFavoriteView.setLoadingIndicator(false);
            }
            return;
        }
        Log.d("TAG", "loadNewsList: ");
        mNewsRepository.getFavoriteNewsList(page, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);
                Log.d("TAG", "onNewsListLoaded: process news");
                if(!mFavoriteView.isActive()) {
                    Log.d("TAG", "onNewsListLoaded: not active");
                    return;
                }

                if(showLoadingUI) {
                    mFavoriteView.setLoadingIndicator(false);
                }

                processNews(page, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void processNews(int page, ArrayList<News> newsList) {
        Log.d("TAG", "processNews: ");
        if(newsList.isEmpty()) {
            Log.d("TAG", "empty: ");
            mFavoriteView.showNoNews(page, newsList);
        }
        else {
            Log.d("TAG", "not empty: ");
            mFavoriteView.showNews(page, newsList);
        }
    }

    @Override
    public void start()
    {
        Log.d("favorite presenter", "start: ");
        loadNews(1, true);
    }

    @Override
    public void removeFromFavorites(String newsId) {
        mNewsRepository.unfavoriteNews(newsId);
    }

}
