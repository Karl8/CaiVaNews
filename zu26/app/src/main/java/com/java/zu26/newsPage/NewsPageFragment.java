package com.java.zu26.newsPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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



    public static NewsPageFragment newInstance() {
        return new NewsPageFragment();
    }

    public NewsPageFragment() {

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
        mTimeView.setVisibility(View.VISIBLE);
        mTimeView.setText(news.getTime());
        mSourceView.setText(news.getSource());
        mAuthorView.setText(news.getSource());
        if(news.getCoverPicture() != null) {
            Glide.with(getContext()).load(news.getCoverPicture()).into(mCoverImageView);
        }
        else {
            mCoverImageView.setVisibility(View.GONE);
        }
        mIntroductionView.setText(news.getIntro());
        mContentView.setText(news.getContent());

    }


}
