package com.java.zu26.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.java.zu26.data.News;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by kaer on 2017/9/8.
 */

public class UserSetting {

    private static final String PREFERENCE_NAME_CATEGORY = "CategorySetting";

    private static final String PREFERENCE_NAME_DAY_NIGHT_MODE = "day_night_mode";

    private static final int MODE = Context.MODE_PRIVATE;

    private static final String PREFERENCE_PACKAGE = "com.java.zu26";

    private static final String PREFERENCE_NAME_KEYWORD = "KeywordSetting";

    private static final String PREFERENCE_NAME_PICTURE = "PictureSetting";

    private static int category_size = 0;

    public UserSetting() {
    }

    //定义一个保存数据的方法
    public static void saveCategorySetting(Context context, ArrayList<String> categoryList) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME_CATEGORY, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("size", categoryList.size());
        for (int i = 0 ; i < categoryList.size(); i++) {
            editor.putString(String.valueOf(i), categoryList.get(i));
        }
        category_size = categoryList.size();
        editor.commit();
        Toast.makeText(context, "已保存设置", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public static ArrayList<String> loadCategorySetting(Context context) {
        ArrayList<String> categoryList = new ArrayList<>();
        Context otherContext;
        if(category_size == 0) {
            for (int i = 0 ; i < 13; i++) {
                categoryList.add(String.valueOf(i));
            }
            return categoryList;
        }
        try {
            otherContext = context.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = otherContext.getSharedPreferences(PREFERENCE_NAME_CATEGORY, MODE);
            for (int i = 0 ; i < category_size; i++) {
                categoryList.add(sp.getString(String.valueOf(i), ""));
            }
            return categoryList;
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0 ; i < 13; i++) {
                categoryList.add(String.valueOf(i));
            }
            return categoryList;
        }
    }


    public static void saveDayNightMode(Context context, DayNight mode) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME_DAY_NIGHT_MODE, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("day_night_mode", mode.getName());
        editor.commit();
    }

    public static boolean isDay(Context context) throws PackageManager.NameNotFoundException {
        Context otherContext = context.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
        SharedPreferences sp = otherContext.getSharedPreferences(PREFERENCE_NAME_DAY_NIGHT_MODE, MODE);
        String mode = sp.getString("day_night_mode", "");
        if (DayNight.DAY.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNight(Context context) throws PackageManager.NameNotFoundException {
        Context otherContext = context.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
        SharedPreferences sp = otherContext.getSharedPreferences(PREFERENCE_NAME_DAY_NIGHT_MODE, MODE);
        String mode = sp.getString("day_night_mode", "");
        if (DayNight.NIGHT.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }


    public static void saveKeyWord(Context context, HashMap<String, Double> keyword) {
        Gson g = new Gson();
        String json = g.toJson(keyword);

        Log.d("save", "saveKeyWord: " + json);
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME_KEYWORD, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("keyword", json);
        editor.commit();

    }

    public static HashMap<String, Double> loadKeyWord(Context context) {
        HashMap<String, Double> keyword = null;
        Context otherContext;
        try {
            otherContext = context.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = otherContext.getSharedPreferences(PREFERENCE_NAME_KEYWORD, MODE);
            Gson g = new Gson();
            Log.d("", "loadKeyWord: " + sp.getString("keyword", ""));
            keyword = g.fromJson(sp.getString("keyword", ""), HashMap.class);
        } catch (Exception e) {
            Log.d("", "loadKeyWord: " + e.toString());
        }
        return keyword;
    }

    public static void setPictureMode(Context context, int mode) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME_PICTURE, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("pictureMode", mode);
        editor.commit();
        Toast.makeText(context, "已保存设置", Toast.LENGTH_SHORT).show();
    }

    public static int getPictureMode(Context context) {
        Context otherContext;
        int mode = 0;
        try {
            otherContext = context.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = otherContext.getSharedPreferences(PREFERENCE_NAME_PICTURE, MODE);
            mode = sp.getInt("pictureMode", 0);

        } catch (Exception e) {
            Log.d("PICTURE_MODE", "exception: " + mode);
        }

        Log.d("PICTURE_MODE", "getPictureMode: " + mode);
        //return 0;

        return mode;
    }
}