package com.java.zu26.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
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

    void getLatestNewsList(int page, int category, @NonNull LoadNewsListCallback callback);

    void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback);


    void readNews(@NonNull String newsId);

    void favoriteNews(@NonNull String newsId);

    void unfavoriteNews(@NonNull String newsId);

    void saveNewsList(@NonNull ArrayList<News> newsList);

    void saveNews(@NonNull News news);

    // share news callback ???
}
