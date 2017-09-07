package com.java.zu26.newsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.java.zu26.R;
import com.java.zu26.utils.ActivityUtils;

/**
 * Created by lucheng on 2017/9/6.
 */

public class NewsPageActivity extends AppCompatActivity {

    private NewsPagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspage);

        NewsPageFragment newsPageFragment =
                (NewsPageFragment) getSupportFragmentManager().findFragmentById(R.id.page_frame);
        if (newsPageFragment == null) {
            // Create the fragment
            newsPageFragment = NewsPageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), newsPageFragment, R.id.page_frame);
        }
    }
}
