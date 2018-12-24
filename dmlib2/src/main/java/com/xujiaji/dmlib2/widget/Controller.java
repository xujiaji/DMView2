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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.LogUtil;
import com.xujiaji.dmlib2.SurfaceProxy;
import com.xujiaji.dmlib2.Util;
import com.xujiaji.dmlib2.callback.OnDMAddListener;
import com.xujiaji.dmlib2.entity.BaseDmEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 动画路径绘制登帮助类
 */
public class Controller
{
    private Direction mDirection = Direction.RIGHT_LEFT;
    private int mDuration = 3000;
    private boolean isRun = false;
    private PriorityQueue<BaseDmEntity> mQueue = new PriorityQueue<>();
    private Set<BaseDmEntity> mSpliceList = new LinkedHashSet<>();
    private SurfaceProxy mSurfaceProxy;
    private boolean isActive;//是否是活跃状态
    private Bitmap mOneBitmap;
    private Bitmap mTwoBitmap;
    private Bitmap mDrawDMBitmap;

    private Canvas mOneCanvas;
    private Canvas mTwoCanvas;
    private Canvas mDrawDMCanvas;
    private float mUseWidth;//绘制了的弹幕占用的宽
    private float mUseHeight;//绘制了的弹幕占用的高

    private int mWidth, mHeight;
    private ValueAnimator mValueAnim;
    private int mOneLeft, mTwoLeft, mOneTop, mTwoTop;
    private final Random random = new Random();

    private OnDMAddListener mOnDMAddListener;

    /**
     *
     * @param width 画布登宽
     * @param height 画布的高
     * @param duration 展示一个弹幕多少秒
     * @param direction 动画允许方向
     * @param surfaceProxy 代理surface
     */
    void init(int width, int height, int duration, Direction direction, SurfaceProxy surfaceProxy)
    {
        mSurfaceProxy = surfaceProxy;
        mDirection = direction;
        isActive = true;
        mUseWidth = 0;
        mUseHeight = 0;
        mDuration = duration;
        mOneBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mTwoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mDrawDMBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mOneCanvas = new Canvas(mOneBitmap);
        mTwoCanvas = new Canvas(mTwoBitmap);
        mDrawDMCanvas = new Canvas(mDrawDMBitmap);
//        mOneCanvas.drawColor(Color.RED);
//        mTwoCanvas.drawColor(Color.GREEN);

        this.mWidth = width;
        this.mHeight = height;

        switch (mDirection)
        {
            case DOWN_UP:
                mTwoTop = 0;
                mOneTop = mHeight;
                mValueAnim = ValueAnimator.ofInt(0, mHeight).setDuration(mDuration);
                break;
            case UP_DOWN:
                mTwoTop = 0;
                mOneTop = -mHeight;
                mValueAnim = ValueAnimator.ofInt(0, mHeight).setDuration(mDuration);
                break;
            case LEFT_RIGHT:
                mTwoLeft = 0;
                mOneLeft = -mWidth;
                mValueAnim = ValueAnimator.ofInt(0, mWidth).setDuration(mDuration);
                break;
            case RIGHT_LEFT:
                mTwoLeft = 0;
                mOneLeft = mWidth;
                mValueAnim = ValueAnimator.ofInt(0, mWidth).setDuration(mDuration);
                break;
            default:
        }
        mValueAnim.setInterpolator(new LinearInterpolator());
        mValueAnim.setRepeatCount(-1);
        mValueAnim.setRepeatMode(ValueAnimator.RESTART);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                final int value = (int) animation.getAnimatedValue();
                startRun(value);
            }
        });

        mValueAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                LogUtil.i("onAnimationRepeat");
                animRepeat();
            }
        });

        mValueAnim.start();

        addFromQueue(mQueue);
    }


    /**
     * 动画交替，数值变化
     */
    private void animRepeat()
    {
        mUseWidth = 0;
        mUseHeight = 0;

        switch (mDirection)
        {
            case DOWN_UP:
                if (mTwoTop == 0)
                {
                    mTwoTop = mHeight;
                    mOneTop = 0;
                    exchangeTwoHandle();
                } else
                {
                    mTwoTop = 0;
                    mOneTop = mHeight;
                    exchangeOneHandle();
                }
                break;
            case UP_DOWN:
                if (mTwoTop == 0)
                {
                    mTwoTop = -mHeight;
                    mOneTop = 0;
                    exchangeTwoHandle();
                } else
                {
                    mTwoTop = 0;
                    mOneTop = -mHeight;
                    exchangeOneHandle();
                }
                break;
            case LEFT_RIGHT:
                if (mTwoLeft == 0)
                {
                    mTwoLeft = -mWidth;
                    mOneLeft = 0;
                    exchangeTwoHandle();
                } else
                {
                    mTwoLeft = 0;
                    mOneLeft = -mWidth;
                    exchangeOneHandle();
                }
                break;
            case RIGHT_LEFT:
                if (mTwoLeft == 0)//动画重新开始时，判断如果mTwoBitmap是第一张图片，那么说明该次mTwoBitmap已展示完毕
                {
                    mTwoLeft = mWidth;//那么mTwoBitmap将重新开始展示
                    mOneLeft = 0;//mOneBitmap将从完全展示到消失
                    exchangeTwoHandle();

                } else
                {//mOneBitmap已展示完毕
                    mTwoLeft = 0;
                    mOneLeft = mWidth;
                    exchangeOneHandle();
                }
                break;
        }

        mDrawDMCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清理后台绘制弹幕的画布
//        n ++;
//        if (n % 2 == 0)
//        {
//            mDrawDMCanvas.drawColor(Color.BLUE);
//        } else
//        {
//            mDrawDMCanvas.drawColor(Color.GREEN);
//        }

        Set<BaseDmEntity> set = new LinkedHashSet<>(mSpliceList);
        for (Iterator<BaseDmEntity> it = set.iterator(); it.hasNext();)
        {
            BaseDmEntity dm = it.next();
            add(dm);
            if (dm.isSplice()) // 去掉有效弹幕
            {
                it.remove();
            }
        }

        //清楚无效弹幕
        mSpliceList.removeAll(set);

        addFromQueue(mQueue);
    }

