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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xujiaji.dmlib2.DM;
import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.R;

/**
 * 用SurfaceView实现弹幕
 * Created by jiaji on 2018/2/19.
 */

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback, DM
{
    private SurfaceHolder mSurfaceHolder;
    private Controller mController;
    private Direction mDirection;
    private int mDuration = 3000;

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
        mController = new Controller();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        initAttr(context.obtainStyledAttributes(attrs, R.styleable.DMSurfaceView, defStyleAttr, 0));
    }

    /**
     * 初始化参数
     */
    private void initAttr(TypedArray a)
    {
        mDirection = Direction.getType(a.getInt(R.styleable.DMSurfaceView_direction, Direction.RIGHT_LEFT.value));
        mDuration = a.getInt(R.styleable.DMSurfaceView_duration, 3000);
        a.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mController.init(width, height, mDuration, mDirection, mSurfaceHolder);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mController.destroy();
    }


    @Override
    public Controller getController()
    {
        return mController;
    }
}
