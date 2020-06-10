package com.tutou.bigwork.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Soyang
 */
public class ImageUtil {

    /**
     * @Description: 根据图片网络地址设置imageView
     */
    public static void setImageToImageView(final ImageView imageView ,final String imgURL){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap)msg.obj;
            imageView.setImageBitmap(bitmap);
            }
        };
        new Thread(() -> {
            Bitmap bitmap = getBitmap(imgURL);
            Message msg = new Message();
            msg.obj = bitmap;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * @Description: 根据URL获取图片
     */
    private static Bitmap getBitmap(String url) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}


