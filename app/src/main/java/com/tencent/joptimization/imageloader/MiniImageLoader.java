package com.tencent.joptimization.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author: Jeremy
 * date: 2018/8/9
 * desc:
 */
public class MiniImageLoader extends ImageLoader {
    private volatile static MiniImageLoader miniImageLoader;

    private MiniImageLoader() {
    }

    public static MiniImageLoader getInstance() {
        if (miniImageLoader == null) {
            synchronized (MiniImageLoader.class) {
                if (miniImageLoader == null) {
                    miniImageLoader = new MiniImageLoader();
                }
            }
        }
        return miniImageLoader;
    }


    @Override
    protected Bitmap downLoadBitmap(String urlString) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            Bitmap bitmap = deCodeSampleBitmapFromStream(in);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Bitmap deCodeSampleBitmapFromStream(InputStream in) {
        return BitmapFactory.decodeStream(in);
    }
}
