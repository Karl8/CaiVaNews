package com.java.zu26.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.zu26.util.NewsDataUtil;
import com.java.zu26.util.UserSetting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsRepository implements NewsDataSource {
    private static NewsRepository INSTANCE = null;

    private final NewsDataSource mNewsRemoteDataSource;

    private final NewsDataSource mNewsLocalDataSource;

    private Map<String, News> mCachedNewsDetail;

    private ArrayList<String> mCachedFavoriteId;

    private ArrayList<ArrayList<String>> mCachedNewsListId;

    private HashMap<String, Double> mCachedRecommendationTree;

    private HashSet<String> mCachedRecommendationId;

    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                           @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;

        // =>>> read cache file
        // else
        //mCachedNewsListId = new ArrayList[10];
        //mCachedNewsDetail = new Map<String, News>();


    }

    public void setCachedRecommend(Context context) {
        mCachedRecommendationTree = UserSetting.loadKeyWord(context);
        if (mCachedRecommendationTree == null)
            mCachedRecommendationTree = new HashMap<>();
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
        if (mCachedNewsDetail != null && mCachedNewsListId != null && mCachedNewsListId.get(category).size() >= page * 10) {

            ArrayList<News> newsList = new ArrayList<>();
            for (int i = page * 10 - 10; i < page * 10 && i < mCachedNewsListId.get(category).size(); i++) {
                String id = mCachedNewsListId.get(category).get(i);
                newsList.add(mCachedNewsDetail.get(id));
                Log.d("TAG", "getNewsListfrom Cache: " + mCachedNewsDetail.get(id).getTitle());
            }
            callback.onNewsListLoaded(newsList);
            return;
        }
        Log.d("Recommend", "cache size " + mCachedRecommendationTree.size());

        if (category == 0) {
            mNewsLocalDataSource.getNewsList(page, category, new LoadNewsListCallback() {
                @Override
                public void onNewsListLoaded(ArrayList<News> newsList) {
                    //refreshNewsListCache(category, newsList);
                    callback.onNewsListLoaded(newsList);
                }

                @Override
                public void onDataNotAvailable() {
                    getRecommendaton(new LoadNewsListCallback() {
                        @Override
                        public void onNewsListLoaded(ArrayList<News> newsList) {
                            //不缓存，因为很可能每次都刷新
                            //refreshNewsListCache(category, newsList);
                            refreshRecommendId(newsList);

                            for (News news:newsList) {
                                Log.d("recommend", news.getTitle() +" " + news.getId());
                            }
                            callback.onNewsListLoaded(newsList);
                        }

                        @Override
                        public void onDataNotAvailable() {

                        }
                    });
                }
            }, true);

        }
        else {
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
        }

    }

    @Override
    public void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback, boolean reverse) {

    }

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

    public void refredshRecommendation(News news) {
        Log.d("Recommend", "refredshRecommendation: ");

        HashMap<String, Double> newKeyword = NewsDataUtil.parseKeyword(news.getJson());
        int cnt = 0;
        Iterator i = mCachedRecommendationTree.entrySet().iterator();
        for (HashMap.Entry entry:mCachedRecommendationTree.entrySet()) {
            String key = (String)entry.getKey();
            Double value = (Double)entry.getValue();
            mCachedRecommendationTree.put(key, value * 0.9);
        }

        List<HashMap.Entry<String, Double>> list = new ArrayList<>(newKeyword.entrySet());

        Collections.sort(list,new Comparator<HashMap.Entry<String, Double>>() {
            //降序排序
            public int compare(HashMap.Entry<String, Double> o1, HashMap.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        i = list.iterator();
        while (i.hasNext()) {
            HashMap.Entry<String, Double> entry = (HashMap.Entry) i.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            if (mCachedRecommendationTree.containsKey(key)) {
                Double oldValue = mCachedRecommendationTree.get(key);
                mCachedRecommendationTree.put(key, value + oldValue);
            }
            else
                mCachedRecommendationTree.put(key, value);
            cnt++;
            if (cnt > 3) break;
        }
        Log.d("refresh", "getRecommendaton: " + mCachedRecommendationTree.entrySet().size());
        list = new ArrayList<>(mCachedRecommendationTree.entrySet());

        Collections.sort(list,new Comparator<HashMap.Entry<String, Double>>() {
            //降序排序
            public int compare(HashMap.Entry<String, Double> o1, HashMap.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        cnt = 0;
        i = list.iterator();
        while (i.hasNext()) {
            HashMap.Entry<String, Double> entry = (HashMap.Entry)i.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            Log.d("Recommend", "Cache Key: " + key + " " + value);
            cnt++;
            //if (cnt > 5) break;
        }
        Log.d("refresh", "getRecommendaton: " + mCachedRecommendationTree.entrySet().size());

    }

    public void saveKeywordCache(Context context) {
        UserSetting.saveKeyWord(context, mCachedRecommendationTree);
    }
    public void refreshRecommendId(ArrayList<News> newsList) {
        if (mCachedRecommendationId == null)
            mCachedRecommendationId = new HashSet<>();
        for (News news: newsList) {
            mCachedRecommendationId.add(news.getTitle());
        }
    }

    public void getRecommendaton(@NonNull final LoadNewsListCallback callback) {
        if (mCachedRecommendationId == null)
            mCachedRecommendationId = new HashSet<>();
        ///--测试用----
        //mCachedRecommendationTree.put("运动员", 100.0);
       // mCachedRecommendationTree.put("iphone", 200.0);
        //mCachedRecommendationTree.put("app", 100.0);
       // mCachedRecommendationTree.put("柔道", 100.0);
       // mCachedRecommendationTree.put("世界", 100.0);
        ///-----------

        Double []weight = new Double[6];
        Double accu = 0.0;
        int cnt = 0;
        final int[] num = new int[6];
        final String[] keys = new String[6];
        List<HashMap.Entry<String, Double>> list = new ArrayList<>(mCachedRecommendationTree.entrySet());

        Collections.sort(list,new Comparator<HashMap.Entry<String, Double>>() {
            //降序排序
            public int compare(HashMap.Entry<String, Double> o1, HashMap.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Log.d("Recommend", "getRecommendaton: " + mCachedRecommendationTree.size());
        Iterator i = list.iterator();
        while (i.hasNext()) {
            HashMap.Entry<String, Double> entry = (HashMap.Entry)i.next();
            String key = entry.getKey();
            Double value = entry.getValue();
            Log.d("!!!!Recommend", "!!!!!!getRecommendaton: value " + String.valueOf(value) + ' ' + key);
            cnt++;
            keys[cnt] = key;
            accu += value;
            weight[cnt] = accu;
            num[cnt] = 0;
            if (cnt >= 5) break;
        }
        Double d;
        Random random = new Random();
        for (int j = 0; j < 10; j++) {
            d = random.nextDouble() * accu;
            if (d < weight[1]) {
                num[1]++;
            }
            else if (d < weight[2]) {
                num[2]++;
            }
            else if (d < weight[3]) {
                num[3]++;
            }
            else if (d < weight[4]) {
                num[4]++;
            }
            else
                num[5]++;
            } Log.d("", "getRecommendaton: num " + String.valueOf(num[1]));
        Log.d("", "getRecommendaton: num " + String.valueOf(num[1]));
        Log.d("", "getRecommendaton: num " + String.valueOf(num[2]));
        Log.d("", "getRecommendaton: num " + String.valueOf(num[3]));
        Log.d("", "getRecommendaton: num " + String.valueOf(num[4]));
        Log.d("", "getRecommendaton: num " + String.valueOf(num[5]));
        new Thread() {
            @Override
            public void run(){
                final ArrayList<News> retList = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    if (num[j] != 0) {
                        mNewsRemoteDataSource.searchKeywordNews(keys[j], num[j], new LoadNewsListCallback() {
                            @Override
                            public void onNewsListLoaded(ArrayList<News> newsList) {
                                retList.addAll(newsList);
                            }

                            @Override
                            public void onDataNotAvailable() {

                            }
                        }, mCachedRecommendationId);
                    }
                }
                while (retList.size() < 10) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                callback.onNewsListLoaded(retList);
            }
        }.start();

    }

    @Override
    public void getNews(@NonNull final String newsId, @NonNull boolean isDetailed, @NonNull final GetNewsCallback callback) {
        Log.d("TAG", "get one News ");
        Log.d("get news", "getRecommendaton: " + mCachedRecommendationTree.size());
        if (mCachedNewsDetail != null && mCachedNewsDetail.containsKey(newsId)) {
            News news = mCachedNewsDetail.get(newsId);
            if (news.isRead() && news.getContent() != null) {
                Log.d("local", "get one News from cache ");
                refredshRecommendation(news);
                callback.onNewsLoaded(news);
                return;
            }
        }

        mNewsLocalDataSource.getNews(newsId, isDetailed, new GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                Log.d("local", "get one news from local ");
                refreshNewsCache(news);
                refredshRecommendation(news);
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
                refredshRecommendation(news);
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
        if (mCachedFavoriteId == null)
            mCachedFavoriteId = new ArrayList<>();
        if (mCachedFavoriteId.contains(newsId))
            mCachedFavoriteId.remove(newsId);
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {

    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList, boolean recommend) {

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

    @Override
    public void searchKeywordNews(String keyWord, int count, @NonNull LoadNewsListCallback callback, HashSet<String> cache) {

    }

}
