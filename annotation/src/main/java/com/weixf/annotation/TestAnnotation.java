package com.weixf.annotation;


@A("testClass")
public class TestAnnotation {

    @B("testMethod")
    public void sayHello() {
        System.out.println("hello word");
    }
}
