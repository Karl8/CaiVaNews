package com.java.zu26.data;

import android.support.annotation.NonNull;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsRepository implements NewsDataSource {
    @Override
    public void getLatestNewsList(int page, @NonNull LoadNewsListCallback callback) {

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
