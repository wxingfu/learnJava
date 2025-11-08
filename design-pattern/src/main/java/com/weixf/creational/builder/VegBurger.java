package com.weixf.creational.builder;

/**
 *
 *
 * @since 2022-08-25
 */
public class VegBurger extends Burger {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }

}
