package com.java.zu26.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsRepository implements NewsDataSource {
    private static NewsRepository INSTANCE = null;

    private final NewsDataSource mNewsRemoteDataSource;

    private final NewsDataSource mNewsLocalDataSource;

    Map<String, News> mCachedNewsDetail;

    ArrayList<ArrayList<String>> mCachedNewsListId;

    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                            @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
        // =>>> read cache file
        // else
        //mCachedNewsListId = new ArrayList[10];
        //mCachedNewsDetail = new Map<String, News>();


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
    public void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback) {
        Log.d("TAG", "getNewsList: ");
        if (mCachedNewsDetail != null && mCachedNewsListId != null && mCachedNewsListId.get(category).size() >= 10 * page) {
            ArrayList<News> newsList = new ArrayList<>();
            for (int i = page * 10 - 10; i < page * 10; i++) {
                String id = mCachedNewsListId.get(category).get(i);
                newsList.add(mCachedNewsDetail.get(id));
            }
            callback.onNewsListLoaded(newsList);
            return;
        }
        getNewsListFromRemoteDataSource(page, category, callback);
    }
    //public void reefreshCache()
    public void getNewsListFromRemoteDataSource(int page, final int category, @NonNull final LoadNewsListCallback callback) {
        Log.d("TAG", "getNewsListFromRemoteDataSource: ");
        mNewsRemoteDataSource.getNewsList(page, category, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                refreshNewsListCache(category, newsList);
                //mNewsLocalDataSource.saveNewsList(newsList);
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    public void refreshNewsListCache(int category, ArrayList<News> newsList) {
        Log.d("TAG", "refreshNewsListCache: ");
        if (mCachedNewsListId == null) {
            mCachedNewsListId = new ArrayList();
            for (int i = 0; i < 13; i++) {
                mCachedNewsListId.add(new ArrayList<String>());
            }
        }
        if (mCachedNewsDetail == null) {
            mCachedNewsDetail = new HashMap<>();
        }
        for (int i = 0; i < newsList.size(); i++) {
            Log.d("TAG", "refreshNewsListCache: " + newsList.get(i).getId());
            mCachedNewsListId.get(category).add(newsList.get(i).getId());
            mCachedNewsDetail.put(newsList.get(i).getId(), newsList.get(i));
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
