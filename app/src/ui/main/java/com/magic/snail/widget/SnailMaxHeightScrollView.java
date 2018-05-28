package com.magic.snail.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.magic.snail.assist.R;


/**
 * Created by cheney on 2017/6/16.
 */

public class SnailMaxHeightScrollView extends ScrollView {

    int maxHeight = 0;

    public SnailMaxHeightScrollView(Context c) {
        this(c, null);
    }

    public SnailMaxHeightScrollView(Context c, AttributeSet a) {
        this(c, a, 0);
    }

    public SnailMaxHeightScrollView(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.SnailMaxHeightScrollView);
        maxHeight = (int) ta.getDimension(R.styleable.SnailMaxHeightScrollView_qui_maxHeight, 0.0f);
        ta.recycle();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        Log.i("tt", "maxH: " + maxHeight + ",  measure： "+ measuredHeight);
        if (maxHeight < measuredHeight) {
            int measureMode = View.MeasureSpec.AT_MOST;
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
