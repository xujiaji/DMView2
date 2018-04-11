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
