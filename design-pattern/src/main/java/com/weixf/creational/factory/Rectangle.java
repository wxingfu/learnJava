package com.weixf.creational.factory;

/**
 *
 *
 * @since 2022-08-24
 */
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
