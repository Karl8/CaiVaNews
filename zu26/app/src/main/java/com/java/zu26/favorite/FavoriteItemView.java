package com.java.zu26.favorite;

import android.support.v7.widget.CardView;
import android.widget.HorizontalScrollView;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.java.zu26.R;

/**
 * Created by kaer on 2017/9/9.
 */

public class FavoriteItemView extends HorizontalScrollView {

    //删除按钮
    private CardView mCardView_Delete;

    //滚动条可以移动距离
    private int mScrollWidth;

    private onSlidingButtonListener mOnSlidingButtonListener;

    private Boolean isOpen = false;

    public FavoriteItemView(Context context) {
        this(context, null);
    }

    public FavoriteItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FavoriteItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(mCardView_Delete == null){
            mCardView_Delete = (CardView) findViewById(R.id.favorite_delete_card);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(0,0);
            mScrollWidth = mCardView_Delete.getWidth() + 8;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mOnSlidingButtonListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //让删除按钮显示在item的背后效果
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d("scroll", "onScrollChanged: l:" + l + " t:" + t + " oldl:" + oldl + " oldt:" + t );
        //mTextView_Delete.setTranslationX(l - mScrollWidth);
    }

    public void changeScrollx(){
        if(getScrollX() >= (mScrollWidth/2)){
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            mOnSlidingButtonListener.onMenuIsOpen(this);
        }else{
            this.smoothScrollTo(0, 0);
            isOpen = false;
        }
    }

    public void openItem()
    {
        if (isOpen){
            return;
        }
        this.smoothScrollTo(mScrollWidth, 0);
        isOpen = true;
        mOnSlidingButtonListener.onMenuIsOpen(this);
    }

    public void closeItem()
    {
        if (!isOpen){
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    public void setSlidingButtonListener(onSlidingButtonListener listener){
        mOnSlidingButtonListener = listener;
    }

    public interface onSlidingButtonListener{
        void onMenuIsOpen(View view);
        void onDownOrMove(FavoriteItemView slidingButtonView);
    }

}
