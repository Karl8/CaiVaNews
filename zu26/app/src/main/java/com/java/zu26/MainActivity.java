package com.java.zu26;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.java.zu26.data.News;
import com.java.zu26.data.NewsDbHelper;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsPersistenceContract;
import com.java.zu26.util.NewsDataUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    public void test(int page, int category) {
        String url = "http://166.111.68.66:2042/news/action/query/latest?pageNo=" + String.valueOf(page) + "&pageSize=10&category=" + String.valueOf(category);
        class URLThread extends Thread{
            private URL url;
            public URLThread(String path) throws MalformedURLException {
                url = new URL(path);
            }
            @Override
            public void run(){
                StringBuilder content = new StringBuilder();
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line + "\n");
                    }
                    bufferedReader.close();

                } catch (Exception e) {
                    Log.d("TAG", "getUrlContent failed");
                }
                ArrayList<News> newsList = NewsDataUtil.parseLastedNewsListJson(content.toString());
                NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
                //newsLocalDataSource.saveNewsList(newsList);
                newsLocalDataSource.find("20160913041301d5fc6a41214a149cd8a0581d3a014f");
            }
        }
        try {
            new URLThread(url).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;

        Log.d("TAG", "onCreate:");
        test(1, 1);
    }
}
