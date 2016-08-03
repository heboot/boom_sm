package com.codingfeel.sm.utils;

import android.content.Context;

import com.igexin.sdk.PushManager;

/**
 * Created by Heboot on 16/7/24.
 */
public class PushUtil {

    public static void initPush(Context context) {
        PushManager.getInstance().initialize(context);
    }

    public static String getPushClientId(Context context) {
        return  PushManager.getInstance().getClientid(context);
    }

}
