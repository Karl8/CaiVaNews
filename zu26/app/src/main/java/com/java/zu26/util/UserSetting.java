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

    private Context mContext;

    public UserSetting() {
    }

    public UserSetting(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void saveCategorySetting(ArrayList<String> categoryList) {
        SharedPreferences sp = mContext.getSharedPreferences("categorySetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("size", categoryList.size());
        for (int i = 0 ; i < categoryList.size(); i++) {
            editor.putString(String.valueOf(i), categoryList.get(i));
        }
        editor.commit();
        Toast.makeText(mContext, "已保存设置", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public ArrayList<String> readCategorySetting() {
        ArrayList<String> categoryList = new ArrayList<>();
        SharedPreferences sp = mContext.getSharedPreferences("categorySetting", Context.MODE_PRIVATE);
        for (int i = 0 ; i < categoryList.size(); i++) {
            categoryList.add(sp.getString(String.valueOf(i), ""));
        }
        return categoryList;
    }
}