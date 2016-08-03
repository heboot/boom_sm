package com.codingfeel.sm.event;

import android.graphics.Bitmap;

/**
 * Created by Heboot on 16/7/24.
 */
public class ImageEvent {

    public static class ImageCropEvent {

        private Bitmap bitmap;

        private String imgPath;

        public ImageCropEvent(String imgPath) {
            this.imgPath = imgPath;
        }

        public ImageCropEvent(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public String getImgPath() {
            return imgPath;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }

}
