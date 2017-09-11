package com.java.zu26.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by kaer on 2017/9/3.
 */

public final class News implements Parcelable {

    private final String mId;

    private final String mAuthor;

    private final String mTitle;

    private final String mCategory;

    private final String mPictures;

    private final String mSource;

    private final String mTime;

    private final String mUrl;

    private final String mIntro;


    private final boolean mRead;

    private final String mContent;

    private final boolean mFavorite;

    private final String mJson;

    //public ()

    public News(News news, boolean read, boolean favorite) {
        mId = news.getId();
        mAuthor = news.getAuthor();
        mTitle = news.getTitle();
        mCategory = news.getCategory();
        mPictures = news.getPictures();
        mSource = news.getSource();
        mTime = news.getTime();
        mUrl = news.getUrl();
        mIntro = news.getIntro();
        mRead = read;
        mContent = news.getContent();
        mFavorite = favorite;
        mJson = news.getJson();
    }


    private News(Parcel in){
        mId = in.readString();
        mTitle = in.readString();
        mSource = "";
        mAuthor = in.readString();;
        mCategory = "";
        mPictures = "";
        mTime = in.readString();
        mUrl = "";
        mIntro = "";
        mRead = false;
        mContent = "";
        mFavorite = in.readInt() == 1;
        mJson = "";
    }


    public News(String id, String author, String title, String category, String pictures, String source, String time, String url, String intro) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mCategory = category;
        mPictures = pictures;
        mSource = source;
        mTime = time;
        mUrl = url;
        mIntro = intro;
        mRead = false;
        mContent = "";
        mFavorite = false;
        mJson = "";
    }

    public News(String id, String author, String title, String category, String pictures, String source,
                String time, String url, String intro, boolean read, String content, boolean favorite, String json) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mCategory = category;
        mPictures = pictures;
        mSource = source;
        mTime = time;
        mUrl = url;
        mIntro = intro;
        mRead = read;
        mContent = content;
        mFavorite = favorite;
        mJson = json;
    }
    // public News From json
    public String getCoverPicture() {
        int pos = mPictures.indexOf(';');
        if (pos != -1)
            return mPictures.substring(0, pos);
        return new String("");
    }

    public String getId() { return mId; }

    public String getTitle() { return mTitle; }

    public String getCategory() { return mCategory; }

    public String getPictures() { return mPictures; }

    public String getSource() { return mSource; }

    public String getTime() { return mTime; }

    public String getUrl() { return mUrl; }

    public String getIntro() { return mIntro; }

    public boolean isRead() { return mRead; }

    public String getAuthor() { return mAuthor; }

    public String getContent() { return mContent; }

    public boolean isFavorite() { return mFavorite; }

    public String getJson() { return mJson; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mTime);
        parcel.writeInt(mFavorite? 1 : 0);
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

/*
public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CLASS_TAG = "classTag";
        public static final String COLUMN_NAME_PICTURES = "pictures";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_INTRO = "intro";
        public static final String COLUMN_NAME_READ = "read";
        //--------------------------------------------------------------
        // detail:
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
 */