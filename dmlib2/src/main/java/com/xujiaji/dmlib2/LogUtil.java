package com.xujiaji.dmlib2;

import android.util.Log;

public class LogUtil
{
    private static final String TAG = "DMLog";
    private static final boolean CLOSE = false;

    public static void e(String msg)
    {
        if (CLOSE) return;
        Log.e(TAG, msg);
    }

    public static void i(String msg)
    {
        if (CLOSE) return;
        Log.i(TAG, msg);
    }
}
