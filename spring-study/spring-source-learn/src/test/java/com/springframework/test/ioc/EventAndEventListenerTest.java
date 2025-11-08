package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.common.event.CustomEvent;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class EventAndEventListenerTest {

    @Test
    public void testEventListener() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();// 或者applicationContext.close()主动关闭容器;
    }
}
