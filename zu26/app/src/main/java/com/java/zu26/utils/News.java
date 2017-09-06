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
    String pictures = "http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011621140.png";

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
    public String getCoverPicture(){return pictures;}

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
