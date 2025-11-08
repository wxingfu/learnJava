package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.service.HelloService;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-23
 */
public class AwareInterfaceTest {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        Assert.assertNotNull(helloService.getApplicationContext());
        Assert.assertNotNull(helloService.getBeanFactory());
    }
}

