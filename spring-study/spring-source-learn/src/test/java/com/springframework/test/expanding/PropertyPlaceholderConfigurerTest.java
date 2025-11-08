package com.springframework.test.expanding;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Car;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class PropertyPlaceholderConfigurerTest {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:property-placeholder-configurer.xml");

        Car car = applicationContext.getBean("car", Car.class);
        Assert.assertEquals(car.getBrand(), "lamborghini");
    }
}
