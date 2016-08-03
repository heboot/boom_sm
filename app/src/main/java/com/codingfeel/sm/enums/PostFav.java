package com.codingfeel.sm.enums;

/**
 * Created by Heboot on 16/7/2.
 */
public enum  PostFav {

    FAV(1),

    NO_FAV(0);


    private int value = 0;

    private PostFav(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }
}
