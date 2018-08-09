package com.tencent.joptimization.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * author: Jeremy
 * date: 2018/8/9
 * desc: 参数配置
 */
public class BitmapConfig {
    private int mWidth, mHeight;
    private Bitmap.Config mPreferred;

    public BitmapConfig(int width, int height) {
        this(width, height, null);
    }

    public BitmapConfig(int width, int height, Bitmap.Config preferred) {
        mWidth = width;
        mHeight = height;
        mPreferred = preferred;
    }

    public BitmapFactory.Options getBitmapOptions() {
        return getBitmapOptions(null);
    }

    private BitmapFactory.Options getBitmapOptions(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (is != null) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            options.inSampleSize = calculateInSampleSize(options, mWidth, mHeight);
        }
        options.inJustDecodeBounds = false;
        return options;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int outHeight = options.outHeight;
        final int outWidth = options.outWidth;
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            int halfHeight = outHeight / 2;
            int halfWidth = outWidth / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
