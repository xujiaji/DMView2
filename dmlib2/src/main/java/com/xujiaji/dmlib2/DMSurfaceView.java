package com.xujiaji.dmlib2;

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
import com.xujiaji.dmlib2.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 用SurfaceView实现弹幕
 * Created by jiaji on 2018/2/19.
 */

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder mSurfaceHolder;
    private PriorityQueue<BaseEntity> mQueue = new PriorityQueue<>();
    private List<BaseEntity> mRunEntity = new ArrayList<>();
    private List<BaseEntity> mIdleEntity = new ArrayList<>();

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

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    public Canvas lockCanvas()
    {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        return canvas;
    }

    public void unlockCanvas(Canvas canvas)
    {
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


    /**
     * 加入一个元素
     */
    public void addElem(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.barrage, null);
        PointF start = new PointF(900, 100);
        PointF end = new PointF(200, 100);
        BaseEntity entity = new BaseEntity(0, view, start, end);
        mQueue.offer(entity);
//        for (BaseEntity b : mQueue)
//        {
//            System.out.println(" " + b);
//            System.out.println(" " + mQueue.peek());
//        }
        if (mQueue.size() == 1)
        {
            start();
        }
    }

    private void start()
    {
        if (mQueue.peek() == null) return;
        BaseEntity entity = mQueue.remove();
        entity.start();
        start();
    }


    /***
     * 绘制元素
     */
    public void drawCurElem(Bitmap bitmap, PointF pointPosition)
    {
        Canvas canvas = lockCanvas();
        canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        canvas.drawPoint(pointPosition.x, pointPosition.y, paint);

        canvas.drawBitmap(bitmap, pointPosition.x, pointPosition.y, null);
        unlockCanvas(canvas);
    }

    public static Bitmap convertViewToBitmap(View view){
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
