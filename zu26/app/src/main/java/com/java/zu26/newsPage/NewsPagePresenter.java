package com.java.zu26.newsPage;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;

/**
 * Created by lucheng on 2017/9/7.
 */

public class NewsPagePresenter implements NewsPageContract.Presenter {

    private NewsPageContract.View mView;

    private Toolbar mToolbar;

    private NewsRepository mRespository;

    private News mNews;

    public NewsPagePresenter(@NonNull NewsRepository respository, @NonNull NewsPageContract.View NewsView, Toolbar toolbar) {
        mRespository = respository;
        mView = NewsView;
        mView.setPresenter(this);
        mToolbar = toolbar;
    }

    @Override
    public void start(String newsId) {
        mRespository.getNews(newsId, new NewsDataSource.GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                mNews = news;
                mView.showNews(news);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public News getNews() {
        return mNews;
    }

    @Override
    public void showShareDialog() {

    }

    @Override
    public void addToFavorites() {

    }

    @Override
    public void removeFromFavorites() {

    }
}
