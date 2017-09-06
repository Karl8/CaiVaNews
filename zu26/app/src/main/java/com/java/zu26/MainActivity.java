package com.java.zu26;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    public void test(int page, int category) {
        NewsRemoteDataSource  rds = NewsRemoteDataSource.getInstance();
        NewsLocalDataSource lds = NewsLocalDataSource.getInstance(mContext);
        NewsRepository r = NewsRepository.getInstance(rds, lds);
        r.getNewsList(1, 1, new NewsDataSource.LoadNewsListCallback() {
            @Override
            public void onNewsListLoaded(ArrayList<News> newsList) {
                Log.d("TAG", "onNewsListLoaded: " + newsList.get(1).getPictures() + "\n" + newsList.get(1).getCoverPicture());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;
        Log.d("TAG", "onCreate:???");
        test(1, 1);
    }
}
