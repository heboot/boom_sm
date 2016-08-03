package com.codingfeel.sm.event;

/**
 * Created by Heboot on 16/7/18.
 */
public class QiNiuEvent {

    public static class UploadCompleteEvent {

        private String localPath;
        private String qiniuPath;

        public UploadCompleteEvent(String localPath, String qiniuPath) {
            this.localPath = localPath;
            this.qiniuPath = qiniuPath;
        }

        public String getLocalPath() {
            return localPath;
        }


        public String getQiniuPath() {
            return qiniuPath;
        }

    }

}
