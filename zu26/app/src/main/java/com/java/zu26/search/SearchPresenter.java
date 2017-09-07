package com.java.zu26.search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;

import java.util.ArrayList;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by kaer on 2017/9/7.
 */

public class SearchPresenter implements SearchContract.Presenter{
    private NewsRepository mNewsRepository;

    private SearchContract.View mSearchView;

    private boolean mFirstLoad = true;

    public SearchPresenter(@NonNull NewsRepository newsRepository, @NonNull SearchContract.View newsView) {
        mNewsRepository = checkNotNull(newsRepository, "newsRepository cannot be null");
        mSearchView = checkNotNull(newsView, "newsView cannot be null!");
        mSearchView.setPresenter(this);
    }



    @Override
    public void loadNews(String keyWord, int page, boolean forceUpdate) {
        loadNewsList(keyWord, page, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the  data in the {@link NewsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNewsList(final String keyWord, final int page, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            mSearchView.setLoadingIndicator(true);
        }
        if(!forceUpdate) {
            if(showLoadingUI) {
                mSearchView.setLoadingIndicator(false);
            }
            return;
        }
        Log.d("TAG", "loadNewsList: ");
        mNewsRepository.searchNews(keyWord, page, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);
                Log.d("TAG", "onNewsListLoaded: process news");
                if(!mSearchView.isActive()) {
                    Log.d("TAG", "onNewsListLoaded: not active");
                    return;
                }

                if(showLoadingUI) {
                    mSearchView.setLoadingIndicator(false);
                }

                processNews(keyWord, page, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void processNews(String keyWord, int page, ArrayList<News> newsList) {
        Log.d("TAG", "processNews: ");
        if(newsList.isEmpty()) {
            Log.d("TAG", "empty: ");
            mSearchView.showNoNews(keyWord, page, newsList);
        }
        else {
            Log.d("TAG", "not empty: ");
            mSearchView.showNews(keyWord, page, newsList);
        }
    }

    public void searchNews(final String keyWord, final int page, final boolean showLoadingUI) {
        Log.d("search", "searchNews: " + keyWord);
        if(showLoadingUI) {
            mSearchView.setLoadingIndicator(true);
        }

        mNewsRepository.searchNews(keyWord, page, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);

                if(!mSearchView.isActive()) {
                    return;
                }

                if(showLoadingUI) {
                    mSearchView.setLoadingIndicator(false);
                }

                processNews(keyWord, page, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void start()
    {
        searchNews("北京", 1, true);
    }
}
