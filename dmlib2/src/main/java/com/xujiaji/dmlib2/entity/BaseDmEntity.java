package com.xujiaji.dmlib2.entity;
/*
 * Copyright 2018 xujiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.graphics.Bitmap;
import android.view.View;

import com.xujiaji.dmlib2.Util;

/**
 * 弹幕实体类
 * Created by jiaji on 2018/2/26.
 */

public class BaseDmEntity implements Comparable<BaseDmEntity>
{
    private Bitmap bitmap;
    private boolean isSplice;//是否需要拼接
    private float left;
    private float top;
    private float right;
    private float bottom;
    private int priority;

    public BaseDmEntity(View itemView)
    {
        bitmap = Util.convertViewToBitmap(itemView);
        isSplice = false;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public boolean isSplice()
    {
        return isSplice;
    }

    public void setSplice(boolean splice)
    {
        isSplice = splice;
    }

    public float getLeft()
    {
        return left;
    }

    public void setLeft(float left)
    {
        this.left = left;
    }

    public float getTop()
    {
        return top;
    }

    public void setTop(float top)
    {
        this.top = top;
    }

    public float getRight()
    {
        return right;
    }

    public void setRight(float right)
    {
        this.right = right;
    }

    public float getBottom()
    {
        return bottom;
    }

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    @Override
    public int compareTo(BaseDmEntity o)
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
