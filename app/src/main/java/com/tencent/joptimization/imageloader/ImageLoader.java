package com.tencent.joptimization.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * author: Jeremy
 * date: 2018/8/7
 * desc:
 */
public abstract class ImageLoader {
    private boolean mExitTasksEarly = false;//是否提前退出的标志
    protected boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();

    public ImageLoader() {
    }

    public void loadImage(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        BitmapDrawable bitmapDrawable = null;
        if (bitmapDrawable != null) {
            imageView.setImageDrawable(bitmapDrawable);
        } else {
            BitmapLoadTask bitmapLoadTask = new BitmapLoadTask(url, imageView);
            bitmapLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class BitmapLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String mUrl;
        private final WeakReference<ImageView> mImageViewWeakReference;

        public BitmapLoadTask(String url, ImageView imageView) {
            mUrl = null;
            mImageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            BitmapDrawable bitmapDrawable = null;
            synchronized (mPauseWorkLock) {
                try {
                    mPauseWorkLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap == null && !isCancelled() && mImageViewWeakReference.get() != null && !mExitTasksEarly) {
                bitmap = downLoadBitmap(mUrl);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled()||mExitTasksEarly){
                result=null;
            }
            ImageView imageView = mImageViewWeakReference.get();
            if (result!=null&&imageView!=null){
                setImageBitmap(imageView,result);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            synchronized (mPauseWorkLock){
                mPauseWorkLock.notifyAll();
            }
        }
    }

    private void setImageBitmap(ImageView imageView, Bitmap result) {
        imageView.setImageBitmap(result);
    }

    public void setPauseWork(boolean pauseWork){
        synchronized (mPauseWorkLock){
            mPauseWork=pauseWork;
            if (!mPauseWork){
                mPauseWorkLock.notifyAll();
            }
        }
    }

    public void setExitTasksEarly(boolean exitTasksEarly){
        mExitTasksEarly=exitTasksEarly;
        setPauseWork(false);
    }

    protected abstract Bitmap downLoadBitmap(String url);


}
