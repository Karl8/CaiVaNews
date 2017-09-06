package com.java.zu26.util;

import android.util.Log;

import com.java.zu26.data.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsDataUtil {
    public static ArrayList<News> parseLastedNewsListJson(String content) {
        ArrayList<News> newsList = new ArrayList<News>();
        try{
            JSONObject listJsonObj = new JSONObject(content);
            JSONArray listJsonArray = listJsonObj.getJSONArray("list");
            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject newsJsonObj = (JSONObject) listJsonArray.get(i);
                //News news = new News();
                String id = newsJsonObj.getString("news_ID");
                String classTag = newsJsonObj.getString("newsClassTag");
                String author = newsJsonObj.getString("news_Author");
                String pictures = newsJsonObj.getString("news_Pictures");
                String source = newsJsonObj.getString("news_Source");
                String time = newsJsonObj.getString("news_Time");
                String title = newsJsonObj.getString("news_Title");
                String url = newsJsonObj.getString("news_URL");
                String intro = newsJsonObj.getString("news_Intro");
                Log.d("TAG", "parse:" + id + " " + author + " " + intro + " " + time);
                newsList.add(new News(id, author, title, classTag, pictures, source, time, url, intro));

            }
        }catch (Exception e){e.printStackTrace();}
        return newsList;
    }
    public static void getUrlContent(String path) throws Exception{ // callback?
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
                    // content => callback
                } catch (Exception e) {
                    Log.d("TAG", "getUrlContent failed");
                }
            }
        }
        new URLThread(path).start();
    }
}
