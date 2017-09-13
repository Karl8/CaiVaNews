package com.java.zu26.newsList;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.zu26.R;
import com.java.zu26.data.News;

import com.java.zu26.data.NewsLocalDataSource;
import com.java.zu26.data.NewsRemoteDataSource;
import com.java.zu26.data.NewsRepository;
import com.java.zu26.newsPage.NewsPageActivity;
import com.java.zu26.search.SearchActivity;
import com.java.zu26.util.UserSetting;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by lucheng on 2017/9/3.
 */

public class NewsListFragment extends Fragment implements NewsListContract.View{

    private NewsListContract.Presenter mPresenter;

    private NewsListAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private Context mActivityContext;

    private int lastVisibleItem = 0;

    private int mPage = 0;

    private int mCategory = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    mAdapter.notifyDataSetChanged();
                    mAdapter.changeLoadingStatus(NewsListAdapter.LOAD_FINISH);
            }
        }
    };

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newslist, container, false);

        Bundle argumentBundle = getArguments();
        mCategory = argumentBundle.getInt("category");

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

                    mAdapter.changeLoadingStatus(NewsListAdapter.LOAD_PULLING_UP);
                    mPresenter.loadNews(mPage + 1, mCategory, true);
                    //mPresenter.loadNews(mPage + 1, , true);
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

                if(position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(mActivityContext, SearchActivity.class);
                    startActivity(intent);
                    return;
                }
                else if(position + 1 == mAdapter.getItemCount())
                    return;
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), NewsPageActivity.class);

                    //Bundle bundle = new Bundle();
                    //bundle.putParcelable("news", mAdapter.getItem(position));
                    //bundle.putString("newsId", mAdapter.getItem(position).getId());
                    //intent.putExtras(bundle);
                    intent.putExtra("newsId", mAdapter.getItem(position - 1).getId());
                    startActivity(intent);
                }

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCategory == 0) {
            //mPage = 0;
            ///mAdapter.clearData();
            //mAdapter.notifyDataSetChanged();
        }
        mPresenter.start();
    }

    @Override
    public void setPresenter(NewsListPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setContext(Context context) {
        mActivityContext = context;
    }

    @Override
    public void showNews(int page, int category, ArrayList<News> newslist) {
        Log.d("TAG", "showNews: " + newslist.size());
        mAdapter.replaceData(newslist);
        mPage = page;
        mCategory = category;
        mRecyclerView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
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

    @Override
    public int getCategory() {
        return mCategory;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public void refreshUI(TypedValue background, TypedValue textColor) {

        mRecyclerView.setBackgroundResource(background.resourceId);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            childView.setBackgroundResource(background.resourceId);

            View card = childView.findViewById(R.id.newslist_cardview);
            card.setBackgroundResource(background.resourceId);
            ViewGroup table = childView.findViewById(R.id.newslist_tablelayout);
            table.setBackgroundResource(background.resourceId);
            ViewGroup tablerow = childView.findViewById(R.id.newslist_tablelayout);
            tablerow.setBackgroundResource(background.resourceId);
            ViewGroup rela = childView.findViewById(R.id.newslist_item_RelativeLayout);
            rela.setBackgroundResource(background.resourceId);
            TextView news_title = (TextView) childView.findViewById(R.id.news_title);
            news_title.setBackgroundResource(background.resourceId);
            news_title.setTextColor(textColor.resourceId);
            TextView motto = (TextView) childView.findViewById(R.id.news_source);
            motto.setBackgroundResource(background.resourceId);
            motto.setTextColor((textColor.resourceId));
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        private ArrayList<News> newslist;

        private OnItemClickListener itemListener;

        private Context context;

        private int loadingStatus;

        private static final int TYPE_SEARCH = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;

        private static final int LOAD_PULLING_UP = 0;
        private static final int LOAD_LOADING = 1;
        private static final int LOAD_FINISH = 2;

        public NewsListAdapter(Context _context, ArrayList<News> _newsList) {
            this.inflater = LayoutInflater.from(_context);
            this.newslist = _newsList;
            this.context = _context;
        }

        public void replaceData(ArrayList<News> _newsList) {
            this.newslist.addAll(_newsList);
        }

        public News getItem(int i) {
            return newslist.get(i);

        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) return TYPE_FOOTER;
            else if (position == 0) return TYPE_SEARCH;
            else return TYPE_ITEM;
        }

        public void clearData() {
            Log.d("TAG", "onclearData:");
            if (newslist == null) {
                Log.d("TAG", "clearData: NULL");
            }
            else
                this.newslist.clear();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_ITEM) {
                final View view = inflater.inflate(R.layout.newslist_itemlayout, parent, false);
                RecyclerView.ViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newsTitle = view.findViewById(R.id.news_title);
                        newsTitle.setTextColor(getResources().getColor(R.color.colorCategoryGray));
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsPageActivity.class);
                        int position = (int)view.getTag();
                        News news = getItem(position - 1);
                        newslist.set(position, new News(news, true, news.isFavorite()));
                        //Bundle bundle = new Bundle();
                        //bundle.putParcelable("news", mAdapter.getItem(position));
                        //bundle.putString("newsId", mAdapter.getItem(position).getId());
                        //intent.putExtras(bundle);
                        intent.putExtra("newsId", mAdapter.getItem(position - 1).getId());
                        startActivity(intent);
                    }
                });
                return holder;
            }
            else if(viewType == TYPE_SEARCH) {
                final View view = inflater.inflate(R.layout.newslist_search_layout, parent, false);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivityContext, SearchActivity.class);
                        startActivity(intent);
                    }
                });
                return new SearchViewHolder(view);
            }
            else if(viewType ==TYPE_FOOTER) {
                final View view = inflater.inflate(R.layout.newslist_footer_layout, parent, false);
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("TAG", "onBindViewHolder: ");
            if(holder instanceof ItemViewHolder) {
                News news = getItem(position - 1);
                //Log.d("TAG", "onBindViewHolder: " + position + " read : " + news.isRead() + " " + news.getTitle());

                ItemViewHolder itemHolder = (ItemViewHolder)holder;
                itemHolder.newsTitle.setText(news.getTitle());
                if (news.isRead()) {
                    //Log.d("read", "onBindViewHolder: read" + String.valueOf(position));
                    itemHolder.newsTitle.setTextColor(Color.rgb(158, 158, 158));
                }
                else
                    itemHolder.newsTitle.setTextColor(Color.rgb(0, 0, 0));
                //itemHolder.newsTime.setText(news.getTime());
                itemHolder.newsSource.setText(news.getSource());
                String url = news.getCoverPicture();
                try {
                    if (UserSetting.getPictureMode(mActivityContext) == 0 && url != null && url.length() > 0) {
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

            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerHolder = (FooterViewHolder) holder;
                TextView text = footerHolder.loadText;
                switch (loadingStatus) {
                    case LOAD_PULLING_UP:
                        text.setText(R.string.load_text_pulling_up); break;
                    case LOAD_LOADING:
                        text.setText(R.string.load_text_loading); break;
                    case LOAD_FINISH:
                        text.setText(R.string.load_text_finish); break;
                    default:
                        text.setText("");
                }
            }

            else if (holder instanceof SearchViewHolder) {
                return;
            }
        }



        @Override
        public int getItemCount() {
            return newslist.size() + 2;
        }

        void changeLoadingStatus(int newStatus) {
            loadingStatus = newStatus;
            notifyDataSetChanged();
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

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView loadText = null;

        public FooterViewHolder(View view) {
            super(view);
            loadText = (TextView) view.findViewById(R.id.load_text);
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


