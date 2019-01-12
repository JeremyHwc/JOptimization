package com.tencent.joptimization.launch;

import android.util.Log;

public class LaunchTimer {
    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        long time = System.currentTimeMillis() - sTime;
        Log.i("launchTime", "launchTime:" + time);
    }
}
