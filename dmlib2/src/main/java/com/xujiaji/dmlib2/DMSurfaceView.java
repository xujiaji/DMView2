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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.xujiaji.dmlib2.entity.BaseDmEntity;

import java.util.List;
import java.util.PriorityQueue;

/**
 * 用SurfaceView实现弹幕
 * Created by jiaji on 2018/2/19.
 */

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final String TAG = "DMSurfaceView";
    private SurfaceHolder mSurfaceHolder;
    private PriorityQueue<BaseDmEntity> mQueue = new PriorityQueue<>();
    private RefreshDMList mRefreshDMList = new RefreshDMList();
    private RecoveredDMList mDMRecoveredList = new RecoveredDMList();

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
        addListener();
    }

    private void addListener()
    {
        mRefreshDMList.setOnRefreshListener(new RefreshDMList.OnRefreshListener()
        {
            @Override
            public void refreshDM(List<BaseDmEntity> list)
            {
                update(list);
            }

            @Override
            public void overDM(BaseDmEntity dmEntity)
            {
                mDMRecoveredList.add(dmEntity);
            }
        });
    }

    private void update(List<BaseDmEntity> list)
    {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        for (BaseDmEntity dmEntity : list)
        {
            PointF position = dmEntity.getPositions().poll();
            if (position == null) continue;
            Log.e(TAG, "("+ position.x + ", " + position.y + ")");
            canvas.drawPoint(position.x, position.y, paint);
            canvas.drawBitmap(dmEntity.getBitmap(), position.x, position.y, null);
            PointCreator.getInstance().reset(position);
        }

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        addElem(getContext());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }


    /**
     * 加入一个元素
     */
    public void addElem(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.barrage, null);
        PointF start = new PointF(900, 100);
        PointF end = new PointF(200, 100);
        BaseDmEntity entity = new BaseDmEntity(0, view, start, end);
        mQueue.offer(entity);
        if (mQueue.size() == 1)
        {
            start();
        }
    }

    private void start()
    {
        if (mQueue.peek() == null) return;
        BaseDmEntity entity = mQueue.remove();
        mRefreshDMList.addDM(entity);
        entity.start();
        start();
    }

}
