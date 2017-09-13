package com.java.zu26.newsList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;

import java.util.ArrayList;


import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by lucheng on 2017/9/3.
 *
 */

public class NewsListPresenter implements NewsListContract.Presenter{

    private NewsRepository mNewsRepository;

    private NewsListContract.View mNewsView;

    private boolean mFirstLoad = true;

    public NewsListPresenter(@NonNull NewsRepository newsRepository, @NonNull NewsListContract.View newsView) {
        mNewsRepository = checkNotNull(newsRepository, "newsRepository cannot be null");
        mNewsView = checkNotNull(newsView, "newsView cannot be null!");
        mNewsView.setPresenter(this);
    }



    @Override
    public void loadNews(int page, int category, boolean forceUpdate) {
        loadNewsList(page, category, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link NewsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNewsList(final int page, final int category, boolean forceUpdate, final boolean showLoadingUI) {
        if(showLoadingUI) {
            mNewsView.setLoadingIndicator(true);
        }
        if(!forceUpdate) {
            if(showLoadingUI) {
                mNewsView.setLoadingIndicator(false);
            }
            return;
        }

        mNewsRepository.getNewsList(page, category, new NewsDataSource.LoadNewsListCallback() {

            @Override
            public void onNewsListLoaded(ArrayList<News> news) {
                ArrayList<News> newsToShow = new ArrayList<News>(news);

                if(!mNewsView.isActive()) {
                    return;
                }

                if(showLoadingUI) {
                    mNewsView.setLoadingIndicator(false);
                }

                processNews(page, category, newsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    public void processNews(int page, int category, ArrayList<News> newslist) {

        if(newslist.isEmpty()) {
            Log.d("TAG", "empty: ");
            mNewsView.showNoNews(page, category, newslist);
        }
        else {
            Log.d("TAG", "not empty: ");
            mNewsView.showNews(page, category, newslist);
        }
    }

    @Override
    public void start() {
        if(mNewsView.getPage() > 0) return;
        else loadNews(1, mNewsView.getCategory(), true);
    }

    @Override
    public void refreshUI(TypedValue background, TypedValue textColor) {
        if(mFirstLoad)return;
        mNewsView.refreshUI(background,textColor);
    }

    @Override
    public void getCoverPicture(final Context context, News news, final ImageView imageview) {
        mNewsRepository.getCoverPicture(news, new NewsDataSource.GetPictureCallback() {
            @Override
            public void onPictureLoaded(String picture) {
                Glide.with(context).load(picture).placeholder(R.drawable.downloading).into(imageview);
            }

            @Override
            public void onPictureNotAvailable() {
                Glide.with(context).load(R.drawable.downloading).placeholder(R.drawable.downloading).into(imageview);
            }
        });

    }



}