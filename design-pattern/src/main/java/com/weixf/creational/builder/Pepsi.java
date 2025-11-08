package com.weixf.creational.builder;

/**
 *
 *
 * @since 2022-08-25
 */
public class Pepsi extends ColdDrink {

    @Override
    public float price() {
        return 35.0f;
    }

    @Override
    public String name() {
        return "Pepsi";
    }

}
