package com.xujiaji.dmlib2;


import android.graphics.Bitmap;
import android.view.View;

/**
 * 绘制帮助类
 * Created by jiaji on 2018/2/19.
 */

public class DM
{

    private static DM mHelper;
    public static DM getInstance()
    {
        if (mHelper == null)
        {
            throw new RuntimeException("DM did not initialize.");
        } else
        {
            return mHelper;
        }
    }

    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static void restoreBitmap(Bitmap bitmap)
    {
        if (bitmap == null || bitmap.isRecycled()) return;
        bitmap.recycle();
    }
}

