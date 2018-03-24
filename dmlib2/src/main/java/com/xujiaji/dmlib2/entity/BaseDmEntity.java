package com.xujiaji.dmlib2.entity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xujiaji.dmlib2.DM;
import com.xujiaji.dmlib2.PointCreator;
import com.xujiaji.dmlib2.PointFEvaluator;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 弹幕实体类
 * Created by jiaji on 2018/2/26.
 */

public class BaseDmEntity implements Comparable<BaseDmEntity>
{
    private int _id;
    private int priority;
    private Bitmap bitmap;
    private Queue<PointF> positions = new LinkedList<>();
    private ValueAnimator anim;

    public BaseDmEntity(int id, View itemView, PointF start, PointF end)
    {
        this._id = id;
        bitmap = DM.convertViewToBitmap(itemView);
        anim = ValueAnimator.ofObject(new PointFEvaluator(), start, end);
        anim.setDuration(3000);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF position = (PointF) animation.getAnimatedValue();
                PointF p = PointCreator.getInstance().newPointF();
                p.set(position);
                positions.offer(p);
            }
        });
    }

    public Queue<PointF> getPositions()
    {
        return positions;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    /**
     * 开启当前元素的动画
     */
    public void start()
    {
        if (anim == null || anim.isRunning()) return;
        anim.start();
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
