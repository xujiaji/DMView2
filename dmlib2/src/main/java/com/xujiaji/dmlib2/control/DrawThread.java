package com.xujiaji.dmlib2.control;

import android.graphics.Canvas;

public class DrawThread extends Thread {
    private volatile boolean isRun = false;
    private volatile boolean isDraw = false; // 是否绘制
    private int FRAME_INTERVAL = 10;// 默认帧时间10ms
    private OnDrawListener drawListener;
    private OnFrameListener frameListener;
    private final SurfaceProxy surfaceProxy;

    public DrawThread(SurfaceProxy surfaceProxy) {
        this.surfaceProxy = surfaceProxy;
    }

    @Override
    public synchronized void start() {
        isRun = true;
        isDraw = true;
        super.start();
    }

    @Override
    public void run() {
        while (isRun) {
            draw();
        }
    }

    /**
     * 绘制图画
     */
    private void draw() {
        if (isDraw && surfaceProxy != null) {
            long startTime = System.currentTimeMillis(); // 帧开始时间
            Canvas canvas = surfaceProxy.lockCanvas(); // 注意lock的时间消耗
            try {
                synchronized (surfaceProxy) {
                    // 调用外部接口
                    this.drawListener.onDraw(canvas);
                    // 调用外部接口
                    long endTime = System.currentTimeMillis();
                    // 计算出绘画一次更新的毫秒数
                    int diffTime = (int) (endTime - startTime); // 当前帧绘制时间

                    if (diffTime < FRAME_INTERVAL) {
                        try {
                            Thread.sleep(FRAME_INTERVAL - diffTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //发送一次循环运行总时间
                    if (frameListener != null)
                        frameListener.onFrameRate(System.currentTimeMillis() - startTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)
                    surfaceProxy.unlockCanvasAndPost(canvas);
            }
        } else { // fps: stop
            try {
                Thread.sleep(FRAME_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (frameListener != null) frameListener.onFrameRate(1000L);
        }
    }


    public void setOnDrawListener(OnDrawListener drawListener) {
        this.drawListener = drawListener;
    }

    public void setOnFrameListener(OnFrameListener frameListener) {
        this.frameListener = frameListener;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public boolean isRun() {
        return isRun;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public interface OnFrameListener {
        void onFrameRate(long time);
    }

    public interface OnDrawListener {
        void onDraw(Canvas canvas);
    }

}