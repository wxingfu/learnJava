package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Car;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-23
 */
public class PrototypeBeanTest {

    @Test
    public void testPrototype() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:prototype-bean.xml");

        Car car1 = applicationContext.getBean("car", Car.class);
        Car car2 = applicationContext.getBean("car", Car.class);

        Assert.assertNotSame(car1, car2);
    }
}
