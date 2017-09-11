package com.java.zu26.newsPage;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.util.NewsDataUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lucheng on 2017/9/7.
 */

public class NewsPageFragment extends Fragment implements NewsPageContract.View {

    private NewsPagePresenter mPresenter;

    private TextView mTitleView;

    private TextView mTimeView;

    private TextView mSourceView;

    private TextView mAuthorView;

    private ImageView mCoverImageView;

    private TextView mIntroductionView;

    private TextView mContentView;

    private boolean rawFavorite;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    showNews(mPresenter.getNews());
                    Log.d("handler", "handleMessage: " + mPresenter.getNews().isFavorite());
                    //mPresenter.prepareToolbar(mPresenter.getNews().isFavorite());
            }
        }
    };

    public static NewsPageFragment newInstance() {
        return new NewsPageFragment();
    }

    public NewsPageFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newspage, container, false);
        mTitleView = root.findViewById(R.id.news_page_title);
        mTimeView = root.findViewById(R.id.news_page_time);
        mSourceView = root.findViewById(R.id.news_page_source);
        mAuthorView = root.findViewById(R.id.news_page_author);
        mCoverImageView = root.findViewById(R.id.news_page_cover_image);
        mIntroductionView = root.findViewById(R.id.news_page_introduction);
        mContentView = root.findViewById(R.id.news_page_content);



        return root;
    }

    @Override
    public void setPresenter(NewsPagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNews(News news) {
        mTitleView.setText(news.getTitle());
        String time = news.getTime() + "  |  ";
        mTimeView.setText(time);
        String source = news.getSource() + "  ";
        mSourceView.setText(source);
        mAuthorView.setText(news.getAuthor());
        if(news.getCoverPicture() != null) {
            Glide.with(getContext()).load(news.getCoverPicture()).into(mCoverImageView);
        }
        else {
            mCoverImageView.setVisibility(View.GONE);
        }
        //mIntroductionView.setText(news.getIntro());


        Spanned htmlContent = Html.fromHtml(mPresenter.processContent(news.getContent()));

        SpannableString ss2 = new SpannableString(htmlContent);

        ArrayList<String> li= NewsDataUtil.parsePeopleLocation(news.getJson());

        for(String x: li) {
            findlink(ss2,x);
        }
        mContentView.setText(ss2);
        mContentView.setMovementMethod(LinkMovementMethod.getInstance());









        Log.d("LC::", "showNews: " + news.getContent());
    }

    public void findlink(SpannableString s, String a ){
        Pattern p = Pattern.compile(a);
        String text=s.toString();
        Matcher m = p.matcher(text);
        while(m.find()){
            setlink(m.start(),m.end(),s);
        }
    }
    public void setlink(int start ,int end, SpannableString s){
        final String ss=s.toString().substring(start,end);
        s.setSpan(new ClickableSpan() {
            @Override

            public void onClick(View widget) {
                Uri uri =  Uri.parse("http://baike.baidu.com/item/"+ ss);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public void onGetNews () {
        handler.sendEmptyMessage(0);
    }



}
