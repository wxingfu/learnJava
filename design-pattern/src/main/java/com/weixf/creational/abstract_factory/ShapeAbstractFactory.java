package com.weixf.creational.abstract_factory;

/**
 *
 *
 * @since 2022-08-24
 */
public abstract class ShapeAbstractFactory {

    public abstract Shape getShape(String shape);

    public abstract Color getColor(String color);

}
