package com.codingfeel.sm.model;

import android.graphics.Bitmap;

/**
 * Created by Heboot on 16/7/27.
 */
public class BitmapModel {
    private int index;
    private Bitmap bitmap;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public BitmapModel(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }
}
