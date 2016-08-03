package com.codingfeel.sm.enums;

/**
 * Created by Heboot on 16/6/28.
 */
public enum AppType {

    FARAWAY(0),
    BAODIAN(1);


    private int value = 0;

    private AppType(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }

}
