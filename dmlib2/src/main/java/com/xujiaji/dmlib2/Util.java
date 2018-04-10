package com.xujiaji.dmlib2;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * 绘制帮助类
 * Created by jiaji on 2018/2/19.
 */

public class Util
{
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void restoreBitmap(Bitmap bitmap)
    {
        if (bitmap == null || bitmap.isRecycled()) return;
        bitmap.recycle();
    }
}

