package com.java.zu26.favorite;

import com.java.zu26.data.News;
import com.java.zu26.newsList.NewsListPresenter;

import java.util.ArrayList;

/**
 * Created by kaer on 2017/9/8.
 */

public interface FavoriteContract {

    interface View {

        void setPresenter(FavoritePresenter presenter);

        void showNews(int page, ArrayList<News> newslist);

        void showNoNews(int page, ArrayList<News> newslist);

        void setLoadingIndicator(boolean active);

        boolean isActive();

    }

    interface Presenter {

        void loadNews(int page, boolean forceUpdate);

        void start();

        void removeFromFavorites(String newsId);
    }
}