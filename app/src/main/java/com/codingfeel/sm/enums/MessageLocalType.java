package com.codingfeel.sm.enums;

/**
 * Created by Heboot on 16/7/27.
 */
public enum MessageLocalType {


    NORMAL(0),
    SYSTEM(1);


    private int value = 0;

    private MessageLocalType(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }


}
