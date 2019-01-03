package com.xujiaji.dmlib2.widget;
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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.SparseArray;
import android.view.View;

import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.LogUtil;
import com.xujiaji.dmlib2.SurfaceProxy;
import com.xujiaji.dmlib2.callback.OnDMAddListener;
import com.xujiaji.dmlib2.entity.BaseDmEntity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 动画路径绘制登帮助类
 */
public class Controller implements Runnable {
    private Direction mDirection = Direction.RIGHT_LEFT;
    private int mDuration = 3000;
    // 新弹幕
    private PriorityQueue<BaseDmEntity> mNewDMQueue = new PriorityQueue<>();
    // 已添加到屏幕的弹幕
    private List<BaseDmEntity> mAddedMDList = new LinkedList<>();
    private SurfaceProxy mSurfaceProxy;
    private int mWidth, mHeight;
    private int offset;
    private int hSpace = 20;// 水平间距
    private int vSpace = 20;// 垂直间距
    private boolean isRunning;
    private boolean isPause;// 是否是暂停状态
    private OnDMAddListener mOnDMAddListener;

    /**
     * @param width        画布登宽
     * @param height       画布的高
     * @param duration     展示一个弹幕多少秒
     * @param direction    动画允许方向
     * @param surfaceProxy 代理surface
     */
    void init(int width, int height, int duration, Direction direction, SurfaceProxy surfaceProxy) {
        mSurfaceProxy = surfaceProxy;
        mDirection = direction;
        mDuration = duration;
        this.mWidth = width;
        this.mHeight = height;
        offset = mWidth;
    }

    @Override
    public void run() {
        while (isRunning) {
            runTask();
        }
    }

    private void runTask() {
        offset -= 5;
        draw(offset);
        if (addDMInQueue()) {

        }
        else if (mAddedMDList.size() == 0) {
            isRunning = false;
        }
    }

    private void draw(int value) {
        Canvas canvas = null;
        try {
            canvas = mSurfaceProxy.lockCanvas();
            if (canvas == null) {
//                mValueAnim.cancel();
            } else {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                canvas.save();
                switch (mDirection) {
                    case RIGHT_LEFT:
                        canvas.translate(value, 0);
                        break;
                    case LEFT_RIGHT:
                        break;
                    case UP_DOWN:
                        break;
                    case DOWN_UP:
                        break;
                    default:
                }

                Iterator<BaseDmEntity> iterator = mAddedMDList.iterator();
                while (iterator.hasNext()) {
                    BaseDmEntity entity = iterator.next();
                    if (offset < -entity.rect.right) {
                        iterator.remove();
                    }
                    canvas.drawBitmap(entity.bitmap, entity.rect.left, entity.rect.top, null);
                }

                canvas.restore();
            }
        } catch (Exception e) {
            LogUtil.i(e.getMessage());
        } finally {
            if (canvas != null) {
                mSurfaceProxy.unlockCanvasAndPost(canvas);
            }
        }

    }

    public synchronized void add(View templateView) {
        BaseDmEntity entity = new BaseDmEntity(templateView);
        mNewDMQueue.add(entity);
        if (!isRunning) {
            start();
        }
    }

    private SparseArray<LinkedList<BaseDmEntity>> hierarchy = new SparseArray<>();
    private boolean addDMInQueue() {
        BaseDmEntity entity = mNewDMQueue.peek();

        if (entity == null) return false;
        int maxLeft = mWidth - offset;
        int maxRight = maxLeft + mWidth;
        // 没有添加过弹幕的时候
        if (mAddedMDList.size() == 0) {
            addToDisplay(entity);
            return true;
        }

        for (BaseDmEntity addedDM: mAddedMDList) {
            if (hierarchy.get(addedDM.rect.top) == null) {// 没有初始化的情况
                hierarchy.put(addedDM.rect.top, new LinkedList<BaseDmEntity>());
            }
            hierarchy.get(addedDM.rect.top).addFirst(addedDM);
        }

        BaseDmEntity lastDm = null;
        for (int i = 0; i < hierarchy.size(); i++) {
            LinkedList<BaseDmEntity> linkedList = hierarchy.get(hierarchy.keyAt(i));
            if (linkedList.size() == 0) continue;
            lastDm = linkedList.getFirst();
            if (lastDm.rect.right < maxRight) {
                entity.rect.offsetTo((lastDm.rect.right > maxLeft ? lastDm.rect.right : maxLeft) + hSpace, lastDm.rect.top);
                addToDisplay(entity);
                return true;
            }
        }

        if (lastDm == null) throw new RuntimeException("lastDm can not null");

        if (lastDm.rect.bottom < mHeight - lastDm.rect.height()) {
            entity.rect.offsetTo(maxLeft, lastDm.rect.bottom + vSpace);
            addToDisplay(entity);
            return true;
        }

        return false;
    }

    int dmNum = 0;
    private void addToDisplay(BaseDmEntity entity) {
        mNewDMQueue.remove(entity);
        mAddedMDList.add(entity);
        hierarchy.clear();
        dmNum ++;
        LogUtil.e("当前是添加的第几个弹幕：" + dmNum);
    }

    void start() {
        offset = mWidth;
        isRunning = true;
        new Thread(this).start();
    }

    void prepare() {
        if (isPause) {
            isRunning = true;
            new Thread(this).start();
        }
    }

    void pause() {
        isPause = true;
        isRunning = false;
    }

    void destroy() {
        offset = mWidth;
        isPause = false;
        isRunning = false;
        mAddedMDList.clear();
        mNewDMQueue.clear();
    }
}
