package com.xujiaji.dmview2;

import android.os.Handler;
import android.os.Message;

public abstract class CustomTimer {
private static final int MSG = 1;
private final long mCountdownInterval;
private boolean isStop = false;
private boolean isPause = false;
private Handler mHandler = new Handler() {
public void handleMessage(Message msg) {
if (msg.what == MSG) {
synchronized (CustomTimer.this) {
if (!CustomTimer.this.isStop && !CustomTimer.this.isPause) {
CustomTimer.this.onTick();
this.sendMessageDelayed(this.obtainMessage(1), mCountdownInterval);
}
}
}
}
};

public CustomTimer(long countDownInterval) {
    this.mCountdownInterval = countDownInterval;
}

private synchronized CustomTimer startTimer() {
    this.isStop = false;
    this.mHandler.sendMessage(this.mHandler.obtainMessage(MSG));
    return this;
}

public final synchronized void start() {
    this.startTimer();
}

public final synchronized void stop() {
    this.isStop = true;
    this.mHandler.removeMessages(MSG);
}

public final synchronized void pause() {
    if (!this.isStop) {
        this.isPause = true;
        this.mHandler.removeMessages(MSG);
    }
}

public final synchronized void restart() {
    if (!this.isStop && this.isPause) {
        this.isPause = false;
        this.startTimer();
    }
}

public abstract void onTick();
}

