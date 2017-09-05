package com.java.zu26.data;

import android.support.annotation.NonNull;

/**
 * Created by kaer on 2017/9/3.
 */

public final class News {

    private final String mId;

    private final String mAuthor;

    private final String mTitle;

    private final String mClassTag;

    private final String mPictures;

    private final String mSource;

    private final String mTime;

    private final String mUrl;

    private final String mIntro;

    private final boolean mRead;


    private final String mContent;

    private final boolean mFavorite;

    //public ()
    public News(String id, String author, String title, String classTag, String pictures, String source, String time, String url, String intro) {
        mId = id;
        mAuthor = author;
        mTitle = title;
        mClassTag = classTag;
        mPictures = pictures;
        mSource = source;
        mTime = time;
        mUrl = url;
        mIntro = intro;
        mRead = false;
        mContent = "";
        mFavorite = false;
    }

    // public News From json

    public String getId() { return mId; }

    public String getTitle() { return mTitle; }

    public String getmClassTag() { return mClassTag; }

    public String getPictures() { return mPictures; }

    public String getSource() { return mSource; }

    public String getTime() { return mTime; }

    public String getUrl() { return mUrl; }

    public String getIntro() { return mIntro; }

    public boolean isRead() { return mRead; }

    public String getAuthor() { return mAuthor; }

    public String getContent() { return mContent; }

    public boolean isFavorite() { return mFavorite; }





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