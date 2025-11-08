package com.springframework.context.event;

import com.springframework.context.ApplicationEvent;
import com.springframework.context.ApplicationListener;

/**
 *
 *
 * @since 2022-06-23
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);

}
