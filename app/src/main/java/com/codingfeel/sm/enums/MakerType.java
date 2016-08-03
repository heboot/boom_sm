package com.codingfeel.sm.enums;

/**
 * Created by Heboot on 16/7/16.
 */
public enum MakerType {


    INFO_YOUHUI(0),
    INFO_GUONEI(1),
    CHECK_ERR(2);


    private int value = 0;

    private MakerType(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }



}
