package com.springframework.test.common.event;

import com.springframework.context.ApplicationContext;
import com.springframework.context.event.ApplicationContextEvent;

/**
 *
 *
 * @since 2022-06-24
 */
public class CustomEvent extends ApplicationContextEvent {

    public CustomEvent(ApplicationContext source) {
        super(source);
    }
}

