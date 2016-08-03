package com.codingfeel.sm.utils;

import android.util.Log;

import com.codingfeel.sm.BuildConfig;


/**
 * Created by Heboot on 16/6/27.
 */
public class LogUtils {

    public static void e(String tag, String message) {
        if (BuildConfig.LOGDEBUG) {
            Log.e(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.LOGDEBUG) {
            Log.e(tag, message);
        }
    }

}
