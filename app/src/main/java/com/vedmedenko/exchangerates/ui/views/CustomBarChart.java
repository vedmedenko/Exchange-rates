package com.vedmedenko.exchangerates.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;

public class CustomBarChart extends BarChart {

    private ViewPager pager;

    public CustomBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent p_event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent p_event) {
        if (p_event.getAction() == MotionEvent.ACTION_MOVE && pager != null)
        {
            pager.requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(p_event);
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }
}
