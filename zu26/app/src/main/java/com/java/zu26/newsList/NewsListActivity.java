package com.java.zu26.newsList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.utils.ActivityUtils;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListActivity extends AppCompatActivity {

    private NewsListPresenter mNewsPresenter;

    private Context mContext;

    private static ArrayList<Integer> categories = new ArrayList<Integer>();

    private static ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.newslist_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // 注意：之后每次category变化时，都要调用adapter的notify.
        for(int i = 1; i < 13; i++){
            categories.add(i);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.newslist_viewpager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.newslist_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mContext = NewsListActivity.this;
        //mContext.deleteDatabase("News.db");

    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            NewsListFragment newsListFragment = new NewsListFragment();
            Bundle args = new Bundle();
            args.putInt("category", categories.get(position));
            newsListFragment.setArguments(args);
            NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
            NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
            mNewsPresenter = new NewsListPresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), newsListFragment);
            return newsListFragment;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position).toString();
        }
    }

}
