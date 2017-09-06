package com.java.zu26.data;

import android.support.annotation.NonNull;

import com.java.zu26.utils.News;

import java.util.ArrayList;


/**
 * Created by lucheng on 2017/9/3.
 */


// News 类的import不正确
public interface NewsDataSource {

    interface LoadNewsCallback {

        void onNewsLoaded(ArrayList<News> news);

        void onDataNotAvailable();
    }

    interface GetNewsCallback {

        void onNewsLoaded(News news);

        void onDataNotAvailable();
    }

    void getNewsList(int page, int category, @NonNull LoadNewsCallback callback);

    void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback);

    void refreshNews();


}
