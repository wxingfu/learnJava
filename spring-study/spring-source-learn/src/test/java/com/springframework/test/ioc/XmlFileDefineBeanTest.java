package com.springframework.test.ioc;

import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.springframework.test.bean.Car;
import com.springframework.test.bean.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-23
 */
public class XmlFileDefineBeanTest {

    @Test
    public void testXmlFile() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);

        Assert.assertEquals(person.getName(), "derek");
        Assert.assertEquals(person.getCar().getBrand(), "porsche");

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
        Assert.assertEquals(car.getBrand(), "porsche");
    }
}
