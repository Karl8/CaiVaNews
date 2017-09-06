package com.java.zu26.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsRepository implements NewsDataSource {
    private static NewsRepository INSTANCE = null;

    private final NewsDataSource mNewsRemoteDataSource;

    private final NewsDataSource mNewsLocalDataSource;

    Map<String, News> mCachedNewsDetail;

    ArrayList<String>[] mCachedNewsListId;

    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                            @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
    }

    public static NewsRepository getInstance(NewsDataSource newsRemoteDataSource,
                                              NewsDataSource newsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsRemoteDataSource, newsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getLatestNewsList(int page, int category, @NonNull LoadNewsListCallback callback) {
        if (mCachedNewsDetail != null && mCachedNewsListId[category] != null && mCachedNewsListId[category].size() >= 10 * page) {

        }
    }

    @Override
    public void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback) {

    }

    @Override
    public void readNews(@NonNull String newsId) {

    }

    @Override
    public void favoriteNews(@NonNull String newsId) {

    }

    @Override
    public void unfavoriteNews(@NonNull String newsId) {

    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {

    }

    @Override
    public void saveNews(@NonNull News news) {

    }

}
