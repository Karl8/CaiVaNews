package com.java.zu26.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaer on 2017/9/3.
 */

public interface NewsDataSource {

    interface LoadNewsListCallback {

        void onNewsListLoaded(ArrayList<News> newsList);

        void onDataNotAvailable();


    }

    interface GetNewsCallback {

        void onNewsLoaded(News news);

        void onDataNotAvailable();
    }

    void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback);

    void getNews(@NonNull String newsId,@NonNull boolean isDetailed, @NonNull GetNewsCallback callback);

    void getFavoriteNewsList(int page, @NonNull GetNewsCallback callback);

    void readNews(@NonNull String newsId);

    void favoriteNews(@NonNull News news);

    void unfavoriteNews(@NonNull String newsId);

    void saveNewsList(@NonNull ArrayList<News> newsList);

    void saveNews(@NonNull News news);

    void updateNewsDetail(@NonNull News news);

    void searchNews(String keyWord, int page, @NonNull LoadNewsListCallback callback);

    // share news callback ???
}
