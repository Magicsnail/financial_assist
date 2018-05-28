package com.magic.snail.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.magic.snail.assist.R;


/**
 * Created by cheney on 2017/5/11.
 */

public class SUI {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spValue,
            context.getResources().getDisplayMetrics());
    }

    public static int getStatusBarHeight(Context context) {
        int result;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId <= 0) {
                resourceId = R.dimen.qui_def_status_bar_height;
            }
            result = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            result = context.getResources().getDimensionPixelSize(R.dimen.qui_def_status_bar_height);
        }
        return result;
    }

    public static Point getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size;
    }


    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
