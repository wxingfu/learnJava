package com.weixf.creational.abstract_factory;


/**
 *
 *
 * @since 2022-08-24
 */
public class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
