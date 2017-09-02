package com.java.zu26.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by kaer on 2017/9/3.
 */

public interface NewsDataSource {

    interface LoadNewsListCallback {

        void onNewslistLoaded(List<News> newsList);

        void onDataNotAvailable();
    }

    interface GetNewsCallback {

        void onNewsLoaded(News news);

        void onDataNotAvailable();
    }

    void getLatestNewsList(@NonNull LoadNewsListCallback callback, int amount);

    void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback);


    void readNews(@NonNull News news);

    void favoriteNews(@NonNull News news);

    void unfavoriteNews(@NonNull News news);

    // share news callback ???
}
