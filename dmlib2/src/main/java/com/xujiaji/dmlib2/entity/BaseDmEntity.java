package com.xujiaji.dmlib2.entity;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.xujiaji.dmlib2.DM;

/**
 * 弹幕实体类
 * Created by jiaji on 2018/2/26.
 */

public class BaseDmEntity implements Comparable<BaseDmEntity>
{
    private int _id;
    private int priority;
    private Bitmap bitmap;

    public BaseDmEntity(int id, View itemView, PointF start, PointF end)
    {
        this._id = id;
        bitmap = DM.convertViewToBitmap(itemView);
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
