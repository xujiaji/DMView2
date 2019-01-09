package com.xujiaji.dmlib2;
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
import android.view.Surface;
import android.view.SurfaceHolder;

public class SurfaceProxy {
    private Surface mSurface;
    private SurfaceHolder mSurfaceHolder;

    public SurfaceProxy(Surface surface) {
        this.mSurface = surface;
    }

    public SurfaceProxy(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder = surfaceHolder;
    }

    public Canvas lockCanvas() {
        if (mSurfaceHolder != null) {
            return mSurfaceHolder.lockCanvas();
        } else {
            return mSurface.lockCanvas(null);
        }
    }

    public void unlockCanvasAndPost(Canvas canvas) {
        if (mSurfaceHolder != null) {
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        } else {
            mSurface.unlockCanvasAndPost(canvas);
        }
    }
}
