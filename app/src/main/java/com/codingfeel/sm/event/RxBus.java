package com.codingfeel.sm.event;

import android.support.annotation.NonNull;

import java.util.Vector;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Heboot on 2017/1/14.
 */

public class RxBus {

    private static RxBus instance;

    private Vector<Subject> subjects = new Vector<>();

    private final Subject<Object, Object> bus;

    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static synchronized RxBus getInstance() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }


    public void post(Object object) {
        bus.onNext(object);
    }

}
