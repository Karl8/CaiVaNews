package com.java.zu26.newsList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.zu26.R;
import com.java.zu26.data.News;
import com.java.zu26.newsPage.NewsPageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListFragment extends Fragment implements NewsListContract.View{

    private NewsListContract.Presenter mPresenter;

    private NewsListAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private int lastVisibleItem = 0;

    private int mPage = 0;

    private int mCategory = 0;

    public NewsListFragment() {

    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newslist, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mAdapter = new NewsListAdapter(getContext(), new ArrayList<News>(0));
        mRecyclerView.setAdapter(mAdapter);

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swipeRefreshLayout1);
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

                    // 不需要向上更新新闻，只需要向下读取内容，因此为false.
                    mPresenter.loadNews(mPage + 1, mCategory, false);
                    //Toast.makeText(activity-context, "加载成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        // 实现顶部上拉刷新(其实没有刷新23333)
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                // 其实应该改成true，但是没有关系，因为老师的数据库不会更新.
                mPresenter.loadNews(1, mCategory, false);
            }
        });


        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getContext(), NewsPageActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("news", mAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(NewsListPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNews(int page, int category, ArrayList<News> newslist) {
        Log.d("TAG", "showNews: " + newslist.size());
        mAdapter.replaceData(newslist);
        mPage = page;
        mCategory = category;
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoNews(int page, int category, ArrayList<News> newslist) {

    }


    @Override
    public void setLoadingIndicator(final boolean active) {
        if(getView() == null){
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout1);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    private class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<News> newslist;

        private OnItemClickListener itemListener;

        private Context context;


        public NewsListAdapter(Context _context, ArrayList<News> _newslist) {
            this.inflater = LayoutInflater.from(_context);
            this.newslist = _newslist;
            this.context = _context;
        }

        public void replaceData(ArrayList<News> _newslist) {
            this.newslist.addAll(_newslist);
        }

        public News getItem(int i) {
            return newslist.get(i);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View view = inflater.inflate(R.layout.newslist_itemlayout, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null)
                        itemListener.onItemClick(v,(int)v.getTag());
                }
            });
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("TAG", "onBindViewHolder: ");
            if(holder instanceof ItemViewHolder) {
                News news = newslist.get(position);
                ItemViewHolder itemHolder = (ItemViewHolder)holder;
                itemHolder.newsTitle.setText(news.getTitle());
                //itemHolder.newsTime.setText(news.getTime());
                itemHolder.newsSource.setText(news.getSource());
                String url = news.getCoverPicture();
                try {
                    if (url != null && url.length() > 0) {
                        Log.d("aaa","asaa");
                        Picasso.with(context).load(url).into(itemHolder.newsImage);
                        //itemHolder.newsImage.setImageBitmap(BitmapFactory.decodeStream(myurl.openStream()));
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
            return newslist.size();
        }

        void setOnItemClickListener(OnItemClickListener listener) {
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
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsSource = (TextView) view.findViewById(R.id.news_source);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsTime = null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


