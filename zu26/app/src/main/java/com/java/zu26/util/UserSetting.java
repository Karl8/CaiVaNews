package com.java.zu26.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.java.zu26.data.News;

import java.util.ArrayList;

/**
 * Created by kaer on 2017/9/8.
 */

public class UserSetting {

    private static final String PREFERENCE_NAME_CATEGORY = "CategorySetting";

    private static final int MODE = Context.MODE_PRIVATE;

    private static final String PREFERENCE_PACKAGE = "com.java.zu26";

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
            for (int i = 1 ; i < 13; i++) {
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
            for (int i = 1 ; i < 13; i++) {
                categoryList.add(String.valueOf(i));
            }
            return categoryList;
        }
    }
}