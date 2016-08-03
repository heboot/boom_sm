package com.codingfeel.sm.model;

/**
 * Created by Heboot on 16/7/19.
 */
public class EditDataImgModel {

    private String localPath;
    private String qiniuPath;

    public EditDataImgModel(String localPath, String qiniuPath) {
        this.localPath = localPath;
        this.qiniuPath = qiniuPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }
}
