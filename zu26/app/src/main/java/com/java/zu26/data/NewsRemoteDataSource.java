package com.java.zu26.data;

import android.support.annotation.NonNull;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsRemoteDataSource implements NewsDataSource{
    private static NewsRemoteDataSource INSTANCE;
    //private Client client;
    private NewsRemoteDataSource() {

    }
    public static NewsRemoteDataSource getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new NewsRemoteDataSource();
        return INSTANCE;
    }

    public void getLatestNewsList() {


    }

    @Override
    public void getLatestNewsList(@NonNull LoadNewsListCallback callback, int amount) {

    }

    @Override
    public void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback) {

    }

    @Override
    public void readNews(@NonNull News news) {

    }

    @Override
    public void favoriteNews(@NonNull News news) {

    }

    @Override
    public void unfavoriteNews(@NonNull News news) {

    }
}
