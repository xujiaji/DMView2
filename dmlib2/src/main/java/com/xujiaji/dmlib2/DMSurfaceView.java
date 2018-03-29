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

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String TAG = "DMSurfaceView";
    private SurfaceHolder mSurfaceHolder;
    private PriorityQueue<BaseDmEntity> mQueue = new PriorityQueue<>();
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


    private void update(List<BaseDmEntity> list)
    {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
//        for (BaseDmEntity dmEntity : list)
//        {
//            PointF position = dmEntity.getPositions().poll();
//            if (position == null) continue;
//            Log.e(TAG, "("+ position.x + ", " + position.y + ")");
//            canvas.drawPoint(position.x, position.y, paint);
//            canvas.drawBitmap(dmEntity.getBitmap(), position.x, position.y, null);
//        }

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
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

        mTwoLeft = 0;
        mOneLeft = mWidth;

        mValueAnim = ValueAnimator.ofInt(0, mWidth)
                .setDuration(3000);
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
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawBitmap(mOneBitmap, mOneLeft - value, 0, null);
        canvas.drawBitmap(mTwoBitmap, mTwoLeft - value, 0, null);

//        if (mQueue.peek() != null)
//        {
//            BaseDmEntity entity = mQueue.remove();
//            mOneCanvas.drawBitmap(entity.getBitmap(), 0, 0, null);
//        }

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mValueAnim.end();
        DM.restoreBitmap(mOneBitmap);
        DM.restoreBitmap(mTwoBitmap);
        DM.restoreBitmap(mDrawDMBitmap);
    }

    Random random = new Random();
    /**
     * 加入一个元素
     */
    public void addElem(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.barrage, null);
        PointF start = new PointF(900, 100);
        PointF end = new PointF(200, 100);
        BaseDmEntity entity = new BaseDmEntity(0, view, start, end);
//        mQueue.offer(entity);

        Bitmap bitmap = entity.getBitmap();
        int left = 0;
        int top = 0;
        if (mUseWidth == 0 && mUseHeight == 0)
        {
            left = random.nextInt(100);
            top = 0;
        } else if (mWidth - mUseWidth > bitmap.getWidth())
        {
            left = mUseWidth;
            top = mUseHeight - bitmap.getHeight();
        } else if (mWidth - mUseWidth < bitmap.getWidth())
        {
            left = random.nextInt(100);
            top = mUseHeight;
        }

        mUseWidth = left + bitmap.getWidth() + random.nextInt(100);
        mUseHeight = top + bitmap.getHeight();
        mDrawDMCanvas.drawBitmap(bitmap, left, top, null);
    }

}