//    int n = 1;

    /**
     * 交换mOneBitmap和mDrawDMBitmap的引用
     */
    private void exchangeOneHandle()
    {
        Bitmap b = mOneBitmap;
        Canvas c = mOneCanvas;
        mOneBitmap = mDrawDMBitmap;
        mOneCanvas = mDrawDMCanvas;
        mDrawDMBitmap = b;
        mDrawDMCanvas = c;
    }

    /**
     * 交换mTwoBitmap和mDrawDMBitmap的引用
     */
    private void exchangeTwoHandle()
    {
        Bitmap b = mTwoBitmap;
        Canvas c = mTwoCanvas;
        mTwoBitmap = mDrawDMBitmap;
        mTwoCanvas = mDrawDMCanvas;
        mDrawDMBitmap = b;
        mDrawDMCanvas = c;
    }

    private void startRun(int value)
    {
        Canvas canvas = null;
        try
        {
            canvas = mSurfaceProxy.lockCanvas();
            if (canvas == null)
            {
//                mValueAnim.cancel();
            } else
            {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                switch (mDirection)
                {
                    case RIGHT_LEFT:
                        canvas.drawBitmap(mOneBitmap, mOneLeft - value, 0, null);
                        canvas.drawBitmap(mTwoBitmap, mTwoLeft - value, 0, null);
                        break;
                    case LEFT_RIGHT:
                        canvas.drawBitmap(mOneBitmap, mOneLeft + value, 0, null);
                        canvas.drawBitmap(mTwoBitmap, mTwoLeft + value, 0, null);
                        break;
                    case UP_DOWN:
                        canvas.drawBitmap(mOneBitmap, 0, mOneTop + value, null);
                        canvas.drawBitmap(mTwoBitmap, 0, mTwoTop + value, null);
                        break;
                    case DOWN_UP:
                        canvas.drawBitmap(mOneBitmap, 0, mOneTop - value, null);
                        canvas.drawBitmap(mTwoBitmap, 0, mTwoTop - value, null);
                        break;
                    default:
                }
            }
        } catch (Exception e)
        {
            LogUtil.i(e.getMessage());
        } finally
        {
            if (canvas != null)
            {
                mSurfaceProxy.unlockCanvasAndPost(canvas);
            }
        }

    }


    /**
     * 弹幕从上往下跑
     * 绘制弹幕：从画布下到上，左到右绘制
     */
    private void drawUpDown(BaseDmEntity entity)
    {
        Bitmap bitmap = entity.getBitmap();
        final float left;
        final float top;
        if (entity.isSplice())
        {
            final float diff = mHeight - bitmap.getHeight();
            top = diff < 0 ? 0 : diff;
            left = entity.getLeft();
        } else if (mUseWidth == 0 && mUseHeight == 0)//第一次添加的情况
        {
            top = mHeight - bitmap.getHeight() - random.nextInt(100);
            left = 0;
        } else if (mHeight - mUseHeight > bitmap.getHeight())//顶部空间足的情况
        {
            top = mHeight - mUseHeight - bitmap.getHeight();
            left = mUseWidth - bitmap.getWidth();
        } else if (mHeight - mUseHeight < bitmap.getHeight() && mWidth - mUseWidth > bitmap.getWidth())//顶部空间不足，右侧空间足到情况
        {
            top = mHeight - bitmap.getHeight() - random.nextInt(100);
            left = mUseWidth;
        } else
        {
            mQueue.offer(entity);
            return;
        }

        mUseWidth = left + bitmap.getWidth();
        mUseHeight = mHeight - top;
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);

        if (top < 0)
        {
            final int newBitmapHeight = (int) (Math.abs(top));
            entity.setSplice(true);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), newBitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            entity.setBitmap(newBitmap);
            entity.setLeft(left);
            mSpliceList.add(entity);
        } else
        {
            entity.setSplice(false);
        }

        dmAdded(entity);
        bitmap.recycle();
    }

    /**
     * 弹幕从下往上跑
     * 绘制弹幕：从画布上到下，左到右绘制
     */
    private void drawDownUp(BaseDmEntity entity)
    {
        Bitmap bitmap = entity.getBitmap();
        final float left;
        final float top;
        if (entity.isSplice())
        {
            top = 0;
            left = entity.getLeft();
        }
        else if (mUseWidth == 0 && mUseHeight == 0)//第一次添加的情况
        {
            top = random.nextInt(100);
            left = 0;
        } else if (mHeight - mUseHeight > bitmap.getHeight())//底部空间足的情况
        {
            left = mUseWidth - bitmap.getWidth();
            top = mUseHeight;
        } else if (mHeight - mUseHeight < bitmap.getHeight() && mWidth - mUseWidth > bitmap.getWidth())//底部空间不足，右侧空间足到情况
        {
            left = mUseWidth;
            top = random.nextInt(100);
        } else
        {
            mQueue.offer(entity);
            return;
        }

        mUseWidth = left + bitmap.getWidth();
        mUseHeight = top + bitmap.getHeight();
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);

        if (mUseHeight > mHeight)
        {
            final int newBitmapHeight = (int) (bitmap.getHeight() - mHeight + top);
            entity.setSplice(true);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), newBitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap, 0, -mHeight + top, null);
            entity.setBitmap(newBitmap);
            entity.setLeft(left);
            mSpliceList.add(entity);
        } else
        {
            entity.setSplice(false);
        }

        dmAdded(entity);
        bitmap.recycle();
    }

    /**
     * 弹幕从右往左跑
     * 绘制弹幕：从画布左到右，上到下绘制
     */
    private void drawRightLeft(BaseDmEntity entity)
    {
        Bitmap bitmap = entity.getBitmap();
        final float left;
        final float top;
        if (entity.isSplice())
        {
            top = entity.getTop();
            left = 0;
        }
        else if (mUseWidth == 0 && mUseHeight == 0)//第一次添加的情况
        {
            left = random.nextInt(100);
            top = 0;
        } else if (mWidth - mUseWidth > bitmap.getWidth())//右侧空间足的情况
        {
            left = mUseWidth;
            top = mUseHeight - bitmap.getHeight();
        } else if (mWidth - mUseWidth < bitmap.getWidth() && mHeight - mUseHeight > bitmap.getHeight())//右侧空间不足，底部空间足到情况
        {
            left = random.nextInt(100);
            top = mUseHeight;
        } else//此时画布空间不足了，将这个弹幕添加到容器当中
        {
//            LogUtil.i("此时画布空间不足了，将这个弹幕添加到容器当中， num = " + num);
            mQueue.offer(entity);
            return;
        }

        mUseWidth = left + bitmap.getWidth();
        mUseHeight = top + bitmap.getHeight();
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);


        if (mUseWidth > mWidth)
        {
            final int newBitmapWidth = (int) (bitmap.getWidth() - mWidth + left);
            entity.setSplice(true);
            Bitmap newBitmap = Bitmap.createBitmap(newBitmapWidth, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap, -mWidth + left, 0, null);
            entity.setBitmap(newBitmap);
            entity.setTop(top);
            mSpliceList.add(entity);
        } else
        {
            entity.setSplice(false);
        }

        dmAdded(entity);
        bitmap.recycle();
    }

    /**
     * 弹幕从左往右跑
     * 绘制弹幕：从画布右到左，上到下绘制
     */
    private void drawLeftRight(BaseDmEntity entity)
    {
        Bitmap bitmap = entity.getBitmap();
        final float left;
        final float top;
        if (entity.isSplice())
        {
            final float diff = mWidth - bitmap.getWidth();
            left = diff < 0 ? 0 : diff;
            top = entity.getTop();
        } else if (mUseWidth == 0 && mUseHeight == 0)//第一次添加的情况
        {
            left = mWidth - bitmap.getWidth() - random.nextInt(100);
            top = 0;
        } else if (mWidth - mUseWidth > bitmap.getWidth())//左侧空间足到情况
        {
            left = mWidth - mUseWidth - bitmap.getWidth();
            top = mUseHeight - bitmap.getHeight();
        } else if (mWidth - mUseWidth < bitmap.getWidth() && mHeight - mUseHeight > bitmap.getHeight())//左侧空间不足，底部空间足到情况
        {
            left = mWidth - bitmap.getWidth() - random.nextInt(100);
            top = mUseHeight;
        } else //此时画布空间不足了，将这个弹幕添加到容器当中
        {
            mQueue.offer(entity);
            return;
        }

        mUseWidth = mWidth - left;
        mUseHeight = top + bitmap.getHeight();
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);

        if (left < 0)
        {
            final int newBitmapWidth = (int) (Math.abs(left));
            entity.setSplice(true);
            Bitmap newBitmap = Bitmap.createBitmap(newBitmapWidth, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            entity.setBitmap(newBitmap);
            entity.setTop(top);
            mSpliceList.add(entity);
        } else
        {
            entity.setSplice(false);
        }

        dmAdded(entity);
        bitmap.recycle();
    }



    /**
     * 将队列中未画在画布上到弹幕展示出来
     *
     * @param queue 将保存在队列中到弹幕展示出来
     */
    private void addFromQueue(PriorityQueue<BaseDmEntity> queue)
    {
        if (!isActive) return;
        LogUtil.i("addFromQueue method");
        if (queue.peek() != null)
        {
            BaseDmEntity entity = queue.remove();
            if (mWidth - mUseWidth > entity.getBitmap().getWidth() || mHeight - mUseHeight > entity.getBitmap().getHeight())
            {
                add(entity);
            } else
            {
                return;
            }
        }

        if (queue.peek() != null)
        {
            addFromQueue(queue);
        }
    }

    public synchronized void add(View templateView)
    {
        if (!isActive) return;
        BaseDmEntity entity = new BaseDmEntity(templateView);
        add(entity);
    }

    public synchronized void add(BaseDmEntity entity)
    {
        if (!isActive) return;
//        LogUtil.i("num = " + num + ", mUseWidth = " + mUseWidth + ", mUseHeight = " + mUseHeight + " --- mWidth = " + mWidth + ", mHeight = " + mHeight);
        switch (mDirection)
        {
            case RIGHT_LEFT:
                drawRightLeft(entity);
                break;
            case LEFT_RIGHT:
                drawLeftRight(entity);
                break;
            case UP_DOWN:
                drawUpDown(entity);
                break;
            case DOWN_UP:
                drawDownUp(entity);
                break;
            default:
                break;
        }

    }

    private void dmAdded(BaseDmEntity dmEntity)
    {
        if (mOnDMAddListener == null) return;
        mOnDMAddListener.added(dmEntity);
    }

    public void setOnDMAddListener(OnDMAddListener l)
    {
        this.mOnDMAddListener = l;
    }

    private long mCurrentPlayTime;

    void prepare() {
        if (mValueAnim == null || mValueAnim.isStarted()) {
            return;
        }
        mValueAnim.setCurrentPlayTime(mCurrentPlayTime);
        mValueAnim.start();
    }

    void pause() {
        mCurrentPlayTime = mValueAnim.getCurrentPlayTime();
        mValueAnim.cancel();
    }

    void destroy()
    {
        isActive = false;
        mValueAnim.cancel();
        Util.restoreBitmap(mOneBitmap);
        Util.restoreBitmap(mTwoBitmap);
        Util.restoreBitmap(mDrawDMBitmap);
    }
}
