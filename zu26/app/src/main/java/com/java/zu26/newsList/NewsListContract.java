package com.java.zu26.newsList;

import com.java.zu26.data.News;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public interface NewsListContract {

    interface View {

        void setPresenter(NewsListPresenter presenter);

        void showNews(int page, int category, ArrayList<News> newslist);

        void showNoNews(int page, int category, ArrayList<News> newslist);

        void setLoadingIndicator(boolean active);

        boolean isActive();
    }

    interface Presenter {

        void loadNews(int page, int category, boolean forceUpdate);

        void start();
    }
}
