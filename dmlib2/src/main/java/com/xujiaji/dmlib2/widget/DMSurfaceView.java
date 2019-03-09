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
import com.xujiaji.dmlib2.LogUtil;
import com.xujiaji.dmlib2.R;
import com.xujiaji.dmlib2.SurfaceProxy;
import com.xujiaji.dmlib2.Util;

/**
 * 用SurfaceView实现弹幕
 * Created by jiaji on 2018/2/19.
 */

public class DMSurfaceView extends SurfaceView implements SurfaceHolder.Callback, DM {
    private SurfaceHolder mSurfaceHolder;
    private Controller mController;
    private int mWidth;
    private int mHeight;

    public DMSurfaceView(Context context) {
        this(context, null);
    }

    public DMSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DMSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mController = new Controller();
        initHolder();

        TypedArray a                = context.obtainStyledAttributes(attrs, R.styleable.DMSurfaceView, defStyleAttr, 0);

        final Direction direction   = Direction.getType(a.getInt(R.styleable.DMSurfaceView_dm_direction, Direction.RIGHT_LEFT.value));
        final int span              = a.getDimensionPixelOffset(R.styleable.DMSurfaceView_dm_span, Util.dp2px(context, 2));
        final int sleep             = a.getInteger(R.styleable.DMSurfaceView_dm_sleep, 0);
        final int spanTime          = a.getInteger(R.styleable.DMSurfaceView_dm_span_time, 0);
        final int vSpace            = a.getDimensionPixelOffset(R.styleable.DMSurfaceView_dm_v_space, Util.dp2px(context, 10));
        final int hSpace            = a.getDimensionPixelOffset(R.styleable.DMSurfaceView_dm_h_space, Util.dp2px(context, 10));

        a.recycle();

        mController.setDirection(direction);
        mController.sethSpace(hSpace);
        mController.setvSpace(vSpace);
        mController.setSpan(span);
        mController.setSpanTime(spanTime == 0 ? sleep : spanTime);
    }

    private void initHolder() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mController.prepare();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mWidth == width && mHeight == height) return;
        this.mWidth = width;
        this.mHeight = height;
        mController.init(width, height, new SurfaceProxy(mSurfaceHolder));
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        LogUtil.e("DMSurfaceView onWindowFocusChanged() - > " + hasWindowFocus);
        if (hasWindowFocus) {
            mController.prepare();
        }
        else {
            mController.pause();
//            mController.draw(0, true);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e("DMSurfaceView surfaceDestroyed()");
        mController.pause();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e("DMSurfaceView onDetachedFromWindow()");
        mController.destroy();
    }

    @Override
    public Controller getController() {
        return mController;
    }
}
