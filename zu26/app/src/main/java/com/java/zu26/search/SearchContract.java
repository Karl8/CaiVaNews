package com.java.zu26.search;

import com.java.zu26.data.News;
import com.java.zu26.newsList.NewsListPresenter;

import java.util.ArrayList;

/**
 * Created by kaer on 2017/9/7.
 */

public interface SearchContract {

    interface View {

        void setPresenter(SearchPresenter presenter);

        void showNews(String keyWord, int page, ArrayList<News> newsList);

        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showNoNews(String keyWord, int page, ArrayList<News> newsList);
    }

    interface Presenter {

        void loadNews(String keyWord, int page, boolean forceUpdate);

        void start();
    }

}
