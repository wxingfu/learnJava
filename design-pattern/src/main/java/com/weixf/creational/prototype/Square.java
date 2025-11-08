package com.weixf.creational.prototype;

/**
 *
 *
 * @since 2022-08-25
 */
public class Square extends Shape {

    public Square() {
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }

}
