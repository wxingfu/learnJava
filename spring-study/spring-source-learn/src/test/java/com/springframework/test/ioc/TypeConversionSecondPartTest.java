package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Car;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 *
 *
 * @since 2022-06-24
 */
public class TypeConversionSecondPartTest {

    @Test
    public void testConversionService() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:type-conversion-second-part.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assert.assertEquals(car.getPrice(), 1000000);
        Assert.assertEquals(car.getProduceDate(), LocalDate.of(2021, 1, 1));
    }
}
