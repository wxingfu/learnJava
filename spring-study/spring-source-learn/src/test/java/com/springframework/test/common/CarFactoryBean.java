package com.springframework.test.common;

import com.springframework.beans.factory.FactoryBean;
import com.springframework.test.bean.Car;

/**
 *
 *
 * @since 2022-06-24
 */
public class CarFactoryBean implements FactoryBean<Car> {

    private String brand;

    @Override
    public Car getObject() throws Exception {
        Car car = new Car();
        car.setBrand(brand);
        return car;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

