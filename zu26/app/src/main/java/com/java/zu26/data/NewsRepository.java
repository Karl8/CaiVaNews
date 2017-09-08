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

    ArrayList<String> mCachedFavoriteId;

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
    public void getNewsList(final int page, final int category, @NonNull final LoadNewsListCallback callback) {
        Log.d("TAG", "getNewsList: ");
        if (mCachedNewsDetail != null && mCachedNewsListId != null) {

            ArrayList<News> newsList = new ArrayList<>();
            for (int i = page * 10 - 10; i < page * 10 && i < mCachedNewsListId.get(category).size(); i++) {
                String id = mCachedNewsListId.get(category).get(i);
                newsList.add(mCachedNewsDetail.get(id));
                Log.d("TAG", "getNewsListfrom Cache: " + mCachedNewsDetail.get(id).getTitle());
            }
            callback.onNewsListLoaded(newsList);
            return;
        }
        mNewsLocalDataSource.getNewsList(page, category, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                refreshNewsListCache(category, newsList);
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                getNewsListFromRemoteDataSource(page, category, callback);
            }
        });
        //else
        //    getNewsListFromRemoteDataSource(page, category, callback);
    }
    //public void reefreshCache()
    public void getNewsListFromRemoteDataSource(final int page, final int category, @NonNull final LoadNewsListCallback callback) {
        Log.d("TAG", "getNewsListFromRemoteDataSource: ");
        mNewsRemoteDataSource.getNewsList(page, category, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                refreshNewsListCache(category, newsList);
                mNewsLocalDataSource.saveNewsList(newsList);
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

    public void refreshFavoriteNewsCache(ArrayList<News> newsList) {
        Log.d("TAG", "refreshNews from newsList: ");
        if (mCachedNewsDetail == null) {
            mCachedNewsDetail = new HashMap<>();
        }
        if (mCachedFavoriteId == null) {
            mCachedFavoriteId = new ArrayList<>();
        }
        for (int i = 0; i < newsList.size(); i++) {
            Log.d("TAG", "refreshNewsListCache: " + newsList.get(i).getId());
            mCachedFavoriteId.add(newsList.get(i).getId());
            mCachedNewsDetail.put(newsList.get(i).getId(), newsList.get(i));
        }
    }

    public void refreshNewsCache(News news) {
        Log.d("TAG", "refresh one News: ");
        if (mCachedNewsDetail == null) {
            mCachedNewsDetail = new HashMap<>();
        }
        mCachedNewsDetail.put(news.getId(), news);
    }

    @Override
    public void getNews(@NonNull final String newsId, @NonNull boolean isDetailed, @NonNull final GetNewsCallback callback) {
        Log.d("TAG", "get one News ");

        if (mCachedNewsDetail != null && mCachedNewsDetail.containsKey(newsId)) {
            News news = mCachedNewsDetail.get(newsId);
            if (news.isRead()) {
                Log.d("local", "get one News from cache ");
                callback.onNewsLoaded(news);
                return;
            }
        }

        mNewsLocalDataSource.getNews(newsId, isDetailed, new GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                Log.d("local", "get one news from local ");
                refreshNewsCache(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                getNewsFromRemoteDataSource(newsId, callback);
            }
        });
    }

    public void getNewsFromRemoteDataSource(@NonNull String newsId, @NonNull final GetNewsCallback callback) {
        mNewsRemoteDataSource.getNews(newsId, true, new GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                refreshNewsCache(news);
                mNewsLocalDataSource.updateNewsDetail(news);
                callback.onNewsLoaded(news);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
    @Override
    public void getFavoriteNewsList(int page, @NonNull final LoadNewsListCallback callback) {
        if (mCachedFavoriteId != null && mCachedNewsDetail != null) {
            ArrayList<News> newsList = new ArrayList<>();
            for (int i = page * 10 - 10; i < page * 10 && i < mCachedFavoriteId.size(); i++) {
                String id = mCachedFavoriteId.get(i);
                newsList.add(mCachedNewsDetail.get(id));
                Log.d("TAG", "getNewsListfrom Cache: " + mCachedNewsDetail.get(id).getTitle());
            }
            callback.onNewsListLoaded(newsList);
            return;
        }

        mNewsLocalDataSource.getFavoriteNewsList(page, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                refreshFavoriteNewsCache(newsList);
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void readNews(@NonNull String newsId) {

    }

    @Override
    public void favoriteNews(@NonNull News news) {
        News _news = new News(news, news.isRead(), true);
        if (mCachedNewsDetail == null) {
            mCachedNewsDetail = new HashMap<>();
        }
        if (mCachedFavoriteId == null)
            mCachedFavoriteId = new ArrayList<>();
        mCachedNewsDetail.put(_news.getId(), _news);
        mNewsLocalDataSource.favoriteNews(_news);
        if (!mCachedFavoriteId.contains(_news.getId()))
            mCachedFavoriteId.add(_news.getId());

    }


    @Override
    public void unfavoriteNews(@NonNull String newsId) {
        News _news = new News(mCachedNewsDetail.get(newsId), mCachedNewsDetail.get(newsId).isRead(), false);
        mCachedNewsDetail.put(_news.getId(), _news);
        mNewsLocalDataSource.unfavoriteNews(newsId);
        if (mCachedFavoriteId.contains(newsId))
            mCachedFavoriteId.remove(newsId);
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {

    }

    @Override
    public void saveNews(@NonNull News news) {

    }

    @Override
    public void updateNewsDetail(@NonNull News news) {

    }

    @Override
    public void searchNews(String keyWord, int page, @NonNull final LoadNewsListCallback callback) {
        mNewsRemoteDataSource.searchNews(keyWord, page, new LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                //不缓存，也不保存到本地，否则本地中新闻列表的顺序会乱
                //refreshNewsCache(newsList);
                //mNewsLocalDataSource.saveNewsList(newsList);
                callback.onNewsListLoaded(newsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

}
