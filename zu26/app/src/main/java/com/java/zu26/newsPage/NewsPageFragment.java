package com.java.zu26.newsPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lucheng on 2017/9/7.
 */

public class NewsPageFragment extends Fragment implements NewsPageContract.View {

    NewsPagePresenter mPresenter;


    public static NewsPageFragment newInstance() {
        return new NewsPageFragment();
    }

    public NewsPageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setPresenter(NewsPagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNews() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
