package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class AutowiredAnnotationTest {

    @Test
    public void testAutowiredAnnotation() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:autowired-annotation.xml");
        Person person = applicationContext.getBean(Person.class);
        Assert.assertNotNull(person.getCar());
    }
}
