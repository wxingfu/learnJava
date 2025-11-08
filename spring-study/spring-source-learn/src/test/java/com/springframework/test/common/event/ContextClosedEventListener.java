package com.springframework.test.common.event;

import com.springframework.context.ApplicationListener;
import com.springframework.context.event.ContextClosedEvent;

/**
 *
 *
 * @since 2022-06-24
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println(this.getClass().getName());
    }
}
