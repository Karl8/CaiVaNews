package com.java.zu26.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by kaer on 2017/9/3.
 */

public interface NewsDataSource {


    interface LoadNewsListCallback {

        void onNewsListLoaded(ArrayList<News> newsList);

        void onDataNotAvailable();


    }
    interface GetPictureCallback {

        void onPictureLoaded(String picture);

        void onPictureNotAvailable();
    }

    interface GetNewsCallback {

        void onNewsLoaded(News news);

        void onDataNotAvailable();
    }

    void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback);

    void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback, boolean reverse);

    void getNews(@NonNull String newsId,@NonNull boolean isDetailed, @NonNull GetNewsCallback callback);

    void getFavoriteNewsList(int page, @NonNull LoadNewsListCallback callback);

    void readNews(@NonNull String newsId);

    void favoriteNews(@NonNull News news);

    void unfavoriteNews(@NonNull String newsId);

    void saveNewsList(@NonNull ArrayList<News> newsList);

    void saveNewsList(@NonNull ArrayList<News> newsList, boolean recommend);

    void saveNews(@NonNull News news);

    void updateNewsDetail(@NonNull News news);

    void updateNewsPicture(@NonNull News news);

    void searchNews(String keyWord, int page, @NonNull LoadNewsListCallback callback);

    void searchKeywordNews(String keyWord,int count, @NonNull LoadNewsListCallback callback, HashSet<String> cache);

    void getCoverPicture(News news, @NonNull GetPictureCallback callback);

    // share news callback ???
}
