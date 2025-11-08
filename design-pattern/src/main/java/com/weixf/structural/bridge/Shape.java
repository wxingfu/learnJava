package com.weixf.structural.bridge;

/**
 *
 *
 * @since 2022-08-25
 */
public abstract class Shape {
    protected DrawAPI drawAPI;

    protected Shape(DrawAPI drawAPI) {
        this.drawAPI = drawAPI;
    }

    public abstract void draw();

}
