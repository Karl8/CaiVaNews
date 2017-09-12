package com.java.zu26.newsPage;

import android.content.Context;

import com.java.zu26.data.News;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/7.
 */

public interface NewsPageContract {

    interface View {

        void setPresenter(NewsPagePresenter presenter);

        void showNews(News news);

        void onGetNews();

    }

    interface Presenter {

        void start(String newsId);

        News getNews();

        boolean getFavorite();

        void showShareDialog();

        void addToFavorites();

        void removeFromFavorites();

        void prepareToolbar(boolean isFavorite);

        void getContext(Context context);

        Context getCurrentContext();

        void readText();

        void stopReadingText();

        String processContent(String text);
    }
}
