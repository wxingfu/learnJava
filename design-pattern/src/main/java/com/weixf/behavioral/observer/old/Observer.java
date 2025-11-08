package com.weixf.behavioral.observer.old;

/**
 *
 *
 * @since 2022-08-23
 * 思路：基于某个Subject主题，然后一堆观察者Observer注册到主题上，有事件发生时，subject根据注册列表，去通知所有的observer
 */
public interface Observer {
    void notify(String orderNo);
}
