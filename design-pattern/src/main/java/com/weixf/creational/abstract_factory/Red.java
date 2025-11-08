package com.weixf.creational.abstract_factory;

/**
 *
 *
 * @since 2022-08-24
 */
public class Red implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Red::fill() method.");
    }
}
