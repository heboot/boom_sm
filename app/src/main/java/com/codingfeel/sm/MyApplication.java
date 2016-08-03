package com.codingfeel.sm;

import android.app.Application;
import android.util.Log;

import com.codingfeel.sm.enums.ShareType;
import com.codingfeel.sm.model.PostTagModel;
import com.codingfeel.sm.utils.MiscUtils;
import com.tencent.bugly.crashreport.CrashReport;
//import com.tencent.bugly.crashreport.CrashReport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;


/**
 * Created by Heboot on 16/6/25.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    /**
     * app版本号
     */
    private String version;

    /**
     * token更新的时间戳
     */
    private long uploadTokenUpdateTime;

    /**
     * 七牛的上传token
     */
    private String uploadToken;

    /**
     * 个推的clientId
     */
    private String getuiCID;

    /**
     * 发帖时候的类型
     */
    private List<PostTagModel> postTagModelList;

    /**
     * 七牛的解析路径
     */
    private String qiniuPrefix;

    /**
     * 分享类型
     */
    private ShareType shareType;


    @Override
    public void onCreate() {
        super.onCreate();
//        Thread.currentThread().setUncaughtExceptionHandler(new
//                    MyThreadCatch());
        CrashReport.initCrashReport(getApplicationContext(), BuildConfig.BUGLY_APPID, false);
        myApplication = this;
        version = MiscUtils.getAppVersion(this);
    }


    public static MyApplication getInstance() {
        return myApplication;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getUploadTokenUpdateTime() {
        return uploadTokenUpdateTime;
    }

    public void setUploadTokenUpdateTime(long uploadTokenUpdateTime) {
        this.uploadTokenUpdateTime = uploadTokenUpdateTime;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }


    private class MyThreadCatch implements Thread.UncaughtExceptionHandler {


        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            ex.printStackTrace(pw);
            pw.close();
            Log.e("myex", w.toString());
        }
    }

    public String getGetuiCID() {
        return getuiCID;
    }

    public void setGetuiCID(String getuiCID) {
        this.getuiCID = getuiCID;
    }

    public List<PostTagModel> getPostTagModelList() {
        return postTagModelList;
    }

    public void setPostTagModelList(List<PostTagModel> postTagModelList) {
        this.postTagModelList = postTagModelList;
    }

    public String getQiniuPrefix() {
        return qiniuPrefix;
    }

    public void setQiniuPrefix(String qiniuPrefix) {
        this.qiniuPrefix = qiniuPrefix;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
}
