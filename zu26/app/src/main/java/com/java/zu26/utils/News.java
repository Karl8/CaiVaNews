package com.java.zu26.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lucheng on 2017/9/3.
 */

/*
单条新闻的全部信息.
实现Parcelable接口，可放入Bundle中，在Activity之间传递。
 */

public class News implements Parcelable {


    /*
    Just for demo.
     */
    String sourceUrl = null;
    String source = "新华网";
    String title = "TEST";
    String updateTime = "1h";
    String pictures = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504700572201&di=e8e7d47eda3dfb47b9b6e0db53912402&imgtype=0&src=http%3A%2F%2Ff2.ldjty.com%2Fuploads%2Fallimg%2F150719%2F021933J51-4.jpg";

    public News() {}


    /*
    The order is the same as writeToParcel.
     */
    private News(Parcel in){
        this.title = in.readString();
        this.sourceUrl = in.readString();
        this.updateTime = in.readString();
    }

    public String getTime(){return updateTime;}
    public String getTitle(){return title;}
    public String getSource(){return source;}
    public String getPictures(){return pictures;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(sourceUrl);
        parcel.writeString(updateTime);
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {

        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

}
