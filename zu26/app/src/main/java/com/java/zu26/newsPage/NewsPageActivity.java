package com.java.zu26.newsPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.utils.ActivityUtils;

/**
 * Created by lucheng on 2017/9/6.
 */

public class NewsPageActivity extends AppCompatActivity {

    private NewsPagePresenter mPresenter;

    private Toolbar mToolbar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspage);

        Bundle bundle = getIntent().getExtras();
        String newsId = bundle.getString("newsId");


        mToolbar = (Toolbar) findViewById(R.id.news_page_toolbar);

        NewsPageFragment newsPageFragment =
                (NewsPageFragment) getSupportFragmentManager().findFragmentById(R.id.page_frame);
        if (newsPageFragment == null) {
            // Create the fragment
            newsPageFragment = NewsPageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), newsPageFragment, R.id.page_frame);
        }

        mContext = NewsPageActivity.this;
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mPresenter = new NewsPagePresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), newsPageFragment, mToolbar, this);

        mPresenter.start(newsId);
        //final boolean rawFavorite = mPresenter.getNews().isFavorite();



        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mToolbar.setNavigationIcon(R.drawable.navigation);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void prepareToolbar(final boolean rawFavorite) {
        if(rawFavorite) mToolbar.getMenu().findItem(R.id.news_page_toolbar_favorite).setIcon(R.mipmap.favorite_true);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            boolean isFavorite = rawFavorite;

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.news_page_toolbar_share:
                        mPresenter.showShareDialog();
                        break;
                    case R.id.news_page_toolbar_favorite:
                        if(isFavorite) {
                            mPresenter.removeFromFavorites();
                            Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
                            menuItem.setIcon(R.mipmap.favorite);
                        }
                        else {
                            mPresenter.addToFavorites();
                            Toast.makeText(mContext, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            menuItem.setIcon(R.mipmap.favorite_true);
                        }
                        isFavorite = !isFavorite;
                        break;
                }

                if (!msg.equals("")) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

}
