package com.codingfeel.sm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Heboot on 16/8/1.
 */
public class VerisonUtil {
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String verisonName = info.versionName;
            verisonName = verisonName.replace(".", "");
            return Integer.parseInt(verisonName);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String verisonName = info.versionName;
            return verisonName;
        } catch (Exception e) {
            return "";
        }
    }

}
