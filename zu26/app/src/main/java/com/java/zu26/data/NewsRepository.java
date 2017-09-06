package com.java.zu26.data;

import android.support.annotation.NonNull;

import com.java.zu26.utils.News;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsRepository implements NewsDataSource {

    public static NewsRepository getInstance(){
        return new NewsRepository();
    }

    @Override
    public void getNewsList(int page, int category, @NonNull LoadNewsCallback callback) {
        ArrayList<News> newslist = new ArrayList<News>();
        for(int i=0;i<20;i++)newslist.add(new News());
        callback.onNewsLoaded(newslist);
    }

    @Override
    public void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback) {

    }

    @Override
    public void refreshNews() {

    }
}
