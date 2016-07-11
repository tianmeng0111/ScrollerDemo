package com.tm.example.scrollerdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Tian on 2016/7/6.
 */
public class SlidingMenuLayout extends ViewGroup {
    private static final String TAG = SlidingMenuLayout.class.getSimpleName();
    private int mScaledTouchSlop;
    private Scroller mScroller;
    private View mContent;
    private View mMenu;

    private int mContentWidth;
    private int mMenuWidth;


    private int mTouchState = TOUCH_STATE_REST;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;

    private int mCurrentScreenShow = CONTENT_SHOW;
    private static final int MENU_SHOW = 1;
    private static final int CONTENT_SHOW = 0;
    private static final int INVALID_SHOW= -1;

    private float mDownX;
    private float mDownY;
    private float mLastX;

    private VelocityTracker velocityTracker;

    public SlidingMenuLayout(Context context) {
        super(context);
        init(context);
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.e(TAG, "===onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContent = getChildAt(0);
        mMenu = getChildAt(1);
        mContentWidth = widthMeasureSpec;
        mMenuWidth = mMenu.getLayoutParams().width;
        mContent.measure(widthMeasureSpec, heightMeasureSpec);
        mMenu.measure(mMenuWidth, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.e(TAG, "===onLayout");
        mContent.layout(0, 0, mContent.getMeasuredWidth(), mContent.getMeasuredHeight());
        mMenu.layout(mContent.getMeasuredWidth(), 0, mContent.getMeasuredWidth() + mMenu.getMeasuredWidth(), mContent.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "===onInterceptTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "===onInterceptTouchEvent   ACTION_DOWN");
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
//                Log.e(TAG, "mTouchState--->>" + mTouchState);//0
//                return true;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "===onInterceptTouchEvent  ACTION_MOVE");
                float x = ev.getX();
                if (Math.abs(x - mDownX) > mScaledTouchSlop) {
//                    Log.e(TAG, "===onInterceptTouchEvent");
                    mTouchState = TOUCH_STATE_SCROLLING;
                    return true;
                    //
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        //在拖动中代表正在滚动，给个返回true，会调用OnTouchEvent方法；
        // 滚动停止时返回false传递给子控件
//        Log.e(TAG, "return " + String.valueOf(mTouchState != TOUCH_STATE_REST));
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "===onTouchEvent");

//        acquireVelocityTracker(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "===onTouchEvent  ACTION_DOWN");
                mDownX = ev.getX();
                mLastX = mDownX;

                if (listener != null) {
                    listener.onTouch();
                }

                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "===onTouchEvent  ACTION_MOVE");

                float x = ev.getX();
                float deltaX = x - mLastX;
                mLastX = x;
                if (Math.abs(x - mDownX) > mScaledTouchSlop) {
                    if (deltaX > 0) {
                        if (getScrollX() > 0) {
                            scrollBy(-(int) deltaX, 0);
                        }
                    } else if (deltaX < 0){
                        if (Math.abs(deltaX) + getScrollX() <= mMenu.getWidth()) {
                            scrollBy(-(int) deltaX, 0);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.e(TAG, "===onTouchEvent  ACTION_CANCEL");

                float dX = mLastX - mDownX;
                if (Math.abs(dX) > mScaledTouchSlop) {
                    if (dX > 0) {
                        hideMenu();
                    } else {
                        showMenu();
                    }

                    slideInitial();


                }

//                releaseVelocityTracker();

                break;
        }
        return true;
    }

    /**
     * 如果左侧留白 滑动到初始位置
     */
    private void slideInitial() {
        if (getScrollX() < 0) {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, (int) (getScrollX() * 1.5));
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }



//    private void acquireVelocityTracker(final MotionEvent ev) {
//        if (velocityTracker == null) {
//            velocityTracker = VelocityTracker.obtain();
//        }
//        velocityTracker.addMovement(ev);
//    }
//
//    private void releaseVelocityTracker() {
//        if(null != this.velocityTracker) {
//            this.velocityTracker.clear();
//            this.velocityTracker.recycle();
//            this.velocityTracker = null;
//        }
//    }

    public void showMenu() {
        mScroller.startScroll(getScrollX(), 0, mMenuWidth - getScrollX(), 0, (int) (mMenuWidth * 1.5));
        invalidate();
        if (listener != null) {
            listener.onMenuShow();
        }
    }

    public void hideMenu() {
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, (int) (mMenuWidth * 1.5));
        invalidate();
    }



    public interface OnSlidingMenuListener {
        void onMenuShow();
        void onTouch();
    }

    public OnSlidingMenuListener listener;

    public void setOnSlidingMenuListener(OnSlidingMenuListener listener) {
        this.listener = listener;
    }

}
