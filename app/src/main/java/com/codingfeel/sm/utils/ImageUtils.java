package com.codingfeel.sm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Heboot on 16/6/24.
 */
public class ImageUtils {

    /**
     * 展示图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url).into(imageView);
    }

    /**
     * 展示图片
     *
     * @param context
     * @param imageView
     */
    public static void displayImage(Context context, File file, ImageView imageView) {
        Picasso.with(context).load(file).into(imageView);
    }

    /**
     * 展示图片
     *
     * @param context
     * @param imageView
     */
    public static void displayImage(Context context, int resId, ImageView imageView) {
        Picasso.with(context).load(resId).into(imageView);
    }


    /**
     * 展示图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayImage(Context context, String url, ImageView imageView, int resId) {
        Picasso.with(context).load(url).placeholder(resId).into(imageView);
    }

    public static void displayInfoMarkImage(Context context, Integer type, ImageView imageView) {
        if (type == null) {
            return;
        }
        switch (type) {
            case ConstantValue.MAKER_TYPE_INFO_GUONEI:
                Picasso.with(context).load(R.mipmap.mark_guonei).into(imageView);
                break;
            case ConstantValue.MAKER_TYPE_INFO_HAITAO:
                Picasso.with(context).load(R.mipmap.mark_haitao).into(imageView);
                break;
        }
    }


    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        if (!new File(filePath).exists()) {
            return null;
        }
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 480, 800);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(filePath, options);
        } catch (Exception error) {
            error.printStackTrace();
        }
        return null;
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


}
