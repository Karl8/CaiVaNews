package com.java.zu26.newsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsPersistenceContract;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.favorite.FavoriteActivity;
import com.java.zu26.search.SearchActivity;
import com.java.zu26.util.ActivityUtils;
import com.java.zu26.util.UserSetting;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NewsListPresenter mNewsPresenter;

    private Context mContext;

    private static ArrayList<Integer> categories = new ArrayList<Integer>();

    private static ViewPagerAdapter mPagerAdapter;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);

        mContext = NewsListActivity.this;
        ///////////删除数据库以便测试！！！！！！！！！！！！！！！
        //mContext.deleteDatabase("News.db");

        Toolbar toolbar = (Toolbar) findViewById(R.id.newslist_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.news_list_category:
                        Intent intent = new Intent();
                        intent.setClass(mContext, CategoryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

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

        /*
        TextView mSearch = (TextView) findViewById(R.id.newslist_searchview);
        mSearch.setText("请输入搜索内容...");
        mSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsListActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

/*
        ViewTreeObserver viewTreeObserver = tabLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
*/
        //mContext.deleteDatabase("News.db");

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> cs = UserSetting.loadCategorySetting(mContext);
        categories = new ArrayList<Integer>();
        for(String s : cs) {
            categories.add(Integer.valueOf(s));
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_list, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {  //此处处理侧边栏不同点击事件的结果
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.nav_favorites:
                intent.setClass(this, FavoriteActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            newsListFragment.setContext(mContext);
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
            String id = String.valueOf(categories.get(position));
            return NewsPersistenceContract.NewsEntry.categoryDict.get(id);
        }
    }

}
