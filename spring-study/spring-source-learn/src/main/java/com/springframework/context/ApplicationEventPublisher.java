package com.springframework.context;

/**
 * 事件发布者接口
 *
 * @since 2022-06-23
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     *
     * @param event
     */
    void publishEvent(ApplicationEvent event);
}
