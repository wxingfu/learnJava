package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-23
 */
public class InitAndDestroyMethodTest {

    @Test
    public void testInitAndDestroyMethod() {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");
        applicationContext.registerShutdownHook();
        // 或者手动关闭 applicationContext.close();
        // applicationContext.close();
    }
}

