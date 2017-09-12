package com.java.zu26.favorite;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsList.NewsListFragment;
import com.java.zu26.newsList.NewsListPresenter;
import com.java.zu26.util.ActivityUtils;
import com.java.zu26.util.DayNight;
import com.java.zu26.util.UserSetting;

/**
 * Created by kaer on 2017/9/8.
 */

public class FavoriteActivity extends AppCompatActivity {
    private Context mContext;

    private FavoriteFragment mFavoriteFragment;

    private FavoritePresenter mFavoritePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        mFavoriteFragment =
                (FavoriteFragment) getSupportFragmentManager().findFragmentById(R.id.favorite_frame);
        if (mFavoriteFragment == null) {
            // Create the fragment
            mFavoriteFragment = FavoriteFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFavoriteFragment, R.id.favorite_frame);
        }

        Log.d("favorite activity", "onCreate: ");
        // Create the presenter
        mContext = FavoriteActivity.this;
        //mContext.deleteDatabase("NewsFavorite.db");
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mFavoritePresenter = new FavoritePresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), mFavoriteFragment);

    }
}
