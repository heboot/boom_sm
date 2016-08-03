package com.codingfeel.sm.http;

/**
 * Created by Heboot on 16/6/24.
 */
public class Api {

    String ACTION_VERSION = "/v1";


    private static volatile Api api;

    public static Api getInstance() {
        if (null == api) {
            synchronized (Api.class) {
                if (null == api) {
                    api = new Api();
                }
            }
        }
        return api;
    }

}
