package com.weixf.behavioral.observer.old;

/**
 *
 *
 * @since 2022-08-23
 */
public interface Subject {
    void registerObserver(Observer o);

    void notifyAllObserver(String orderNo);
}
