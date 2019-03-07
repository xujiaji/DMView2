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
import android.graphics.RectF;
import android.view.View;

import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.Util;

/**
 * 弹幕实体类
 * Created by jiaji on 2018/2/26.
 */

public class BaseDmEntity implements Comparable<BaseDmEntity> {
    public final Bitmap bitmap;
    public final RectF rect = new RectF();
    public final int priority;

    public BaseDmEntity(View itemView) {
        this(itemView, 0);
    }

    public BaseDmEntity(View itemView, int priority) {
        bitmap = Util.convertViewToBitmap(itemView);
        this.priority = priority;
        this.rect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * 是否需要绘制
     * @param direction 弹幕运动方向
     * @param displayDis 展示区域长度（相对于运动方向的长度）
     * @return 是否需要绘制该弹幕（如果没有展示出来，则不需要绘制）
     */
    public boolean isNeedDraw(Direction direction, int displayDis) {
        switch (direction) {
            case RIGHT_LEFT:
            case LEFT_RIGHT:
                return rect.left < displayDis;
            case DOWN_UP:
            case UP_DOWN:
                return rect.top < displayDis;
        }
        throw new RuntimeException("not direction " + direction.name() + " in 'isNeedDraw()'");
    }

    @Override
    public int compareTo(BaseDmEntity o) {
        if (this.priority > o.priority) {
            return 1;
        } else if (this.priority < o.priority) {
            return -1;
        } else {
            return 0;
        }
    }
}
