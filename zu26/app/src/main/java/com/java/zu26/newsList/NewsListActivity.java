package com.java.zu26.newsList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsPersistenceContract;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.favorite.FavoriteActivity;
import com.java.zu26.setting.SettingActivity;
import com.java.zu26.util.DayNight;
import com.java.zu26.util.UserSetting;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NewsListPresenter mNewsPresenter = null;

    private Context mContext;

    private static ArrayList<Integer> categories = new ArrayList<Integer>();

    private static ViewPagerAdapter mPagerAdapter;

    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;

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
        /*try {
            if (UserSetting.isDay(NewsListActivity.this)) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        mContext = NewsListActivity.this;
        ///////////删除数据库以便测试！！！！！！！！！！！！！！！
        //mContext.deleteDatabase("News.db");

        mToolbar = (Toolbar) findViewById(R.id.newslist_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        for(int i = 0; i < 13; i++){
            categories.add(i);
        }

        mViewPager = (ViewPager) findViewById(R.id.newslist_viewpager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout = (TabLayout) findViewById(R.id.newslist_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setHomeAsUpIndicator(R.mipmap.category);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();



        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

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
    protected void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences("day_night_mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("day_night_mode");
        super.onDestroy();
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
        try {
            if (UserSetting.isDay(NewsListActivity.this)) {
                setTheme(R.style.AppTheme);
            } else {
                setTheme(R.style.NightTheme);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        refreshUI();
    }

    /**
     * 刷新UI界面
     */
    private void refreshUI() {
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        TypedValue toolbarColor = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorBackground, background, true);
        theme.resolveAttribute(R.attr.colorFont, textColor, true);
        theme.resolveAttribute(R.attr.colorPrimary, toolbarColor, true);

        mDrawerLayout.setBackgroundResource(background.resourceId);

        mTabLayout.setBackgroundResource(background.resourceId);

        mNavigationView.setBackgroundResource(background.resourceId);

        mToolbar.setBackgroundResource(toolbarColor.resourceId);

        refreshStatusBar();

        if(mNewsPresenter != null)mNewsPresenter.refreshUI(background, textColor);
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_list, menu);
        final MenuItem item = menu.findItem(R.id.news_list_category);
        final Menu m = menu;
        item.getActionView().findViewById(R.id.menu_newslist_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });
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
            case R.id.nav_setting:
                intent.setClass(this, SettingActivity.class);
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
