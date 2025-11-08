package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Car;
import com.springframework.test.bean.Person;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 *
 * @since 2022-06-23
 */
public class ApplicationContextTest {

    @Test
    public void testApplicationContext() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        // name属性在CustomBeanFactoryPostProcessor中被修改为ivy
        Assert.assertEquals(person.getName(), "ivy");

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        // brand属性在CustomerBeanPostProcessor中被修改为lamborghini
        Assert.assertEquals(car.getBrand(), "lamborghini");
    }
}
