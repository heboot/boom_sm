package com.codingfeel.sm.enums;

/**
 * Created by Heboot on 16/7/13.
 */
public enum VerifyStatus {

    CHECK_ING(0),
    CHECK_SUC(1),
    CHECK_ERR(2);


    private int value = 0;

    private VerifyStatus(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }


}
