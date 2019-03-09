package com.tencent.joptimization;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.Trace;
import android.util.Log;

import com.github.anrwatchdog.ANRWatchDog;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Debug.startMethodTracing("App");
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Trace.beginSection("Apponcreate");
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                Log.e("Test","thread");
//            }
//        };
//        thread.start();

//        new Runnable(){
//            @Override
//            public void run() {
//                Log.e("Test","test");
//            }
//        }.run();
//        Debug.stopMethodTracing();
//        Trace.endSection();
        init();

//        new ANRWatchDog().start();
    }

    public void init() {

    }
}
