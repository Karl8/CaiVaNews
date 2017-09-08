package com.java.zu26.mainActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsPage.NewsPageActivity;
import com.java.zu26.search.SearchActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, NewsListActivity.class));
        //startActivity(new Intent(this, SearchActivity.class));
        //startActivity(new Intent(this, CategoryActivity.class));
    }
}
