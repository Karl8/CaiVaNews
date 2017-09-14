package com.java.zu26.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.favorite.FavoriteActivity;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsPage.NewsPageActivity;
import com.java.zu26.search.SearchActivity;
import com.java.zu26.util.SpeechUtils;
import com.java.zu26.util.UserSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

//implements View.OnClickListener
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        UserSetting.saveKeyWord(this, new HashMap<String, Double>());
//        SharedPreferences sp = MainActivity.this.getSharedPreferences("CategorySetting", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        editor.commit();
//        deleteDatabase("News.db");
//        deleteDatabase("NewsFavorite.db");
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(this);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        NewsRepository newsRepository = NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource);
        newsRepository.setCachedRecommend(this);
        /*
        newsRepository.getRecommendaton(new NewsDataSource.LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                for (News news: newsList)
                    Log.d("recommend", news.getTitle());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        */
        //startActivity(new Intent(this, SearchActivity.class));
        //startActivity(new Intent(this, CategoryActivity.class));
        //startActivity(new Intent(this, FavoriteActivity.class));
        startActivity(new Intent(this, NewsListActivity.class));
        finish();
    }
}