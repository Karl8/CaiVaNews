package com.java.zu26.newsPage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.util.NewsShareActivity;
import com.java.zu26.util.SpeechUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lucheng on 2017/9/7.
 */

public class NewsPagePresenter implements NewsPageContract.Presenter {

    private NewsPageContract.View mView;

    private Toolbar mToolbar;

    private NewsRepository mRespository;

    private News mNews;

    private NewsPageActivity mActivity;

    private Context mContext;

    private SpeechUtils speech;

    public NewsPagePresenter(@NonNull NewsRepository respository, @NonNull NewsPageContract.View NewsView, Toolbar toolbar, NewsPageActivity activity) {
        mRespository = respository;
        mView = NewsView;
        mView.setPresenter(this);
        mToolbar = toolbar;
        mActivity = activity;
//        speech = SpeechUtils.getsSpeechUtils(mContext);
    }

    @Override
    public void start(String newsId) {
        mRespository.getNews(newsId, true, new NewsDataSource.GetNewsCallback() {
            @Override
            public void onNewsLoaded(News news) {
                Log.d("start", "onNewsLoaded: get news" + news.getTitle());
                mNews = news;
                mView.onGetNews();
                mRespository.saveKeywordCache(mContext);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("start", "onNewsLoaded: not found news");
            }
        });
    }

    @Override
    public News getNews() {
        if (mNews == null)
            Log.d("getnews", "getNews: NULL");
        Log.d("getnews", "getNews: GOOD");
        return mNews;
    }

    @Override
    public boolean getFavorite() {
        return false;
    }

    @Override
    public void showShareDialog() {
        NewsShareActivity a;

        String url=mNews.getUrl();
        String title=mNews.getTitle();
        String picture=mNews.getCoverPicture();
        String text=mNews.getContent();

        a = new NewsShareActivity(mContext);
        a.showShare(title,text,url,picture);
    }

    @Override
    public void addToFavorites() {
        mRespository.favoriteNews(mNews);
    }

    @Override
    public void removeFromFavorites() {
        mRespository.unfavoriteNews(mNews.getId());
    }

    @Override
    public void prepareToolbar(boolean isFavorite) {
        mActivity.prepareToolbar(isFavorite);
    }

    @Override
    public Context getCurrentContext() {
        return mContext;
    }

    @Override
    public void getContext(Context context) {
        mContext = context;
    }

    @Override
    public void readText() {
        String text=mNews.getTitle()+mNews.getContent();
        mActivity.readText(text);
    }
    @Override
    public void stopReadingText() {
        mActivity.stopReadingText();
    }

    @Override
    public String processContent(String text) {
        // add lines
        String result = "";
        String regex = "　";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        int start = 0;
        while(m.find()) {
            if(m.start() - start == 0) {
                result += regex;
                start = m.end();
                continue;
            }
            else if(m.start() - start <= 20) {
                result += "<b><tt>" + text.substring(start, m.start()) + "</tt></b>";
            }
            else {
                result += text.substring(start, m.start());
            }
            result += "<br /><br />" + regex ;
            start = m.end();
        }
        result += text.substring(start, text.length() - 1);
        return result;

    }
}
