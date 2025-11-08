package com.springframework.context.event;

import com.springframework.context.ApplicationContext;

/**
 *
 *
 * @since 2022-06-23
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
}
