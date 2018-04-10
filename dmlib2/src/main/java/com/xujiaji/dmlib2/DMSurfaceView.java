package com.xujiaji.dmlib2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xujiaji.dmlib2.entity.BaseDmEntity;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 用SurfaceView实现弹幕
 * Created by jiaji on 2018/2/19.
 */

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback, DM
{
    public static final String TAG = "DMSurfaceView";
    private Direction mDirection = Direction.RIGHT_LEFT;
    private int mDuration = 3000;
    private SurfaceHolder mSurfaceHolder;
    private PriorityQueue<BaseDmEntity> mQueue = new PriorityQueue<>();
    private boolean isActive;//是否是活跃状态
    private Bitmap mOneBitmap;
    private Bitmap mTwoBitmap;
    private Bitmap mDrawDMBitmap;

    private Canvas mOneCanvas;
    private Canvas mTwoCanvas;
    private Canvas mDrawDMCanvas;
    private int mUseWidth;//绘制了的弹幕占用的宽
    private int mUseHeight;//绘制了的弹幕占用的高

    private int mWidth, mHeight;
    private ValueAnimator mValueAnim;
    private int mOneLeft, mTwoLeft;

    public DMSurfaceView(Context context)
    {
        this(context, null);
    }

    public DMSurfaceView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DMSurfaceView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        isActive = true;
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
                mValueAnim = ValueAnimator.ofInt(0, mHeight).setDuration(mDuration);
                break;
            case UP_DOWN:
                mValueAnim = ValueAnimator.ofInt(0, mHeight).setDuration(mDuration);
                break;
            case LEFT_RIGHT:
                mTwoLeft = -mWidth;
                mOneLeft = 0;
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
                int value = (int) animation.getAnimatedValue();
                startRun(value);
            }
        });

        mValueAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                LogUtil.i("onAnimationRepeat");
                mUseWidth = 0;
                mUseHeight = 0;
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
                mDrawDMCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清理后台绘制弹幕的画布

                addFromQueue(mQueue);
            }
        });

        mValueAnim.start();
    }

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
            canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null)
            {
                mValueAnim.cancel();
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
                        break;
                    case UP_DOWN:
                        break;
                    case DOWN_UP:
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
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        isActive = false;
        mValueAnim.cancel();
        Util.restoreBitmap(mOneBitmap);
        Util.restoreBitmap(mTwoBitmap);
        Util.restoreBitmap(mDrawDMBitmap);
    }

    Random random = new Random();

    @Override
    public synchronized void add(View templateView)
    {
        if (!isActive) return;
        BaseDmEntity entity = new BaseDmEntity(templateView);
        add(entity);
    }

    @Override
    public synchronized void add(BaseDmEntity entity)
    {
        if (!isActive) return;
//        LogUtil.i("num = " + num + ", mUseWidth = " + mUseWidth + ", mUseHeight = " + mUseHeight + " --- mWidth = " + mWidth + ", mHeight = " + mHeight);
        Bitmap bitmap = entity.getBitmap();
        int left = 0;
        int top = 0;
        if (mUseWidth == 0 && mUseHeight == 0)//第一次添加的情况
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

        mUseWidth = left + bitmap.getWidth() + random.nextInt(100);
        mUseHeight = top + bitmap.getHeight();
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);
    }

    @Override
    public void addFromQueue(PriorityQueue<BaseDmEntity> queue)
    {
        if (!isActive) return;
        LogUtil.i("addFromQueue method");
        if (queue.peek() != null)
        {
            BaseDmEntity entity = queue.remove();
            if (mHeight - mUseHeight > entity.getBitmap().getHeight())
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
}
