package com.core.op.lib.weight;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by acer on 2017/3/7.
 */

public class VpSwipeRefreshLayout extends SwipeRefreshLayout {

    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private final int mTouchSlop;
    private boolean isMove;
    private boolean isFirstMove;

    private OnStartDraggerListener onStartDraggerListener;

    public void setOnStartDraggerListener(OnStartDraggerListener onStartDraggerListener) {
        this.onStartDraggerListener = onStartDraggerListener;
    }

    public VpSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                isMove = false;
                isFirstMove = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
//                isMove = true;
//                move();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
//                isMove = false;
//                isFirstMove = true;
//
//                if (onStartDraggerListener != null) {
//                    onStartDraggerListener.onStopDragger();
//                }
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    private void move() {
        if (isMove && isFirstMove) {
            startDragger();
            isFirstMove = false;
        }
    }

    protected void startDragger() {
        if (onStartDraggerListener != null) {
            onStartDraggerListener.onStartDragger();
        }
    }

    public interface OnStartDraggerListener {
        void onStartDragger();

        void onStopDragger();
    }
}