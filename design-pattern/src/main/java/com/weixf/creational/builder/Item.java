package com.weixf.creational.builder;

/**
 *
 *
 * @since 2022-08-25
 */
public interface Item {

    String name();

    Packing packing();

    float price();
}
