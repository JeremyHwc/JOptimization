package com.tencent.joptimization;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Debug.stopMethodTracing();
        /*new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (MainActivity.this){
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (MainActivity.this){
            Log.e("BLOCKCANARY","卡顿测试");
        }*/


    }
    
}
