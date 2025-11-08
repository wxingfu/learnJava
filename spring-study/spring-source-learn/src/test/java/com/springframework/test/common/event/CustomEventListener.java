package com.springframework.test.common.event;

import com.springframework.context.ApplicationListener;

/**
 *
 *
 * @since 2022-06-24
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(this.getClass().getName());
    }
}
