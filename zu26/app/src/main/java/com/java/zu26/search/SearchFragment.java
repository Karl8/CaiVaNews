package com.java.zu26.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsList.NewsListPresenter;
import com.java.zu26.newsPage.NewsPageActivity;
import com.java.zu26.util.UserSetting;

import java.util.ArrayList;

/**
 * Created by kaer on 2017/9/7.
 */

public class SearchFragment extends Fragment implements SearchContract.View{

    private Context mContext;

    private SearchContract.Presenter mPresenter;

    private SearchAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private int lastVisibleItem = 0;

    private int mPage = 0;

    private String mKeyWord = "";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView_search);
        mContext = getContext();
        mAdapter = new SearchFragment.SearchAdapter(mContext, new ArrayList<News>(0));
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);


        // 实现底部上拉刷新
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {


                    mPresenter.loadNews(mKeyWord, mPage + 1, true);
                    //Toast.makeText(activity-context, "加载成功", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });

        try {
            if (UserSetting.isDay(getContext())) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SearchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNews(String keyWord, int page, ArrayList<News> newsList) {
        Log.d("TAG", "showNews: " + newsList.size());
        mAdapter.replaceData(newsList);
        mPage = page;
        mKeyWord = keyWord;
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    public void clearNews() {
        Log.d("", "clearNews: ");
        mAdapter.clearData();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoNews(String keyWord, int page, ArrayList<News> newsList) {

    }

    private class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<News> newsList;

        private SearchFragment.OnItemClickListener itemListener;

        private Context context;

        public SearchAdapter(Context _context, ArrayList<News> _newsList) {
            this.inflater = LayoutInflater.from(_context);
            this.newsList = _newsList;
            this.context = _context;
        }

        public void replaceData(ArrayList<News> _newsList) {
            this.newsList.addAll(_newsList);
        }

        public void clearData() {
            Log.d("TAG", "onclearData:");
            if (newsList == null) {
                Log.d("TAG", "clearData: NULL");
            }
            else
                this.newsList.clear();
        }

        public News getItem(int i) {
            return newsList.get(i);

        }

        @Override
        public int getItemViewType(int position) {
            if(position <= getItemCount() )return 1;
            else return 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 1) {
                final View view = inflater.inflate(R.layout.search_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newTitle = view.findViewById(R.id.news_title);
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsPageActivity.class);
                        int position = (int)view.getTag();
                        News news = newsList.get(position);
                        newsList.set(position, new News(news, true, news.isFavorite()));
                        //Bundle bundle = new Bundle();
                        //bundle.putParcelable("news", mAdapter.getItem(position));
                        //bundle.putString("newsId", mAdapter.getItem(position).getId());
                        //intent.putExtras(bundle);
                        intent.putExtra("newsId", mAdapter.getItem(position).getId());
                        startActivity(intent);
                        Log.d("news list", "onClick: " + position + newsList.get(position).getTitle() + newsList.get(position).isRead());
                    }
                });
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("TAG", "onBindViewHolder: ");
            if(holder instanceof SearchFragment.ItemViewHolder) {
                Log.d("TAG", "onBindViewHolder: " + position);
                News news = newsList.get(position);
                SearchFragment.ItemViewHolder itemHolder = (SearchFragment.ItemViewHolder)holder;
                itemHolder.newsTitle.setText(news.getTitle());
                //itemHolder.newsTime.setText(news.getTime());
                itemHolder.newsSource.setText(news.getSource());
                String url = news.getCoverPicture();
                try {
                    if (UserSetting.getPictureMode(mContext) == 0) {
                        //Picasso.with(context).load(itemHolder.url).into(itemHolder.newsImage);
                        //itemHolder.newsImage.setImageBitmap(BitmapFactory.decodeStream(myurl.openStream()));
                        mPresenter.getCoverPicture(context, news, itemHolder.newsImage);
                    }

                    else {
                        Glide.with(context).load(R.drawable.downloading).into(itemHolder.newsImage);
                    }

                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                itemHolder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        void setOnItemClickListener(SearchFragment.OnItemClickListener listener) {
            itemListener = listener;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newsTime = null;
        TextView newsTitle = null;
        TextView newsSource = null;
        ImageView newsImage = null;

        public ItemViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.search_news_title);
            newsSource = (TextView) view.findViewById(R.id.search_news_source);
            newsImage = (ImageView) view.findViewById(R.id.search_news_image);
            newsTime = null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
