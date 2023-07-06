package com.example.kishservices.services;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class SwipeControlViewPager extends ViewPager {

    private boolean swipeEnabled;

    public SwipeControlViewPager(Context context) {
        super(context);
        swipeEnabled = true;
    }

    public SwipeControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        swipeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return swipeEnabled && super.onInterceptTouchEvent(event);
    }

    public void setSwipeEnabled(boolean enabled) {
        swipeEnabled = enabled;
    }

    public void swipeToNextPage() {
        int currentItem = getCurrentItem();
        if (currentItem < getAdapter().getCount() - 1) {
            setCurrentItem(currentItem + 1);
        }
    }

    public void swipeToPreviousPage() {
        int currentItem = getCurrentItem();
        if (currentItem > 0) {
            setCurrentItem(currentItem - 1);
        }
    }
}