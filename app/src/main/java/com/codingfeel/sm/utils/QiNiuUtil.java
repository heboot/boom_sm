package com.codingfeel.sm.utils;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.QiNiuEvent;
import com.codingfeel.sm.service.UserService;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Heboot on 16/7/18.
 */
public class QiNiuUtil {

    private static UploadManager uploadManager;

    public static void doUpload(final String filePath, String bucketName) {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }

        File file = new File(filePath);

        final String qiniuPath = packageUploadFileName(bucketName);

        String token = MyApplication.getInstance().getUploadToken();

        uploadManager.put(file, qiniuPath, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    EventBus.getDefault().post(new QiNiuEvent.UploadCompleteEvent(filePath, MyApplication.getInstance().getQiniuPrefix() + qiniuPath));
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {

            }
        }, null));

    }


    // BuildConfig.QINIU_SERVER + "/"
    public static String packageUploadFileName(String bucketName) {
        String qiniuFileName = "";
        if (bucketName.equals(ConstantValue.QINIU_BUCKETNAME_HEAD)) {
            qiniuFileName = bucketName + "/" + UserService.getInstance().getLoginUser().getUid() + "/avatar.jpg";
        } else if (bucketName.equals(ConstantValue.QINIU_BUCKETNAME_POST)) {
            qiniuFileName = bucketName + "/" + UserService.getInstance().getLoginUser().getUid() + "/post/" + System.currentTimeMillis() + ".jpg";
        }
//        、、bucketName + "/" + UserService.getInstance().getLoginUser().getUid() + "/" +
        return qiniuFileName;
    }

}
