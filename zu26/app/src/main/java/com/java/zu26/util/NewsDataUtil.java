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
import static com.java.zu26.data.NewsPersistenceContract.NewsEntry.categoryMap;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsDataUtil {
    public static ArrayList<News> parseLastedNewsListJson(String json) {
        Log.d("TAG", "parseLastedNewsListJson: " + json);
        ArrayList<News> newsList = new ArrayList<News>();
        try{
            JSONObject listJsonObj = new JSONObject(json);
            JSONArray listJsonArray = listJsonObj.getJSONArray("list");
            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject newsJsonObj = (JSONObject) listJsonArray.get(i);

                //News news = new News();
                String id = newsJsonObj.getString("news_ID");
                String category = categoryMap.get(newsJsonObj.getString("newsClassTag"));
                String author = newsJsonObj.getString("news_Author");
                String pictures = newsJsonObj.getString("news_Pictures");
                String source = newsJsonObj.getString("news_Source");
                String time = newsJsonObj.getString("news_Time");
                String title = newsJsonObj.getString("news_Title");
                String url = newsJsonObj.getString("news_URL");
                String intro = newsJsonObj.getString("news_Intro");
                Log.d("TAG", "parse:" + category + " " + id + " " + author + " " + intro + " " + time);
                newsList.add(new News(id, author, title, category, pictures, source, time, url, intro));

            }
        }catch (Exception e){e.printStackTrace();}
        return newsList;
    }

    public static News parseNewsDetail(String json) {
        Log.d("TAG", "parse news detail from Json: " + json);
        News news = null;
        try{
            JSONObject newsJsonObj = new JSONObject(json);
            String id = newsJsonObj.getString("news_ID");
            String category = categoryMap.get(newsJsonObj.getString("newsClassTag"));
            String author = newsJsonObj.getString("news_Author");
            String pictures = newsJsonObj.getString("news_Pictures");
            String source = newsJsonObj.getString("news_Source");
            String time = newsJsonObj.getString("news_Time");
            String title = newsJsonObj.getString("news_Title");
            String url = newsJsonObj.getString("news_URL");
            String intro = "";
            String content = newsJsonObj.getString("news_Content");
            Log.d("TAG", "parse:" + category + " " + id + " " + author + " " + intro + " " + time);
            news = new News(id, author, title, category, pictures, source, time, url, intro, true, content, false, json);

        }catch (Exception e){e.printStackTrace();}
        return news;
    }

    public static ArrayList<String> parsePeopleLocation(String json) {
        Log.d("TAG", "parse people location from Json: " + json);
        ArrayList<String> result = new ArrayList<>();
        try{
            JSONObject newsJsonObj = new JSONObject(json);
            JSONArray peopleJsonArray = newsJsonObj.getJSONArray("persons");
            JSONArray locationsJsonArray = newsJsonObj.getJSONArray("locations");
            for (int i = 0 ; i< peopleJsonArray.length(); i++)
            {
                JSONObject peopleJsonObj = (JSONObject) peopleJsonArray.get(i);
                result.add(peopleJsonObj.getString("word"));
            }
            for (int i = 0 ; i< locationsJsonArray.length(); i++)
            {
                JSONObject locationsJsonObj = (JSONObject) locationsJsonArray.get(i);
                result.add(locationsJsonObj.getString("word"));
            }

        }catch (Exception e){e.printStackTrace();}
        return result;
    }

    public static HashMap<String, Double> parseKeyword(String json) {
        Log.d("TAG", "parse people location from Json: " + json);
        HashMap<String, Double> result = new HashMap<>();
        try{
            JSONObject newsJsonObj = new JSONObject(json);
            JSONArray keywordJsonArray = newsJsonObj.getJSONArray("Keywords");
            for (int i = 0 ; i< keywordJsonArray.length(); i++)
            {
                JSONObject keywordJsonObj = (JSONObject) keywordJsonArray.get(i);
                result.put(keywordJsonObj.getString("word"), keywordJsonObj.getDouble("score"));
            }
        }catch (Exception e){e.printStackTrace();}
        return result;
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
