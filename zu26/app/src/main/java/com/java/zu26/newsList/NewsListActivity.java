package com.java.zu26.newsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.java.zu26.R;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.utils.ActivityUtils;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListActivity extends AppCompatActivity {

    private NewsListPresenter mNewsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);

        /*
        TODO: 建立各种界面，先在xml文件里写，然后通过findViewById.
         */


        NewsListFragment newsListFragment =
                (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (newsListFragment == null) {
            // Create the fragment
            newsListFragment = NewsListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), newsListFragment, R.id.contentFrame);
        }

        // Create the presenter
        mNewsPresenter = new NewsListPresenter(NewsRepository.getInstance(), newsListFragment);

    }


}