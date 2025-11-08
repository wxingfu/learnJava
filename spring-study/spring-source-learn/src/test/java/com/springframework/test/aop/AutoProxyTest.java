package com.springframework.test.aop;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.service.WorldService;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class AutoProxyTest {

    @Test
    public void testAutoProxy() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:auto-proxy.xml");
        // 获取代理对象
        WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
        worldService.explode();
    }

    @Test
    public void testPopulateProxyBeanWithPropertyValues() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:populate-proxy-bean-with-property-values.xml");
        // 获取代理对象
        WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
        worldService.explode();

        Assert.assertEquals(worldService.getName(), "earth");
    }
}

