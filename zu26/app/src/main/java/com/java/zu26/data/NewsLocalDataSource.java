package com.java.zu26.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.java.zu26.data.NewsPersistenceContract.NewsEntry;

/**
 * Created by kaer on 2017/9/5.
 */

public class NewsLocalDataSource implements NewsDataSource {
    private static NewsLocalDataSource INSTANCE;

    private NewsDbHelper mDbHelper;

    // Prevent direct instantiation.

    private NewsLocalDataSource(Context context) {
        //checkNotNull(context);
        mDbHelper = new NewsDbHelper(context);
    }
    public static NewsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsLocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public void getNewsList(int page, int category, @NonNull LoadNewsListCallback callback) {
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.COLUMN_NAME_CLASS_TAG
                        + " = ? ORDER BY " + NewsEntry.COLUMN_NAME_INDEX + " LIMIT 10 OFFSET " + String.valueOf(page * 10 - 10),
                new String[]{String.valueOf(category)});


        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String classTag = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String author = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String pictures = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String source = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String time = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String url = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String intro = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                boolean read = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_READ)) == 1;
                boolean favorite = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_FAVORITE)) == 1;
                String content = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_CONTENT));
                News news = new News(id, author, title, classTag, pictures, source, time, url, intro, read, content, favorite);
                newsList.add(news);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

    }

    @Override
    public void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback) {
        /*
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM" + NewsEntry.TABLE_NAME + "WHERE " + NewsEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{newsId});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String classTag = newsJsonObj.getString("newsClassTag");
                String author = newsJsonObj.getString("news_Author");
                String pictures = newsJsonObj.getString("news_Pictures");
                String source = newsJsonObj.getString("news_Source");
                String time = newsJsonObj.getString("news_Time");
                String title = newsJsonObj.getString("news_Title");
                String url = newsJsonObj.getString("news_URL");
                String intro = newsJsonObj.getString("news_Intro");
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();
        */
    }

    @Override
    public void readNews(@NonNull String newsId) {

    }

    @Override
    public void favoriteNews(@NonNull String newsId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, true);

        String selection = NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { newsId };

        db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void unfavoriteNews(@NonNull String newsId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, false);

        String selection = NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { newsId };

        db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (int i = 0; i < 1; i++) {
            News news = newsList.get(i);

            ContentValues values = new ContentValues();
            values.put(NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
            values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
            values.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
            values.put(NewsEntry.COLUMN_NAME_CLASS_TAG, news.getClassTag());
            values.put(NewsEntry.COLUMN_NAME_PICTURES, news.getPictures());
            values.put(NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
            values.put(NewsEntry.COLUMN_NAME_TIME, news.getTime());
            values.put(NewsEntry.COLUMN_NAME_URL, news.getUrl());
            values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
            values.put(NewsEntry.COLUMN_NAME_FAVORITE, 0);
            values.put(NewsEntry.COLUMN_NAME_READ, 0);
            Log.d("TAG", "saveNews: " + news.getTitle());
            db.insert(NewsEntry.TABLE_NAME, null, values);
        }
        db.close();
    }
    @Override
    public void saveNews(@NonNull News news) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
        values.put(NewsEntry.COLUMN_NAME_CLASS_TAG, news.getClassTag());
        values.put(NewsEntry.COLUMN_NAME_PICTURES, news.getPictures());
        values.put(NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
        values.put(NewsEntry.COLUMN_NAME_TIME, news.getTime());
        values.put(NewsEntry.COLUMN_NAME_URL, news.getUrl());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, news.isFavorite());
        values.put(NewsEntry.COLUMN_NAME_READ, news.isRead());

        db.insert(NewsEntry.TABLE_NAME, null, values);

        db.close();
    }

    public void saveNewsDetail(@NonNull News news) {
        // 是否需要先查询？？？？？？？？
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{news.getId()});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(NewsEntry.COLUMN_NAME_CONTENT, news.getContent());
                values.put(NewsEntry.COLUMN_NAME_READ, true);

                String selection = NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
                String[] selectionArgs = { news.getId() };

                db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

    }

    public void find(String newsId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{newsId});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                Log.d("TAG", "find: ");
            }
            else {
                Log.d("TAG", "not find");
            }
        }
        else {
            Log.d("TAG", "not find");
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();
    }
}
