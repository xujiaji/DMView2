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
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.xujiaji.dmlib2.DM;
import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.R;
import com.xujiaji.dmlib2.SurfaceProxy;

public class DMTextureView extends TextureView implements TextureView.SurfaceTextureListener, DM
{
    private Surface mSurface;
    private Controller mController;
    private Direction mDirection;

    public DMTextureView(Context context)
    {
        this(context, null);
    }

    public DMTextureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DMTextureView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setSurfaceTextureListener(this);
        mController = new Controller();
        setOpaque(false);
        initAttr(context.obtainStyledAttributes(attrs, R.styleable.DMTextureView, defStyleAttr, 0));
    }

    /**
     * 初始化参数
     */
    private void initAttr(TypedArray a)
    {
        mDirection = Direction.getType(a.getInt(R.styleable.DMTextureView_direction, Direction.RIGHT_LEFT.value));
        a.recycle();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        mSurface = new Surface(surface);
        mController.init(width, height, mDirection, new SurfaceProxy(mSurface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        mSurface = null;
        mController.destroy();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {

    }

    @Override
    public Controller getController()
    {
        return mController;
    }
}
