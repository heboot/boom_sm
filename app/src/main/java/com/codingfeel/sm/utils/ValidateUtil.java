package com.codingfeel.sm.utils;

import android.text.TextUtils;

import com.codingfeel.sm.model.BaseModel;

/**
 * Created by Heboot on 16/7/16.
 */
public class ValidateUtil {

    public static boolean hasError(BaseModel baseModel){
        if (baseModel.getError() != null && !TextUtils.isEmpty(baseModel.getError_text())) {
            return  true;
        }
        return false;
    }

}
