package com.xujiaji.dmlib2.entity;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;

import com.xujiaji.dmlib2.Util;

/**
 * 弹幕实体类
 * Created by jiaji on 2018/2/26.
 */

public class BaseDmEntity implements Comparable<BaseDmEntity>
{
    private Bitmap bitmap;
    private int priority;

    public BaseDmEntity(View itemView)
    {
        bitmap = Util.convertViewToBitmap(itemView);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }


    @Override
    public int compareTo(@NonNull BaseDmEntity o)
    {
        if (this.priority > o.priority)
        {
            return 1;
        } else if (this.priority < o.priority)
        {
            return -1;
        } else
        {
            return 0;
        }
    }
}
