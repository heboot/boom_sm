package com.codingfeel.sm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharePrefUtil {
    private static String TAG = SharePrefUtil.class.getSimpleName();
    private final static String SP_NAME = "three_building";
    private static SharedPreferences sp;

    /**
     * 保存布尔值
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 保存字符串
     */
    public static void saveString(Context context, String key, String value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).apply();

    }

    public static void clear(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().clear().apply();
    }

    /**
     * 保存long型
     */
    public static void saveLong(Context context, String key, long value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 保存int型
     */
    public static void saveInt(Context context, String key, int value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 保存float型
     */
    public static void saveFloat(Context context, String key, float value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 获取字符值
     *
     * @return 已存储的string
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getString(key, defValue);
    }

    /**
     * 获取int值
     *
     * @return 存储的int值
     */
    public static int getInt(Context context, String key, int defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getInt(key, defValue);
    }

    /**
     * 获取long值
     *
     * @return 存储的long值
     */
    public static long getLong(Context context, String key, long defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @return 存储的float值
     */
    public static float getFloat(Context context, String key, float defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     *
     * @return 存储的布尔值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 将对象进行base64编码后保存到SharePref中
     */
    public static void saveObj(Context context, String key, Object object) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            // 将对象的转为base64码
            String objBase64 = EncryptUtils.Base64.encode(baos.toByteArray());
            sp.edit().putString(key, objBase64).apply();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     */
    public static Object getObj(Context context, String key) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        String objBase64 = sp.getString(key, null);
        if (TextUtils.isEmpty(objBase64))
            return null;
        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = EncryptUtils.Base64.decode(objBase64);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

        ObjectInputStream ois;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
