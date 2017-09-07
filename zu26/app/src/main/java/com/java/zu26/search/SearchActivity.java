package com.java.zu26.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.java.zu26.R;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;

import com.java.zu26.utils.ActivityUtils;

/**
 * Created by kaer on 2017/9/7.
 */

public class SearchActivity extends AppCompatActivity {
    private SearchPresenter mSearchPresenter;

    private Context mContext;

    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
        TODO: 建立各种界面，先在xml文件里写，然后通过findViewById.
         */
        EditText editText = (EditText) findViewById(R.id.search_edit_text);
        editText.setHorizontallyScrolling(true); // 设置 edit_text不换行
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("", "beforeTextChanged: ");
                mSearchFragment.clearNews();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("", "onTextChanged: ");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: " + editable.toString() + " " + editable.toString().length());
                if (editable.toString().isEmpty())
                    mSearchFragment.clearNews();
                else
                    mSearchPresenter.loadNews(editable.toString(), 1, true);
            }
        });

        mSearchFragment =
                (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_result_frame);
        if (mSearchFragment == null) {
            // Create the fragment
            mSearchFragment = SearchFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mSearchFragment, R.id.search_result_frame);
        }


        // Create the presenter
        mContext = SearchActivity.this;
        //mContext.deleteDatabase("News.db");
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(mContext);
        NewsRemoteDataSource newsRemoteDataSource = NewsRemoteDataSource.getInstance();
        mSearchPresenter = new SearchPresenter(NewsRepository.getInstance(newsRemoteDataSource, newsLocalDataSource), mSearchFragment);

    }
}
