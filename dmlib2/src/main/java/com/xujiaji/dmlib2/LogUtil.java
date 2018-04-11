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
