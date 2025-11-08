package com.springframework.test.ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import com.springframework.test.bean.Car;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class PackageScanTest {

    @Test
    public void testScanPackage() throws Exception {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:package-scan.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assert.assertNotNull(car);
    }
}
