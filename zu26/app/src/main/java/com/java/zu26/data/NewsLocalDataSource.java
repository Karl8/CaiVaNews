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

    private NewsFavoriteDbHelper mFavoriteDbHelper;

    // Prevent direct instantiation.

    private NewsLocalDataSource(Context context) {
        //checkNotNull(context);
        Log.d("local data source", "new dbhelper ");
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
        Log.d("LOCAL", "getNewsList: ");
        ArrayList<News> newsList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE "
                + NewsEntry.COLUMN_NAME_CATEGORY + " = '" + String.valueOf(category)
                + "' ORDER BY " + NewsEntry.COLUMN_NAME_INDEX + " LIMIT 10 OFFSET " + String.valueOf(page * 10 - 10), null);
        Log.d("LOCAL", "getNewsList: " + "SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE "
                + NewsEntry.COLUMN_NAME_CATEGORY + " = '" + String.valueOf(category)
                + "' ORDER BY " + NewsEntry.COLUMN_NAME_INDEX + " LIMIT 10 OFFSET " + String.valueOf(page * 10 - 10));

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_CATEGORY));
                String author = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_AUTHOR));
                String pictures = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_PICTURES));
                String source = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_URL));
                String intro = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_INTRO));
                boolean read = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_READ)) == 1;
                boolean favorite = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_FAVORITE)) == 1;
                String content = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_CONTENT));
                News news = new News(id, author, title, mcategory, pictures, source, time, url, intro, read, content, favorite);
                Log.d("LOCAL", "found: " + title);
                Log.d("LOCAL", "category: " + mcategory);
                newsList.add(news);
            }
        }
        else {
            Log.d("LOCAL", "getNewsList: not found");
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        if (newsList.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onNewsListLoaded(newsList);
        }
    }

    @Override
    public void getNews(@NonNull String newsId, @NonNull GetNewsCallback callback) {
        Log.d("LOCAL", "getNews: ");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE "
                + NewsEntry.COLUMN_NAME_ENTRY_ID + " = '" + newsId + "'", null);
        Log.d("LOCAL", "SELECT * FROM " + NewsEntry.TABLE_NAME + " WHERE "
                + NewsEntry.COLUMN_NAME_ENTRY_ID + " = '" + newsId + "'");
        News news = null;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                boolean read = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_READ)) == 1;
                if (read == false) {
                    Log.d("TAG", "getNews: not read");
                    callback.onDataNotAvailable();
                    return;
                }
                String id = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_ENTRY_ID));
                String mcategory = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_CATEGORY));
                String author = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_AUTHOR));
                String pictures = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_PICTURES));
                String source = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_SOURCE));
                String time = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_TIME));
                String title = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_TITLE));
                String url = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_URL));
                String intro = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_INTRO));
                boolean favorite = cursor.getInt(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_FAVORITE)) == 1;
                String content = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_CONTENT));
                news = new News(id, author, title, mcategory, pictures, source, time, url, intro, read, content, favorite);
                Log.d("LOCAL", "get one news found: " + title);
        }
        }
        else {
            Log.d("LOCAL", "getNews : not found");
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        if (news == null) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onNewsLoaded(news);
        }
    }

    @Override
    public void getFavoriteNewsList(int page, @NonNull GetNewsCallback callback) {

    }

    @Override
    public void readNews(@NonNull String newsId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_READ, true);

        String selection = NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { newsId };

        db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void favoriteNews(@NonNull News news) {
        Log.d("local", "favoriteNews: " + news.getTitle());
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
        values.put(NewsEntry.COLUMN_NAME_CATEGORY, news.getCategory());
        values.put(NewsEntry.COLUMN_NAME_PICTURES, news.getPictures());
        values.put(NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
        values.put(NewsEntry.COLUMN_NAME_TIME, news.getTime());
        values.put(NewsEntry.COLUMN_NAME_URL, news.getUrl());
        values.put(NewsEntry.COLUMN_NAME_INTRO, news.getIntro());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, news.isFavorite());
        values.put(NewsEntry.COLUMN_NAME_READ, news.isRead());

        db.insert(NewsEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void unfavoriteNews(@NonNull String newsId) {
        Log.d("local", "unfavoriteNews: " + newsId);
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, false);

        String selection = NewsEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { newsId };

        db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void saveNewsList(@NonNull ArrayList<News> newsList) {
        Log.d("LOCAL", "saveNewsList: ");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (int i = 0; i < newsList.size(); i++) {
            News news = newsList.get(i);

            ContentValues values = new ContentValues();
            values.put(NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
            values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
            values.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
            values.put(NewsEntry.COLUMN_NAME_CATEGORY, news.getCategory());
            values.put(NewsEntry.COLUMN_NAME_PICTURES, news.getPictures());
            values.put(NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
            values.put(NewsEntry.COLUMN_NAME_TIME, news.getTime());
            values.put(NewsEntry.COLUMN_NAME_URL, news.getUrl());
            values.put(NewsEntry.COLUMN_NAME_INTRO, news.getIntro());
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
        Log.d("local", "save one News: ");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_NAME_ENTRY_ID, news.getId());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
        values.put(NewsEntry.COLUMN_NAME_CATEGORY, news.getCategory());
        values.put(NewsEntry.COLUMN_NAME_PICTURES, news.getPictures());
        values.put(NewsEntry.COLUMN_NAME_SOURCE, news.getSource());
        values.put(NewsEntry.COLUMN_NAME_TIME, news.getTime());
        values.put(NewsEntry.COLUMN_NAME_URL, news.getUrl());
        values.put(NewsEntry.COLUMN_NAME_INTRO, news.getIntro());
        values.put(NewsEntry.COLUMN_NAME_AUTHOR, news.getAuthor());
        values.put(NewsEntry.COLUMN_NAME_FAVORITE, news.isFavorite());
        values.put(NewsEntry.COLUMN_NAME_READ, news.isRead());

        db.insert(NewsEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void searchNews(String keyWord, int page, @NonNull LoadNewsListCallback callback) {

    }


    public void updateNewsDetail(@NonNull News news) {

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
                Log.d("local", "updateNewsDetail: ");
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
