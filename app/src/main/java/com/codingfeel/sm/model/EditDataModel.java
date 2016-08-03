package com.codingfeel.sm.model;

import android.graphics.Bitmap;

/**
 * Created by Heboot on 16/7/19.
 */
public class EditDataModel {

    private String inputStr;
    private String imagePath;
    private Bitmap bitmap;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
