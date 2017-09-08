package com.java.zu26.category;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.java.zu26.R;
import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsList.NewsListFragment;
import com.java.zu26.newsList.NewsListPresenter;
import com.java.zu26.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import static com.java.zu26.R.id.categoryLayout;

/**
 * Created by kaer on 2017/9/8.
 */

public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        List<String> labels = new ArrayList<>();
        labels.add("科技");
        labels.add("教育");
        labels.add("军事");
        labels.add("国内");
        labels.add("社会");
        labels.add("文化");
        labels.add("汽车");
        labels.add("国际");
        labels.add("体育");
        labels.add("财经");
        labels.add("健康");
        labels.add("娱乐");
//设置标签
        CategoryLayout categoryLayout = (CategoryLayout)findViewById(R.id.categoryLayout);
        categoryLayout.setLables(labels, true);
//获取选中的标签
        List<String> selectedLables = categoryLayout.getSelectedLables();

    }
}
