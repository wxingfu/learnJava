package com.weixf.creational.builder;

/**
 *
 *
 * @since 2022-08-25
 */
public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();

}
