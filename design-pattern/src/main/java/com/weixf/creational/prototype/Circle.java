package com.weixf.creational.prototype;

/**
 *
 *
 * @since 2022-08-25
 */

public class Circle extends Shape {

    public Circle() {
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
