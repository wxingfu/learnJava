package com.springframework.context.event;

import com.springframework.context.ApplicationContext;
import com.springframework.context.ApplicationEvent;

/**
 *
 *
 * @since 2022-06-23
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
