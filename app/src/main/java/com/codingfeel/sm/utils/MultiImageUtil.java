package com.codingfeel.sm.utils;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Heboot on 16/7/26.
 */
public class MultiImageUtil {

    private static MultiImageUtil multiImageUtil;
    private static MultiImageSelector multiImageSelector;

    public static MultiImageUtil getInstance() {
        if (multiImageUtil == null) {
            multiImageUtil = new MultiImageUtil();
        }
        return multiImageUtil;
    }

    public MultiImageSelector getMultiImageSelector(ArrayList<String> mSelectPath) {
        if (multiImageSelector == null) {
            multiImageSelector = MultiImageSelector.create();
        }
        multiImageSelector.showCamera(true);
        multiImageSelector.count(1);
        multiImageSelector.single();
        multiImageSelector.origin(mSelectPath);
        return multiImageSelector;
    }


}
