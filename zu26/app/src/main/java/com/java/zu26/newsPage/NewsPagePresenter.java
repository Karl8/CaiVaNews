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

    private NewsPageActivity mActivity;

    public NewsPagePresenter(@NonNull NewsRepository respository, @NonNull NewsPageContract.View NewsView, Toolbar toolbar, NewsPageActivity activity) {
        mRespository = respository;
        mView = NewsView;
        mView.setPresenter(this);
        mToolbar = toolbar;
        mActivity = activity;
    }

    @Override
    public void start(String newsId) {
        mRespository.getNews(newsId, true, new NewsDataSource.GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                Log.d("start", "onNewsLoaded: get news" + news.getTitle());
                mNews = news;
                mView.onGetNews();
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("start", "onNewsLoaded: not found news");
            }
        });
    }

    @Override
    public News getNews() {
        if (mNews == null)
            Log.d("getnews", "getNews: NULL");
        Log.d("getnews", "getNews: GOOD");
        return mNews;
    }

    @Override
    public boolean getFavorite() {
        return false;
    }

    @Override
    public void showShareDialog() {

    }

    @Override
    public void addToFavorites() {
        mRespository.favoriteNews(mNews);
    }

    @Override
    public void removeFromFavorites() {
        mRespository.unfavoriteNews(mNews.getId());
    }

    @Override
    public void prepareToolbar(boolean isFavorite) {
        mActivity.prepareToolbar(isFavorite);
    }
}
