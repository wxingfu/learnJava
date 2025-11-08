package com.weixf.behavioral.observer.old;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @since 2022-08-23
 */
public class SubjectImpl implements Subject {
    private final List<Observer> list = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        list.add(o);
    }

    @Override
    public void notifyAllObserver(String orderNo) {
        list.forEach(c -> c.notify(orderNo));
    }
}
