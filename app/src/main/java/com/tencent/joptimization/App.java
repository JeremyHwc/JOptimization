package com.tencent.joptimization;

import android.app.Application;
import android.os.Trace;
import android.util.Log;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Debug.startMethodTracing("App");
        Trace.beginSection("Apponcreate");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.e("Test","thread");
            }
        };
        thread.start();

        new Runnable(){
            @Override
            public void run() {
                Log.e("Test","test");
            }
        }.run();
//        Debug.stopMethodTracing();
        Trace.endSection();
        init();
    }

    public void init(){

    }
}
