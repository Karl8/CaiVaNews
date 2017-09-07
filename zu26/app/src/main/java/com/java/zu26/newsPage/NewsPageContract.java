package com.java.zu26.newsPage;

import com.java.zu26.data.News;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/7.
 */

public interface NewsPageContract {

    interface View {

        void setPresenter(NewsPagePresenter presenter);

        void showNews(News news);

    }

    interface Presenter {

        void start(String newsId);

        News getNews();

        void showShareDialog();

        void addToFavorites();

        void removeFromFavorites();
    }
}
